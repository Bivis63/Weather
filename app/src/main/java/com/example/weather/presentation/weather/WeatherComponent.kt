package com.example.weather.presentation.weather

import com.example.weather.domain.model.WeatherRequest
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface WeatherComponent {
    val model: StateFlow<Model>

    fun onCityChanged(city: String)
    fun onTemperatureChanged(temperature: String)
    fun onEvaluateClicked()
    fun onNewRequestClicked()

    @Serializable
    data class Model(
        val city: String = "",
        val temperature: String = "",
        val currentRequest: WeatherRequest? = null,
        val previousRequests: List<WeatherRequest> = emptyList(),
        val buttonState: WeatherButtonState = WeatherButtonState.EvaluateDisabled
    )

    enum class WeatherButtonState {
        EvaluateDisabled,
        EvaluateEnabled,
        NewRequest
    }
}