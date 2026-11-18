package com.example.core.di

import android.util.Log
import androidx.room.Room
import com.example.core.BuildConfig
import com.example.core.data.NewsRepositoryImpl
import com.example.core.data.source.local.room.NewsDatabase
import com.example.core.data.source.remote.network.ApiService
import com.example.core.domain.repository.NewsRepository
import com.example.core.domain.usecase.CheckFavouriteNewsUseCase
import com.example.core.domain.usecase.GetEplNewsUseCase
import com.example.core.domain.usecase.GetFavouriteNewsUseCase
import com.example.core.domain.usecase.SetFavouriteNewsUseCase
import com.example.core.security.KeystoreManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
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
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    single {
        val host1 = "www.thesportsdb.com"
        val host2 = "thesportsdb.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(host1, "sha256/FGN6RbsXGYeqobPbLXOHJQQy4QlD5lZxOsNidS2kBQk=")
            .add(host1, "sha256/kIdp6NNEd8wsugYyyIYFsi1ylMCED3hZbSR8ZFsa/A4=")
            .add(host1, "sha256/mEflZT5enoR1FuXLgYYGqnVEoZvmf9c2bVBpiOjYQ0c=")
            .add(host2, "sha256/FGN6RbsXGYeqobPbLXOHJQQy4QlD5lZxOsNidS2kBQk=")
            .add(host2, "sha256/kIdp6NNEd8wsugYyyIYFsi1ylMCED3hZbSR8ZFsa/A4=")
            .add(host2, "sha256/mEflZT5enoR1FuXLgYYGqnVEoZvmf9c2bVBpiOjYQ0c=")
            .build()

        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .certificatePinner(certificatePinner)
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
        val context = androidContext()
        SQLiteDatabase.loadLibs(context)
        val passphrase = KeystoreManager.getPassphrase(context)

        // Verify if pre-existing database file is readable with the current passphrase
        val dbFile = context.getDatabasePath("dicosport.db")
        if (dbFile.exists()) {
            try {
                val testDb = SQLiteDatabase.openDatabase(
                    dbFile.absolutePath,
                    String(passphrase, Charsets.UTF_8),
                    null,
                    SQLiteDatabase.OPEN_READWRITE
                )
                testDb.close()
            } catch (e: Exception) {
                Log.e("Dicosport", "Pre-existing database file is incompatible or unencrypted, resetting: ${e.message}")
                context.deleteDatabase("dicosport.db")
            }
        }

        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "dicosport.db"
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
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
