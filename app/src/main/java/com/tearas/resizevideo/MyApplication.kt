package com.tearas.resizevideo;

import com.tearas.resizevideo.BuildConfig
import android.app.Application
import com.access.pro.application.ProApplication
import com.access.pro.config.AdsConfigModel
import com.google.android.gms.ads.MobileAds

class MyApplication : ProApplication() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        AdsConfigModel.GG_APP_OPEN = BuildConfig.GG_APP_OPEN
        AdsConfigModel.GG_BANNER = BuildConfig.GG_BANNER
        AdsConfigModel.GG_NATIVE = BuildConfig.GG_NATIVE
        AdsConfigModel.GG_FULL = BuildConfig.GG_FULL
        AdsConfigModel.GG_REWARDED = BuildConfig.GG_REWARDED
    }
}
