package com.example.favourite.di

import com.example.favourite.FavouriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favouriteModule = module {
    viewModel { FavouriteViewModel(get()) }
}
