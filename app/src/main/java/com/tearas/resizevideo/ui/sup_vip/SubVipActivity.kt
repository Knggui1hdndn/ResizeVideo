package com.tearas.resizevideo.ui.sup_vip

import android.content.Intent
import com.tearas.resizevideo.ui.main.MainActivity
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivitySubVipBinding

class SubVipActivity : BaseActivity<ActivitySubVipBinding>() {
    override fun getViewBinding() = ActivitySubVipBinding.inflate(layoutInflater)

    override fun initView() {
        binding.apply {

            btnUseFree.setOnClickListener {
                startActivity(Intent(this@SubVipActivity, MainActivity::class.java))
                finish()
            }

            close.setOnClickListener {
                startActivity(Intent(this@SubVipActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}