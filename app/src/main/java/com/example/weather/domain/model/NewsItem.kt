package com.example.weather.domain.model

import androidx.compose.ui.graphics.painter.Painter
import kotlinx.serialization.Serializable

@Serializable
data class NewsItem(
    val id: Int,
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