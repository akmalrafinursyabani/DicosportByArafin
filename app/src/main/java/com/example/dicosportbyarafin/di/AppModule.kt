package com.example.dicosportbyarafin.di

import com.example.dicosportbyarafin.presentation.detail.DetailViewModel
import com.example.dicosportbyarafin.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { (newsId: String) -> DetailViewModel(get(), get(), get(), newsId) }
}
