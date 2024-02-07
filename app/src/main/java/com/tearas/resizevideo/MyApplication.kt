package com.tearas.resizevideo;

import android.content.res.Configuration
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.ActionMode
import com.access.pro.application.ProApplication
import com.access.pro.config.AdsConfigModel
import com.google.android.gms.ads.MobileAds
import com.tearas.resizevideo.utils.DarkModeManager

class MyApplication : ProApplication() {
    override fun onCreate() {
        super.onCreate()
        DarkModeManager(this).setDarkMode()
        MobileAds.initialize(this) {}
        AdsConfigModel.GG_APP_OPEN = BuildConfig.GG_APP_OPEN
        AdsConfigModel.GG_BANNER = BuildConfig.GG_BANNER
        AdsConfigModel.GG_NATIVE = BuildConfig.GG_NATIVE
        AdsConfigModel.GG_FULL = BuildConfig.GG_FULL
        AdsConfigModel.GG_REWARDED = BuildConfig.GG_REWARDED
    }
}
