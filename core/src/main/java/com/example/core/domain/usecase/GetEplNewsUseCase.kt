package com.example.core.domain.usecase

import com.example.core.domain.model.NewsItem
import com.example.core.domain.repository.NewsRepository
import com.example.core.domain.repository.Resource
import kotlinx.coroutines.flow.Flow

class GetEplNewsUseCase(private val newsRepository: NewsRepository) {
    operator fun invoke(): Flow<Resource<List<NewsItem>>> = newsRepository.getEplNews()
}
