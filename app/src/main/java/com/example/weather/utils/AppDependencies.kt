package com.example.weather.utils

import android.content.Context
import com.example.weather.data.database.NewsDao
import com.example.weather.data.database.NewsDatabase
import com.example.weather.data.datasourse.LocalWeatherDataSource
import com.example.weather.data.datasourse.RoomNewsDataSource
import com.example.weather.data.repository.NewsRepositoryImpl
import com.example.weather.data.repository.WeatherRepositoryImpl
import com.example.weather.domain.repository.NewsRepository
import com.example.weather.domain.usecase.AddWeatherRequestUseCase
import com.example.weather.domain.usecase.GetFavoritesUseCase
import com.example.weather.domain.usecase.GetNewsUseCase
import com.example.weather.domain.usecase.GetWeatherRequestsUseCase
import com.example.weather.domain.usecase.InitializeNewsDataUseCase
import com.example.weather.domain.usecase.ToggleNewsLikeUseCase
import com.example.weather.domain.usecase.ToggleNewsReadUseCase

object AppDependencies {
    // Пока нет Di

    private var database: NewsDatabase? = null
    private var newsDao: NewsDao? = null
    private var roomDataSource: RoomNewsDataSource? = null
    private var newsRepository: NewsRepository? = null

    fun initialize(context: Context) {
        if (database == null) {
            database = NewsDatabase.getDatabase(context)
            newsDao = database?.newsDao()
            roomDataSource = newsDao?.let { RoomNewsDataSource(it) }
            newsRepository = roomDataSource?.let { NewsRepositoryImpl(it) }
        }
    }

    fun getNewsRepository(): NewsRepository {
        return newsRepository ?: throw IllegalStateException("DatabaseModule not initialized.")
    }

    val weatherDataSource = LocalWeatherDataSource()
    val weatherRepository = WeatherRepositoryImpl(weatherDataSource)

    val getWeatherRequestsUseCase = GetWeatherRequestsUseCase(weatherRepository)
    val addWeatherRequestUseCase = AddWeatherRequestUseCase(weatherRepository)


    val getNewsUseCase by lazy { GetNewsUseCase(getNewsRepository()) }
    val toggleNewsLikeUseCase by lazy { ToggleNewsLikeUseCase(getNewsRepository()) }
    val toggleNewsReadUseCase by lazy { ToggleNewsReadUseCase(getNewsRepository()) }
    val getFavoritesUseCase by lazy { GetFavoritesUseCase(getNewsRepository()) }
    val initializeNewsDataUseCase by lazy { InitializeNewsDataUseCase(getNewsRepository()) }
}