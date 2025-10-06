package com.example.weather.data.datasourse

import com.example.weather.data.database.NewsDao
import com.example.weather.data.database.NewsMapper
import com.example.weather.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomNewsDataSource(
    private val newsDao: NewsDao
) {

    fun getNews(): Flow<List<NewsItem>> {
        return newsDao.getAllNews().map { entities ->
            NewsMapper.toDomainList(entities)
        }
    }

    fun getFavorites(): Flow<List<NewsItem>> {
        return newsDao.getFavoriteNews().map { entities ->
            NewsMapper.toDomainList(entities)
        }
    }

    suspend fun getNewsById(newsId: Int): NewsItem? {
        val entity = newsDao.getNewsById(newsId)
        return entity?.let { NewsMapper.toDomain(it) }
    }


    suspend fun insertAllNews(newsList: List<NewsItem>) {
        val entities = NewsMapper.toEntityList(newsList)
        newsDao.insertAllNews(entities)
    }

    suspend fun updateNews(newsItem: NewsItem) {
        val entity = NewsMapper.toEntity(newsItem)
        newsDao.updateNews(entity)
    }

    suspend fun toggleRead(newsId: Int) {
        val news = getNewsById(newsId)
        news?.let {
            val updatedNews = it.copy(isRead = !it.isRead)
            updateNews(updatedNews)
        }
    }

    suspend fun toggleLike(newsId: Int) {
        val news = getNewsById(newsId)
        news?.let {
            val updatedNews = it.copy(isLike = !it.isLike)
            updateNews(updatedNews)
        }
    }


    suspend fun getNewsCount(): Int {
        return newsDao.getNewsCount()
    }
}
