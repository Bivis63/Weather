package com.example.weather.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.domain.model.NewsItem
import com.example.weather.presentation.news.NewsComponent
import com.example.weather.ui.theme.BorderColorDefault
import com.example.weather.ui.theme.ButtonBackgroundDisabled
import com.example.weather.ui.theme.TextDescriptionColor
import com.example.weather.ui.theme.WeatherCardBackground
import com.example.weather.ui.theme.WeatherCardText

@Composable
fun NewsContent(
    component: NewsComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.collectAsState()
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = model.scrollPosition)

    LaunchedEffect(listState.firstVisibleItemIndex) {
        component.onScrollPositionChanged(listState.firstVisibleItemIndex)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .padding(start = 18.dp, top = 12.dp, bottom = 8.dp)
                .align(Alignment.TopStart),
            text = stringResource(R.string.navigatiom_item_news),
            color = if (model.selectedNews != null) Color.DarkGray else Color.White,
            fontSize = 18.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = listState
        ) {
            items(model.newsList, key = { it.id }) { news ->
                NewsCard(
                    newsItem = news,
                    onClickCard = { component.onNewsCardClicked(news) },
                    onClickReadIcon = { component.onReadIconClicked(news) },
                    onClickLikeIcon = { component.onLikeIconClicked(news) }
                )
            }
        }

        model.selectedNews?.let { news ->
            FullNewsCardModal(
                newsItem = news,
                onDismiss = component::onDismissModal,
                onToggleRead = { component.onReadIconClicked(news) },
                onClickLikeIcon = { component.onLikeIconClicked(news) }
            )
        }
    }
}

@Composable
fun NewsCard(
    newsItem: NewsItem,
    onClickCard: () -> Unit,
    onClickReadIcon: () -> Unit,
    onClickLikeIcon: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (newsItem.isRead) Color(0xFF262626) else WeatherCardBackground)
            .clickable { onClickCard() }
            .padding(12.dp)
    ) {
        NewsContent(newsItem)
        Spacer(modifier = Modifier.height(8.dp))
        NewsActionsRow(
            isRead = newsItem.isRead,
            isLike = newsItem.isLike,
            onReadClick = onClickReadIcon,
            onLikeClick = onClickLikeIcon
        )
    }
}

@Composable
fun NewsContent(
    newsItem: NewsItem,
    isFullMode: Boolean = false
) {
    NewsHeader(newsItem.title, newsItem.timeAgo)
    Divider(
        modifier = Modifier.padding(vertical = 10.dp),
        thickness = 1.dp,
        color = BorderColorDefault
    )
    ArticleInfoRow(newsItem.articleType, newsItem.readingTime)
    Spacer(modifier = Modifier.height(4.dp))
    Image(
        painter = painterResource(newsItem.imageResId),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = newsItem.articleTitle,
        color = Color.White,
        fontSize = 16.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = newsItem.description,
        color = TextDescriptionColor,
        fontSize = 12.sp,
        maxLines = if (isFullMode) Int.MAX_VALUE else 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun NewsActionsRow(
    isRead: Boolean,
    isLike: Boolean,
    onReadClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            icon = painterResource(if (isRead) R.drawable.icon_is_reading else R.drawable.icon_no_reading),
            iconSize = 20.dp,
            onClick = onReadClick
        )
        Spacer(modifier = Modifier.width(6.dp))
        IconButton(
            icon = painterResource(if (isLike) R.drawable.icon_like else R.drawable.icon_heart),
            iconSize = 17.dp,
            onClick = onLikeClick
        )
    }
}

@Composable
fun FullNewsCardModal(
    newsItem: NewsItem,
    onDismiss: () -> Unit,
    onToggleRead: () -> Unit,
    onClickLikeIcon: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 35.dp)
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(start = 8.dp, end = 8.dp, top = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (newsItem.isRead) Color(0xFF262626) else WeatherCardBackground)
                .clickable { }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                NewsContent(newsItem, isFullMode = true)
                Spacer(modifier = Modifier.height(16.dp))
                NewsActionsRow(
                    isRead = newsItem.isRead,
                    isLike = newsItem.isLike,
                    onReadClick = onToggleRead,
                    onLikeClick = onClickLikeIcon
                )
            }
        }

        Box(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopEnd)
                .offset(x = (-16).dp, y = -20.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_close_24),
                contentDescription = stringResource(R.string.close),
                tint = Color.Black,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun NewsHeader(title: String, timeAgo: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .border(1.dp, BorderColorDefault, RoundedCornerShape(4.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(text = title, color = Color.White, fontSize = 14.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(R.drawable.icon_time),
                contentDescription = null,
                tint = WeatherCardText
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "$timeAgo минут назад", color = Color.White)
        }
    }
}

@Composable
fun ArticleInfoRow(articleType: String, readingTime: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = articleType, color = ButtonBackgroundDisabled, fontSize = 14.sp)
        Text(
            text = "Время чтения $readingTime минут",
            color = ButtonBackgroundDisabled,
            fontSize = 14.sp
        )
    }
}

@Composable
fun IconButton(
    icon: Painter,
    iconSize: Dp = 14.dp,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .size(width = 32.dp, height = 24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = Color.Unspecified
        )
    }
}
