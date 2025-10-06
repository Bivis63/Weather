package com.example.weather.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.example.weather.domain.model.Tab
import com.example.weather.presentation.favorites.DefaultFavoritesComponent
import com.example.weather.presentation.favorites.FavoritesComponent
import com.example.weather.presentation.news.DefaultNewsComponent
import com.example.weather.presentation.news.NewsComponent
import com.example.weather.presentation.weather.DefaultWeatherComponent
import com.example.weather.presentation.weather.WeatherComponent
import com.example.weather.utils.AppDependencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {


    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, strategy = RootComponent.Model.serializer())
            ?: RootComponent.Model()
    )

    override val model: StateFlow<RootComponent.Model>
        get() = _model.asStateFlow()

    val dependencies = AppDependencies

    override val weatherComponent: WeatherComponent =
        DefaultWeatherComponent(componentContext = componentContext, dependencies = dependencies)
    override val newsComponent: NewsComponent =
        DefaultNewsComponent(componentContext = componentContext, dependencies = dependencies)
    override val favoritesComponent: FavoritesComponent =
        DefaultFavoritesComponent(componentContext = componentContext, dependencies = dependencies)

    init {
        stateKeeper.register(
            KEY,
            strategy = RootComponent.Model.serializer()
        ) { model.value }
    }

    override fun onWeatherTabClicked() {
        _model.update { it.copy(selectedTab = Tab.WEATHER) }
    }

    override fun onNewsTabClicked() {
        _model.update { it.copy(selectedTab = Tab.NEWS) }
    }

    override fun onFavoritesTabClicked() {
        _model.update { it.copy(selectedTab = Tab.FAVORITE) }
    }

    companion object {
        private const val KEY = "DefaultRootComponent"
    }
}
