package com.example.weather.data.database

import com.example.weather.domain.model.NewsItem

object NewsMapper {

    fun toDomain(entity: NewsEntity): NewsItem {
        return NewsItem(
            id = entity.id,
            title = entity.title,
            timeAgo = entity.timeAgo,
            articleType = entity.articleType,
            readingTime = entity.readingTime,
            imageResId = entity.imageResId,
            articleTitle = entity.articleTitle,
            description = entity.description,
            isRead = entity.isRead,
            isLike = entity.isLike
        )
    }

    fun toEntity(newsItem: NewsItem): NewsEntity {
        return NewsEntity(
            id = newsItem.id,
            title = newsItem.title,
            timeAgo = newsItem.timeAgo,
            articleType = newsItem.articleType,
            readingTime = newsItem.readingTime,
            imageResId = newsItem.imageResId,
            articleTitle = newsItem.articleTitle,
            description = newsItem.description,
            isRead = newsItem.isRead,
            isLike = newsItem.isLike
        )
    }

    fun toDomainList(entities: List<NewsEntity>): List<NewsItem> {
        return entities.map { toDomain(it) }
    }

    fun toEntityList(newsItems: List<NewsItem>): List<NewsEntity> {
        return newsItems.map { toEntity(it) }
    }
}