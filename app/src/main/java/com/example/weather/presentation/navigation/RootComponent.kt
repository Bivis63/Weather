package com.example.weather.presentation.navigation

import com.example.weather.domain.model.Tab
import com.example.weather.presentation.favorites.FavoritesComponent
import com.example.weather.presentation.news.NewsComponent
import com.example.weather.presentation.weather.WeatherComponent
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface RootComponent {
    val model: StateFlow<Model>
    val weatherComponent: WeatherComponent
    val newsComponent: NewsComponent
    val favoritesComponent: FavoritesComponent

    fun onWeatherTabClicked()
    fun onNewsTabClicked()
    fun onFavoritesTabClicked()

    @Serializable
    data class Model(
        val selectedTab: Tab = Tab.WEATHER
    )
}