package com.example.weather.domain.usecase

import com.example.weather.domain.model.NewsItem
import com.example.weather.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(
    private val repository: NewsRepository
) {
     operator fun invoke(): Flow<List<NewsItem>>{
         return repository.getFavorite()
     }
}