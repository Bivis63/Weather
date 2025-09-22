package com.example.weather.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

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

fun ComponentContext.componentScope(): CoroutineScope =
    CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()).apply {
        lifecycle.doOnDestroy { this.cancel() }
    }