package com.tearas.resizevideo.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.tearas.resizevideo.ui.main.MainActivity
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivitySplashBinding
import com.tearas.resizevideo.ui.intro.IntroActivity
import com.tearas.resizevideo.ui.sup_vip.SubVipActivity
import com.tearas.resizevideo.utils.OpenAppManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun getViewBinding() = ActivitySplashBinding.inflate(layoutInflater)

    override fun initView() {
        val openAppManager = OpenAppManager(this)

        val intentClass = when{
            !openAppManager.isOpenAppFirstTime()->IntroActivity::class.java
            proApplication.isSubVip-> MainActivity::class.java
            else ->SubVipActivity::class.java
        }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, intentClass))
            finish()
        }, 3000)
    }
}