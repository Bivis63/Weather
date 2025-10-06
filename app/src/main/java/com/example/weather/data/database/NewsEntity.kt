package com.example.weather.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val timeAgo: Int,
    val articleType: String,
    val readingTime: Int,
    val imageResId: Int,
    val articleTitle: String,
    val description: String,
    val isRead: Boolean = false,
    val isLike: Boolean = false
)

