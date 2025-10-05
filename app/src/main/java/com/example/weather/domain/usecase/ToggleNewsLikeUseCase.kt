package com.example.weather.domain.usecase

import com.example.weather.domain.repository.NewsRepository

class ToggleNewsLikeUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(newsId: Int){
        repository.toggleLike(newsId)
    }
}