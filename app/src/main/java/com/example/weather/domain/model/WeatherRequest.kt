package com.example.weather.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherRequest(
    val city: String,
    val temperature: String
){
    fun isValid(): Boolean = (city.isNotBlank() && temperature.isNotBlank())
}
