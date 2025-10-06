package com.example.weather.presentation.news

import com.arkivanov.decompose.ComponentContext
import com.example.weather.R
import com.example.weather.domain.model.NewsItem
import com.example.weather.domain.usecase.InitializeNewsDataUseCase
import com.example.weather.utils.AppDependencies
import com.example.weather.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DefaultNewsComponent(
    componentContext: ComponentContext,
    dependencies: AppDependencies
) : NewsComponent, ComponentContext by componentContext {


    private val scope = componentScope()

    private val getNewsUseCase = dependencies.getNewsUseCase
    private val toggleNewsReadUseCase = dependencies.toggleNewsReadUseCase
    private val toggleNewsLikeUseCase = dependencies.toggleNewsLikeUseCase
    private val initializeNewsDataUseCase = dependencies.initializeNewsDataUseCase

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY, strategy = NewsComponent.Model.serializer())
            ?: NewsComponent.Model()
    )

    override val model: StateFlow<NewsComponent.Model>
        get() = _model.asStateFlow()

    init {
        stateKeeper.register(
            KEY,
            strategy = NewsComponent.Model.serializer()
        ) { model.value }

        initializeNewsData()

        getNewsUseCase()
            .onEach { newsList ->
                _model.update { current ->
                    val updatedSelectedNews = current.selectedNews?.let { selected ->
                        newsList.find { it.id == selected.id }
                    }
                    current.copy(
                        newsList = newsList,
                        selectedNews = updatedSelectedNews
                    )
                }
            }
            .launchIn(scope)
    }

    private fun initializeNewsData() {
        scope.launch {
            initializeNewsDataUseCase()
        }
    }


    override fun onNewsCardClicked(news: NewsItem) {
        _model.update { it.copy(selectedNews = news) }
    }

    override fun onReadIconClicked(news: NewsItem) {
        scope.launch {
            toggleNewsReadUseCase(news.id)
        }
    }

    override fun onLikeIconClicked(news: NewsItem) {
        scope.launch {
            toggleNewsLikeUseCase(news.id)
        }
    }

    override fun onDismissModal() {
        _model.update { it.copy(selectedNews = null) }
    }

    override fun onScrollPositionChanged(position: Int) {
        _model.update { it.copy(scrollPosition = position) }
    }

    companion object {
        private const val KEY = "DefaultNewsComponent"
    }
}