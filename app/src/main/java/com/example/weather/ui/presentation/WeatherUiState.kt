package com.example.weather.ui.presentation

data class WeatherUiState(
    var city: String = "",
    var temperature: String = "",
    val currentRequest: Pair<String, String>? = null,
    var previousRequests: List<Pair<String, String>>? = emptyList(),
    var buttonState: WeatherButtonState = WeatherButtonState.EvaluateDisabled,
    var selectedTabIndex: Int = 0
)