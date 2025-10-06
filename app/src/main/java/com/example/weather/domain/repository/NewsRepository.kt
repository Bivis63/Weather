package com.example.weather.domain.repository

import com.example.weather.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(): Flow<List<NewsItem>>
    fun getFavorite(): Flow<List<NewsItem>>
    suspend fun toggleRead(newsId: Int)
    suspend fun toggleLike(newsId: Int)
    suspend fun isNewsEmpty(): Boolean
    suspend fun initializeSampleData()
}