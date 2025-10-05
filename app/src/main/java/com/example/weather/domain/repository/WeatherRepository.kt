package com.example.weather.domain.repository

import com.example.weather.domain.model.WeatherRequest
import kotlinx.coroutines.flow.Flow

interface WeatherRepository{
    fun getWeatherRequest(): Flow<List<WeatherRequest>>
    suspend fun addWeatherRequest(request: WeatherRequest)
    suspend fun clearCurrentRequest()
}