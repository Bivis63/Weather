package com.example.weather.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.R
import com.example.weather.domain.model.Category
import com.example.weather.domain.model.NewsItem
import com.example.weather.presentation.favorites.FavoritesComponent
import com.example.weather.ui.theme.BorderColorDefault
import kotlinx.coroutines.delay

@Composable
fun FavoritesContent(
    component: FavoritesComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.collectAsState()

    Column(modifier = modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp)) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = stringResource(R.string.favorite_news),
            fontSize = 18.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Category.allCategories.forEach { category ->
                Chip(
                    title = category.displayName,
                    isSelected = model.selectedChip == category,
                    onClick = { component.onChipClicked(category) }
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (model.filteredFavorites.isEmpty()) {
            Text(
                "Нет избранных новостей",
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(model.filteredFavorites, key = { _, news -> news.id }) { index, news ->
                    AnimatedFavoriteCard(
                        news = news,
                        onLikeClicked = { component.onLikeClicked(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun CardItem(
    imageResId: Int,
    title: String,
    articleType: String,
    isLike: Boolean,
    onLikeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderColorDefault, RoundedCornerShape(4.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(imageResId),
            contentDescription = null,
            modifier = Modifier
                .width(64.dp)
                .height(63.dp)
                .padding(start = 6.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(22.5.dp))

        Column(
            modifier = Modifier
                .padding(top = 6.dp, bottom = 6.dp)
                .width(210.dp)
        ) {
            Text(
                text = articleType,
                color = Color.White,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .border(1.dp, BorderColorDefault, RoundedCornerShape(4.dp))
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = if (isLike) painterResource(R.drawable.icon_like)
            else painterResource(R.drawable.icon_no_likeble),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable { onLikeClick() }
        )
    }
}

@Composable
fun AnimatedFavoriteCard(
    news: NewsItem,
    onLikeClicked: (NewsItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(true) }
    var liked by remember { mutableStateOf(news.isLike) }

    LaunchedEffect(liked) {
        if (!liked) {
            delay(1000)
            visible = false
            delay(300)
            onLikeClicked(news)
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(),
        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(),
        modifier = modifier
    ) {
        CardItem(
            imageResId = news.imageResId,
            articleType = news.articleTitle,
            title = news.title,
            isLike = liked,
            onLikeClick = { liked = false }
        )
    }
}

@Composable
fun Chip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF343434) else Color.Transparent
    val borderColor = if (isSelected) Color(0xFF343434) else BorderColorDefault
    Box(
        modifier = Modifier
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .clickable { onClick() },

        ) {

        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 6.dp),
            text = title,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}