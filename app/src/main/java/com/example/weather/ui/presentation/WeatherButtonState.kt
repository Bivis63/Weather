package com.example.weather.ui.presentation

sealed class WeatherButtonState() {

    object EvaluateDisabled : WeatherButtonState()
    object EvaluateEnabled : WeatherButtonState()
    object NewRequest : WeatherButtonState()
}