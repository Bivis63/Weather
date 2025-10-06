package com.example.weather.domain.usecase

import com.example.weather.domain.repository.NewsRepository

class InitializeNewsDataUseCase(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): Boolean {
        return if (newsRepository.isNewsEmpty()) {
            newsRepository.initializeSampleData()
            true
        } else {
            false
        }
    }
}