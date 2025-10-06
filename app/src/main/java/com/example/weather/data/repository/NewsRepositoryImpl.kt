package com.example.weather.data.repository

import com.example.weather.R
import com.example.weather.data.datasourse.RoomNewsDataSource
import com.example.weather.domain.model.NewsItem
import com.example.weather.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class NewsRepositoryImpl(
    private val roomDataSource: RoomNewsDataSource
) : NewsRepository {
    override fun getNews(): Flow<List<NewsItem>> {
        return roomDataSource.getNews()
    }

    override fun getFavorite(): Flow<List<NewsItem>> {
        return roomDataSource.getFavorites()
    }

    override suspend fun toggleRead(newsId: Int) {
        roomDataSource.toggleRead(newsId)
    }

    override suspend fun toggleLike(newsId: Int) {
        roomDataSource.toggleLike(newsId)
    }

    override suspend fun isNewsEmpty(): Boolean {
        return roomDataSource.getNewsCount() == 0
    }

    override suspend fun initializeSampleData() {
        val sampleNews = listOf(
            NewsItem(
                id = 1,
                title = "Культура",
                timeAgo = 10,
                articleType = "Статья",
                readingTime = 20,
                imageResId = R.drawable.new1,
                articleTitle = "Тайные улочки Барселоны",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis n"
            ),
            NewsItem(
                id = 2,
                title = "Технологии",
                timeAgo = 30,
                articleType = "Эссе",
                readingTime = 15,
                imageResId = R.drawable.new2,
                articleTitle = "Исторический контекст развития технологий",
                description = "Рассматриваем эволюцию технологий от первых компьютеров до современного искусственного интеллекта через призму исторических событий."
            ),
            NewsItem(
                id = 3,
                title = "Путешествия",
                timeAgo = 30,
                articleType = "Блог",
                readingTime = 35,
                imageResId = R.drawable.new3,
                articleTitle = "Поведаю о последнем путешествие",
                description = "Личный опыт путешествия по горным тропам Непала. Что нужно знать перед походом в Гималаи и как подготовиться к высотной болезни."
            )
        )
        roomDataSource.insertAllNews(sampleNews)
    }
}