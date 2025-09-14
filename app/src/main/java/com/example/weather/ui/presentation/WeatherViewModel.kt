package com.example.weather.ui.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WeatherViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    val snackBarHostState = SnackbarHostState()
    fun updateCity(newCity: String) {
        _uiState.update {
            it.copy(
                city = newCity,
                buttonState = computeButtonState(it.copy(city = newCity))
            )
        }
    }

    fun updateTemperature(newTemp: String) {
        _uiState.update {
            it.copy(
                temperature = newTemp,
                buttonState = computeButtonState(it.copy(temperature = newTemp))
            )
        }
    }

    fun addCurrentRequest() {
        _uiState.update { state ->
            if (state.city.isNotEmpty() && state.temperature.isNotEmpty()) {
                val req = state.city to state.temperature
                state.copy(
                    currentRequest = req,
                    previousRequests = state.previousRequests?.plus(req),
                    buttonState = WeatherButtonState.NewRequest
                )
            } else state
        }
    }

    fun resetRequest() {
        _uiState.update { state ->
            state.copy(
                city = "",
                temperature = "",
                currentRequest = null,
                buttonState = WeatherButtonState.EvaluateDisabled
            )


        }
    }

    fun updateSelectedTab(index: Int) {
        _uiState.update { it.copy(selectedTabIndex = index) }
    }

    fun computeButtonState(state: WeatherUiState): WeatherButtonState {
        return if (state.currentRequest == null) {
            if (state.city.isNotEmpty() && state.temperature.isNotEmpty())
                WeatherButtonState.EvaluateEnabled
            else
                WeatherButtonState.EvaluateDisabled
        } else
            WeatherButtonState.NewRequest
    }
}