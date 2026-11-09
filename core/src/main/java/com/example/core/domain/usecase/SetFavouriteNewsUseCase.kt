package com.example.core.domain.usecase

import com.example.core.domain.model.NewsItem
import com.example.core.domain.repository.NewsRepository

class SetFavouriteNewsUseCase(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(newsItem: NewsItem, state: Boolean) =
        newsRepository.setFavouriteNews(newsItem, state)
}
