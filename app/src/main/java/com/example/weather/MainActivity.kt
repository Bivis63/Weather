package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.example.weather.presentation.navigation.DefaultRootComponent
import com.example.weather.ui.components.RootContent
import com.example.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val rootComponent = DefaultRootComponent(
            componentContext = defaultComponentContext()
        )
        setContent {
            WeatherTheme {
                RootContent(
                    component = rootComponent
                )
            }
        }
    }
}
