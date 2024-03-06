package com.tearas.resizevideo;

import com.access.pro.application.ProApplication
import com.access.pro.callBack.OnShowAdsOpenListener
import com.access.pro.config.AdsConfigModel
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.tearas.resizevideo.utils.DarkModeManager
import com.tearas.resizevideo.utils.DialogUtils


class MyApplication : ProApplication() {
    override fun onCreate() {
        super.onCreate()
        val testDeviceIds: List<String> = mutableListOf("0A616E7B2A72EB996BF974ACBF5855FC")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)
        DialogUtils.init(this)
        DarkModeManager(this).setDarkMode()
        MobileAds.initialize(this) {}
        AdsConfigModel.GG_APP_OPEN = BuildConfig.GG_APP_OPEN
        AdsConfigModel.GG_BANNER = BuildConfig.GG_BANNER
        AdsConfigModel.GG_NATIVE = BuildConfig.GG_NATIVE
        AdsConfigModel.GG_FULL = BuildConfig.GG_FULL
        AdsConfigModel.GG_REWARDED = BuildConfig.GG_REWARDED


    }
}
