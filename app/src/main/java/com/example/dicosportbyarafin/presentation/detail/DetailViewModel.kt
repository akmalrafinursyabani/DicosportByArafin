package com.example.dicosportbyarafin.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.NewsItem
import com.example.core.domain.repository.Resource
import com.example.core.domain.usecase.CheckFavouriteNewsUseCase
import com.example.core.domain.usecase.GetEplNewsUseCase
import com.example.core.domain.usecase.SetFavouriteNewsUseCase
import com.example.dicosportbyarafin.presentation.model.NewsUiModel
import com.example.dicosportbyarafin.presentation.model.toDomainModel
import com.example.dicosportbyarafin.presentation.model.toUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DetailViewModel(
    private val setFavouriteNewsUseCase: SetFavouriteNewsUseCase,
    checkFavouriteNewsUseCase: CheckFavouriteNewsUseCase,
    getEplNewsUseCase: GetEplNewsUseCase,
    newsId: String
) : ViewModel() {

    private val _newsItem = MutableLiveData<NewsUiModel>()
    val newsItem: LiveData<NewsUiModel> = _newsItem

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    init {
        viewModelScope.launch {
            checkFavouriteNewsUseCase(newsId).collectLatest { fav ->
                _isFavourite.value = fav
            }
        }
    }

    val relatedNews: LiveData<List<NewsItem>> = getEplNewsUseCase().map { resource ->
        if (resource is Resource.Success) {
            val all = resource.data
            val current = all.find { it.id == newsId }
            if (current != null) {
                _newsItem.value = current.toUiModel()
            }
            all.filter { it.id != newsId }.take(6)
        } else {
            emptyList()
        }
    }.asLiveData()

    fun setFavourite(newsUiModel: NewsUiModel, state: Boolean) {
        _isFavourite.value = state
        val updated = newsUiModel.copy(isFavourite = state)
        _newsItem.value = updated
        viewModelScope.launch {
            setFavouriteNewsUseCase(updated.toDomainModel(), state)
        }
    }
}
