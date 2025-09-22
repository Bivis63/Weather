package com.example.weather.data.datasourse

import com.example.weather.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class LocalNewsDataSource {
    private val _news = MutableStateFlow<List<NewsItem>>(emptyList())
    val news: Flow<List<NewsItem>> = _news.asStateFlow()

    val favorite: Flow<List<NewsItem>> = _news.map { newsList ->
        newsList.filter { it.isLike }
    }

    fun initializeNews(newList: List<NewsItem>) {
        _news.value = newList
    }

    fun toggleRead(newsId: Int) {
        val currentList = _news.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == newsId }
        if (index != -1){
            currentList[index] = currentList[index].copy(isRead = !currentList[index].isRead)
            _news.value = currentList
        }
    }
    fun toggleLike(newsId: Int) {
        val currentList =_news.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == newsId }
        if (index != -1){
            currentList[index] = currentList[index].copy(isLike = !currentList[index].isLike)
            _news.value = currentList
        }
    }
}