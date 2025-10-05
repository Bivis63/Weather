package com.example.weather.presentation.news

import com.arkivanov.decompose.ComponentContext
import com.example.weather.R
import com.example.weather.domain.model.NewsItem
import com.example.weather.utils.AppDependencies
import com.example.weather.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DefaultNewsComponent(
    componentContext: ComponentContext,
    dependencies: AppDependencies
) : NewsComponent, ComponentContext by componentContext {


    private val scope = componentScope()

    private val getNewsUseCase = dependencies.getNewsUseCase
    private val toggleNewsReadUseCase = dependencies.toggleNewsReadUseCase
    private val toggleNewsLikeUseCase = dependencies.toggleNewsLikeUseCase

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

        initializeNewsData(dependencies)

        getNewsUseCase()
            .onEach { newsList ->
                val currentModel = _model.value
                val updatedSelectedNews = currentModel.selectedNews?.let { selected ->
                    newsList.find { it.id == selected.id }
                }
                _model.value = currentModel.copy(
                    newsList = newsList,
                    selectedNews = updatedSelectedNews
                )
            }
            .launchIn(scope)
    }

    private fun initializeNewsData(dependencies: AppDependencies) {
        val sampleNews = listOf(
            NewsItem(
                id = 1,
                title = "Культура",
                timeAgo = 10,
                articleType = "Статья",
                readingTime = 20,
                imageResId = R.drawable.new1,
                articleTitle = "Тайные улочки Барселоны",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis n"
            ),
            NewsItem(
                id = 2,
                title = "Технологии",
                timeAgo = 30,
                articleType = "Эссе",
                readingTime = 15,
                imageResId = R.drawable.new2,
                articleTitle = "Исторический контекст развития технологий",
                description = "Рассматриваем эволюцию технологий от первых компьютеров до современного искусственного интеллекта через призму исторических событий."
            ),
            NewsItem(
                id = 3,
                title = "Путешествия",
                timeAgo = 30,
                articleType = "Блог",
                readingTime = 35,
                imageResId = R.drawable.new3,
                articleTitle = "Поведаю о последнем путешествие",
                description = "Личный опыт путешествия по горным тропам Непала. Что нужно знать перед походом в Гималаи и как подготовиться к высотной болезни."
            )
        )

        dependencies.newsDataSource.initializeNews(sampleNews)
    }

    override fun onNewsCardClicked(news: NewsItem) {
        _model.value = _model.value.copy(selectedNews = news)
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
        _model.value = _model.value.copy(selectedNews = null)
    }

    override fun onScrollPositionChanged(position: Int) {
        _model.value = _model.value.copy(scrollPosition = position)
    }

    companion object {
        private const val KEY = "DefaultNewsComponent"
    }
}