package com.example.weather.domain.usecase

import com.example.weather.domain.model.WeatherRequest
import com.example.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetWeatherRequestsUseCase(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<List<WeatherRequest>>{
        return repository.getWeatherRequest()
    }
}