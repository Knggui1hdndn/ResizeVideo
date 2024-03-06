package com.tearas.resizevideo.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class OpenAppManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("open_app_manager", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun isOpenAppFirstTime() :Boolean{
        val isOpen = sharedPreferences.getBoolean("open_app_first_time", false)
        if (!isOpen) {
            editor.putBoolean("open_app_first_time", true)
            editor.apply()
        }
            return isOpen
    }
}
