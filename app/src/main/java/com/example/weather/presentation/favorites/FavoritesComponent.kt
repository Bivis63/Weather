package com.example.weather.presentation.favorites


import com.example.weather.domain.model.Category
import com.example.weather.domain.model.NewsItem
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface FavoritesComponent {
    val model: StateFlow<Model>

    fun onLikeClicked(news: NewsItem)
    fun onChipClicked(category: Category)

    @Serializable
    data class Model(
        val favorites: List<NewsItem> = emptyList(),
        val selectedChip: Category = Category.ALL,
        val filteredFavorites: List<NewsItem> = emptyList()
    )
}