package com.example.favourite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.favourite.databinding.ActivityFavouriteBinding
import com.example.favourite.di.favouriteModule
import org.koin.core.context.loadKoinModules

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            loadKoinModules(favouriteModule)
        } catch (_: Exception) {
        }

        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, FavouriteFragment())
                .commit()
            }
    }
}
