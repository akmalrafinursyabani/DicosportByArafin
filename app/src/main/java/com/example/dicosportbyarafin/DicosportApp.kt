package com.example.dicosportbyarafin

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.core.di.coreModules
import com.example.dicosportbyarafin.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class DicosportApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Restore saved theme preference
        val sharedPrefs = getSharedPreferences("settings_pref", MODE_PRIVATE)
        val savedMode = sharedPrefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(savedMode)

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@DicosportApp)
            modules(coreModules + appModule)
        }
    }
}
