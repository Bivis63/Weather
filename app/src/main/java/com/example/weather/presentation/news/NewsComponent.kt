package com.example.weather.presentation.news

import com.example.weather.domain.model.NewsItem
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface NewsComponent {
    val model: StateFlow<Model>

    fun onNewsCardClicked(news: NewsItem)
    fun onReadIconClicked(news: NewsItem)
    fun onLikeIconClicked(news: NewsItem)
    fun onDismissModal()
    fun onScrollPositionChanged(position: Int)

    @Serializable
    data class Model(
        val newsList: List<NewsItem> = emptyList(),
        val selectedNews: NewsItem? = null,
        val scrollPosition : Int = 0
    )
}