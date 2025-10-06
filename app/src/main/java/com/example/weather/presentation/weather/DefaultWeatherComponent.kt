package com.example.weather.presentation.weather

import com.arkivanov.decompose.ComponentContext
import com.example.weather.utils.AppDependencies
import com.example.weather.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DefaultWeatherComponent(
    componentContext: ComponentContext,
    dependencies: AppDependencies
) : WeatherComponent, ComponentContext by componentContext {

    private val scope = componentScope()


    private val getWeatherRequestsUseCase = dependencies.getWeatherRequestsUseCase
    private val addWeatherRequestUseCase = dependencies.addWeatherRequestUseCase

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, strategy = WeatherComponent.Model.serializer())
            ?: WeatherComponent.Model()
    )

    override val model: StateFlow<WeatherComponent.Model>
        get() = _model.asStateFlow()

    init {
        stateKeeper.register(
            KEY,
            strategy = WeatherComponent.Model.serializer()
        ) { model.value }

        getWeatherRequestsUseCase()
            .onEach { requests ->
                _model.update { current ->
                    val intermediate = current.copy(
                        previousRequests = requests,
                        currentRequest = requests.lastOrNull()
                    )
                    intermediate.copy(
                        buttonState = computeButtonState(intermediate)
                    )
                }
            }
            .launchIn(scope)
    }

    override fun onCityChanged(city: String) {
        _model.update { current ->
            val intermediate = current.copy(city = city)
            intermediate.copy(
                buttonState = computeButtonState(intermediate)
            )
        }
    }

    override fun onTemperatureChanged(temperature: String) {
        _model.update { current ->
            val intermediate = current.copy(temperature = temperature)
            intermediate.copy(
                buttonState = computeButtonState(intermediate)
            )
        }
    }

    override fun onEvaluateClicked() {
        val currentModel = _model.value
        if (currentModel.city.isNotBlank() && currentModel.temperature.isNotBlank()) {
            scope.launch {
                addWeatherRequestUseCase(currentModel.city, currentModel.temperature)
            }
        }
    }

    override fun onNewRequestClicked() {
        _model.update {
            it.copy(
                city = "",
                temperature = "",
                currentRequest = null,
                buttonState = WeatherComponent.WeatherButtonState.EvaluateDisabled
            )
        }
    }

    private fun computeButtonState(model: WeatherComponent.Model): WeatherComponent.WeatherButtonState {
        return if (model.currentRequest == null) {
            if (model.city.isNotBlank() && model.temperature.isNotBlank()) {
                WeatherComponent.WeatherButtonState.EvaluateEnabled
            } else {
                WeatherComponent.WeatherButtonState.EvaluateDisabled
            }
        } else {
            WeatherComponent.WeatherButtonState.NewRequest
        }
    }
    companion object {
        private const val KEY = "DefaultWeatherComponent"
    }
}
