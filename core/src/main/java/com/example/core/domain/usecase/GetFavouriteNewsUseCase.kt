package com.example.core.domain.usecase

import com.example.core.domain.model.NewsItem
import com.example.core.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetFavouriteNewsUseCase(private val newsRepository: NewsRepository) {
    operator fun invoke(): Flow<List<NewsItem>> = newsRepository.getFavouriteNews()
}
