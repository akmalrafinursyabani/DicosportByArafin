package com.example.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.usecase.GetFavouriteNewsUseCase

class FavouriteViewModel(getFavouriteNewsUseCase: GetFavouriteNewsUseCase) : ViewModel() {
    val favouriteNews = getFavouriteNewsUseCase().asLiveData()
}
