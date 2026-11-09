package com.example.core.domain.repository

import com.example.core.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getEplNews(): Flow<Resource<List<NewsItem>>>
    fun getFavouriteNews(): Flow<List<NewsItem>>
    suspend fun setFavouriteNews(newsItem: NewsItem, state: Boolean)
    fun isFavouriteNews(id: String): Flow<Boolean>
}

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
