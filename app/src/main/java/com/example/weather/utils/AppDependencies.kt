package com.example.weather.utils

import com.example.weather.data.datasourse.LocalNewsDataSource
import com.example.weather.data.datasourse.LocalWeatherDataSource
import com.example.weather.data.repository.NewsRepositoryImpl
import com.example.weather.data.repository.WeatherRepositoryImpl
import com.example.weather.domain.usecase.AddWeatherRequestUseCase
import com.example.weather.domain.usecase.GetFavoritesUseCase
import com.example.weather.domain.usecase.GetNewsUseCase
import com.example.weather.domain.usecase.GetWeatherRequestsUseCase
import com.example.weather.domain.usecase.ToggleNewsLikeUseCase
import com.example.weather.domain.usecase.ToggleNewsReadUseCase

class AppDependencies {
    // Пока нет Di
    val newsDataSource = LocalNewsDataSource()
    val newsRepository = NewsRepositoryImpl(newsDataSource)
    val weatherDataSource = LocalWeatherDataSource()
    val weatherRepository = WeatherRepositoryImpl(weatherDataSource)

    val getWeatherRequestsUseCase = GetWeatherRequestsUseCase(weatherRepository)
    val addWeatherRequestUseCase = AddWeatherRequestUseCase(weatherRepository)

    val getNewsUseCase = GetNewsUseCase(newsRepository)
    val toggleNewsLikeUseCase = ToggleNewsLikeUseCase(newsRepository)
    val toggleNewsReadUseCase = ToggleNewsReadUseCase(newsRepository)
    val getFavoritesUseCase = GetFavoritesUseCase(newsRepository)
}