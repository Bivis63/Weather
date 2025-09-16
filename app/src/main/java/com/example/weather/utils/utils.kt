package com.example.weather.utils

fun String.cityToPrepositional(): String {
    return when (this.lowercase()) {
        "москва" -> "Москве"
        "самара" -> "Самаре"
        "томск" -> "Томске"
        "казань" -> "Казани"
        "сочи" -> "Сочи"
        "пермь" -> "Перми"
        "омск" -> "Омске"
        "ростов" -> "Ростове"
        "уфа" -> "Уфе"
        "калуга" -> "Калуге"
        else -> this
    }
}

fun Int.toWeatherDescription(): String {
    return when (this) {
        in -50..15 -> "холодно"
        in 16..25 -> "нормально"
        in 26..50 -> "жарко"
        else -> "катастрофа"
    }
}