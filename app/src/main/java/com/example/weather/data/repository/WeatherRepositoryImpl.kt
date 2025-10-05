package com.example.weather.data.repository

import com.example.weather.data.datasourse.LocalWeatherDataSource
import com.example.weather.domain.model.WeatherRequest
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(
    private val localWeatherDataSource: LocalWeatherDataSource
) : WeatherRepository {
    override fun getWeatherRequest(): Flow<List<WeatherRequest>> {
        return localWeatherDataSource.weatherRequest
    }

    override suspend fun addWeatherRequest(request: WeatherRequest) {
        return localWeatherDataSource.addWeatherRequest(request)
    }

    override suspend fun clearCurrentRequest() {
        return localWeatherDataSource.clearRequests()
    }
}