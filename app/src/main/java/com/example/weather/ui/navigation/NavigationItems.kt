package com.example.weather.ui.navigation

import com.example.weather.R

sealed class NavigationItems(
    val titleResId: Int,
    val iconResId: Int
) {
    object Weather: NavigationItems(
        titleResId = R.string.navigatiom_item_weather,
        iconResId = R.drawable.icon_folder
    )
    object News: NavigationItems(
        titleResId = R.string.navigatiom_item_news,
        iconResId = R.drawable.icon_news
    )
    object Gallery: NavigationItems(
        titleResId = R.string.navigatiom_item_gallery,
        iconResId = R.drawable.icon_folder
    )
}