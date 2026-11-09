package com.example.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.data.source.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAllNews(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news WHERE isFavourite = 1")
    fun getFavouriteNews(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsEntity>)

    @Update
    suspend fun updateNews(news: NewsEntity)

    @Query("UPDATE news SET isFavourite = :isFavourite WHERE id = :id")
    suspend fun updateFavouriteStatus(id: String, isFavourite: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM news WHERE id = :id AND isFavourite = 1)")
    fun isFavourite(id: String): Flow<Boolean>
}
