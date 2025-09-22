package com.example.weather.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Category(val displayName: String) {
    ALL("Всё"),
    CULTURE("Культура"),
    TECHNOLOGY("Технологии"),
    TRAVEL("Путешествия");

    companion object {
        val allCategories = values().toList()
    }
}
