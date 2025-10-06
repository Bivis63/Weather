package com.example.weather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER BY id")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE isLike = 1 ORDER BY id")
    fun getFavoriteNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getNewsById(newsId: Int): NewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: NewsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNews(newsList: List<NewsEntity>)

    @Update
    suspend fun updateNews(news: NewsEntity)

    @Query("SELECT COUNT(*) FROM news")
    suspend fun getNewsCount(): Int
}