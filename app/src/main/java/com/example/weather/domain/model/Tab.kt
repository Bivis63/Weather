package com.example.weather.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Tab(val index: Int) {
    WEATHER(0),
    NEWS(1),
    FAVORITE(2);
}