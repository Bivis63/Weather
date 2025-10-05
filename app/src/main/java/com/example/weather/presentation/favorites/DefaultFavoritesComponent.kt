package com.example.weather.presentation.favorites

import com.arkivanov.decompose.ComponentContext
import com.example.weather.domain.model.Category
import com.example.weather.domain.model.NewsItem
import com.example.weather.utils.AppDependencies
import com.example.weather.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class DefaultFavoritesComponent(
    componentContext: ComponentContext,
    dependencies: AppDependencies
) : FavoritesComponent, ComponentContext by componentContext {

    private val scope = componentScope()
    private val getFavoritesUseCase = dependencies.getFavoritesUseCase
    private val toggleNewsLikeUseCase = dependencies.toggleNewsLikeUseCase


    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, strategy = FavoritesComponent.Model.serializer())
            ?: FavoritesComponent.Model()
    )

    override val model: StateFlow<FavoritesComponent.Model>
        get() = _model.asStateFlow()

    init {
        stateKeeper.register(
            KEY,
            strategy = FavoritesComponent.Model.serializer()
        ) { model.value }

        getFavoritesUseCase()
            .onEach { favorites ->
                val currentModel = _model.value
                val filtered = if (currentModel.selectedChip == Category.ALL) {
                    favorites
                } else {
                    favorites.filter { it.title == currentModel.selectedChip.displayName }
                }
                _model.value = currentModel.copy(
                    favorites = favorites,
                    filteredFavorites = filtered
                )
            }
            .launchIn(scope)
    }

    override fun onLikeClicked(news: NewsItem) {
        scope.launch {
            toggleNewsLikeUseCase(news.id)
        }
    }

    override fun onChipClicked(category: Category) {
        val currentModel = _model.value
        val filtered = if (category == Category.ALL) {
            currentModel.favorites
        } else {
            currentModel.favorites.filter { it.title == category.displayName }
        }
        _model.value = currentModel.copy(
            selectedChip = category,
            filteredFavorites = filtered
        )
    }

    companion object {
        private const val KEY = "DefaultFavoritesComponent"
    }
}