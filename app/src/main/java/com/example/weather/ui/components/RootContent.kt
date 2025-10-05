package com.example.weather.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weather.R
import com.example.weather.presentation.navigation.RootComponent
import com.example.weather.ui.theme.BottomNavItemSelectedBackground

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.collectAsState()
    val selectedTab = model.selectedTab

    Scaffold(
        modifier = modifier,
        containerColor = Color.Black,
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedTab,
                onWeatherClick = component::onWeatherTabClicked,
                onNewsClick = component::onNewsTabClicked,
                onFavoritesClick = component::onFavoritesTabClicked
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                0 -> WeatherContent(
                    component = component.weatherComponent,
                    modifier = Modifier.fillMaxSize()
                )

                1 -> NewsContent(
                    component = component.newsComponent,
                    modifier = Modifier.fillMaxSize()
                )

                2 -> FavoritesContent(
                    component = component.favoritesComponent,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedIndex: Int,
    onWeatherClick: () -> Unit,
    onNewsClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    val items = listOf(
        Triple(R.string.navigatiom_item_weather, R.drawable.icon_weather, onWeatherClick),
        Triple(R.string.navigatiom_item_news, R.drawable.icon_news, onNewsClick),
        Triple(R.string.navigatiom_item_gallery, R.drawable.icon_like, onFavoritesClick)
    )

    NavigationBar(
        modifier = Modifier.padding(6.dp),
        containerColor = Color.Black
    ) {
        items.forEachIndexed { index, (titleRes, iconRes, onClick) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(titleRes)) },
                selected = selectedIndex == index,
                onClick = onClick,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .height(53.dp)
                    .border(
                        width = 0.6.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(3.6.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selectedIndex == index) BottomNavItemSelectedBackground else Color.Transparent)
            )
        }
    }
}