package com.example.core.di

import androidx.room.Room
import com.example.core.data.NewsRepositoryImpl
import com.example.core.data.source.local.room.NewsDatabase
import com.example.core.data.source.remote.network.ApiService
import com.example.core.domain.repository.NewsRepository
import com.example.core.domain.usecase.CheckFavouriteNewsUseCase
import com.example.core.domain.usecase.GetEplNewsUseCase
import com.example.core.domain.usecase.GetFavouriteNewsUseCase
import com.example.core.domain.usecase.SetFavouriteNewsUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://www.thesportsdb.com/api/v1/json/123/")
            .client(get<OkHttpClient>())
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .build()
            .create(ApiService::class.java)
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NewsDatabase::class.java,
            "dicosport.db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<NewsDatabase>().newsDao() }
}

val repositoryModule = module {
    single<NewsRepository> { NewsRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    factory { GetEplNewsUseCase(get()) }
    factory { GetFavouriteNewsUseCase(get()) }
    factory { SetFavouriteNewsUseCase(get()) }
    factory { CheckFavouriteNewsUseCase(get()) }
}

val coreModules = listOf(networkModule, databaseModule, repositoryModule, useCaseModule)
