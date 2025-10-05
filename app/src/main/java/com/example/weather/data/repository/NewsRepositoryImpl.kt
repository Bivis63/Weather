package com.example.weather.data.repository

import com.example.weather.data.datasourse.LocalNewsDataSource
import com.example.weather.domain.model.NewsItem
import com.example.weather.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val localNewsDataSource: LocalNewsDataSource
) : NewsRepository {
    override fun getNews(): Flow<List<NewsItem>> {
       return localNewsDataSource.news
    }

    override fun getFavorite(): Flow<List<NewsItem>> {
       return localNewsDataSource.favorite
    }

    override suspend fun toggleRead(newsId: Int) {
        return localNewsDataSource.toggleRead(newsId)
    }

    override suspend fun toggleLike(newsId: Int) {
        return localNewsDataSource.toggleLike(newsId)
    }
}