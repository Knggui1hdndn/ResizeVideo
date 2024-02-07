package com.tearas.resizevideo.ui.error

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import com.tearas.resizevideo.MainActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityShowErrorBinding
import com.tearas.resizevideo.utils.Utils.startToMainActivity

class ShowErrorActivity : BaseActivity<ActivityShowErrorBinding>() {
    override fun getViewBinding(): ActivityShowErrorBinding {
        return ActivityShowErrorBinding.inflate(layoutInflater)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initView() {
        setToolbar(
            binding.toolbar,
            getString(R.string.error),
            getDrawable(R.drawable.baseline_arrow_back_24)!!
        )
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               startToMainActivity()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startToMainActivity()
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}