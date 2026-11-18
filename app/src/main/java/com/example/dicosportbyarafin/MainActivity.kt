package com.example.dicosportbyarafin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.dicosportbyarafin.databinding.ActivityMainBinding
import com.example.favourite.FavouriteActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment),
            binding.drawerLayout,
        )

        // Setup ActionBarDrawerToggle & Explicit Hamburger Icon
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.app_name,
            R.string.app_name
        )
        toggle.drawerArrowDrawable.color =
            androidx.core.content.ContextCompat.getColor(this, R.color.toolbar_icon_tint)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.toolbar.setNavigationIcon(R.drawable.ic_drawer_hamburger)
        binding.toolbar.setNavigationOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(androidx.core.view.GravityCompat.START)
            }
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_favourite -> {
                    moveToFavouriteActivity()
                }

                R.id.nav_language -> {
                    showLanguageDialog()
                }

                R.id.nav_theme -> {
                    showThemeDialog()
                }

                R.id.nav_exit -> {
                    showExitConfirmationDialog()
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        return navHostFragment.navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "Bahasa Indonesia", "Español")
        val languageTags = arrayOf("en", "id", "es")

        AlertDialog.Builder(this)
            .setTitle(R.string.nav_language)
            .setItems(languages) { _, which ->
                val appLocale = LocaleListCompat.forLanguageTags(languageTags[which])
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun showThemeDialog() {
        val themes = arrayOf("Light", "Dark", "System Default")
        val modeValues = intArrayOf(
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )

        val sharedPrefs = getSharedPreferences("settings_pref", MODE_PRIVATE)
        val currentMode =
            sharedPrefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val checkedItem = when (currentMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> 0
            AppCompatDelegate.MODE_NIGHT_YES -> 1
            else -> 2
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.nav_theme)
            .setSingleChoiceItems(themes, checkedItem) { dialog, which ->
                val selectedMode = modeValues[which]
                sharedPrefs.edit { putInt("theme_mode", selectedMode) }
                AppCompatDelegate.setDefaultNightMode(selectedMode)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.exit_title)
            .setMessage(R.string.exit_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                finishAffinity()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun moveToFavouriteActivity() {
        val intent = Intent(this, FavouriteActivity::class.java)
        startActivity(intent)
    }
}
