package com.tearas.resizevideo.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class DarkModeManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("dark_mode_manager", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        editor.putInt("dark_mode", AppCompatDelegate.MODE_NIGHT_YES)
        editor.apply()
    }

    fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        editor.putInt("dark_mode", AppCompatDelegate.MODE_NIGHT_NO)
        editor.apply()
    }

    fun darkModeDefault() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        editor.putInt("dark_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        editor.apply()
    }

    fun setDarkMode() {
        val darkMode = sharedPreferences.getInt("dark_mode", -1)
        if (darkMode == AppCompatDelegate.MODE_NIGHT_NO) {
            disableDarkMode()
        }
        if (darkMode == AppCompatDelegate.MODE_NIGHT_YES) {
            enableDarkMode()
        }
        if (darkMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            darkModeDefault()
        }
    }
}
