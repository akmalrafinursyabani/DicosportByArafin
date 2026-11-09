package com.example.core.domain.usecase

import com.example.core.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class CheckFavouriteNewsUseCase(private val newsRepository: NewsRepository) {
    operator fun invoke(id: String): Flow<Boolean> = newsRepository.isFavouriteNews(id)
}
