package com.example.weather.domain.usecase

import com.example.weather.domain.model.WeatherRequest
import com.example.weather.domain.repository.WeatherRepository

class AddWeatherRequestUseCase(
    private val repository: WeatherRepository
) {
        suspend operator fun invoke(city: String, temperature: String): Result<Unit>{
            return try {
                val request = WeatherRequest(city,temperature)
                if (!request.isValid()){
                    return Result.failure(IllegalArgumentException("Invalid weather request"))
                }
                repository.addWeatherRequest(request)
                Result.success(Unit)
            }catch (e: Exception){
                Result.failure(e)
            }
        }
}