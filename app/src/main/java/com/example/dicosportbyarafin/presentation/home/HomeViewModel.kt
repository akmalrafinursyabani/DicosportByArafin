package com.example.dicosportbyarafin.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.NewsItem
import com.example.core.domain.repository.Resource
import com.example.core.domain.usecase.GetEplNewsUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(private val getEplNewsUseCase: GetEplNewsUseCase) : ViewModel() {

    private val _newsState = MutableLiveData<Resource<List<NewsItem>>>()
    val newsState: LiveData<Resource<List<NewsItem>>> = _newsState

    private val _searchQuery = MutableStateFlow("")
    private var allNewsList = listOf<NewsItem>()

    init {
        fetchNews()
        observeSearch()
    }

    fun fetchNews() {
        viewModelScope.launch {
            getEplNewsUseCase().collectLatest { resource ->
                if (resource is Resource.Success) {
                    allNewsList = resource.data
                    applySearch(_searchQuery.value)
                } else {
                    _newsState.value = resource
                }
            }
        }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    applySearch(query)
                }
        }
    }

    fun searchNews(query: String) {
        _searchQuery.value = query
    }

    private fun applySearch(query: String) {
        val current = allNewsList
        if (query.isBlank()) {
            _newsState.value = Resource.Success(current)
        } else {
            val filtered = current.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
            }
            _newsState.value = Resource.Success(filtered)
        }
    }
}
