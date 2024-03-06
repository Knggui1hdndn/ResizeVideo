package com.tearas.resizevideo.ui.setting

import android.content.Intent
import android.view.MenuItem
import com.tearas.resizevideo.ui.main.MainActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivitySettingBinding
import com.tearas.resizevideo.utils.MyMenu.showPopupMenuTheme
import com.tearas.resizevideo.utils.Utils.startToMainActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun getViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun initView() {

        binding.apply {
            setToolbar(
                binding.toolbar,
                "Settings",
                getDrawable(R.drawable.baseline_arrow_back_24)!!
            )
          mLayoutTheme.setOnClickListener { it ->
              it.showPopupMenuTheme {
                  txtTheme.text = it
              }
          }
            showNativeAds(nativeAds){ }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startToMainActivity()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}