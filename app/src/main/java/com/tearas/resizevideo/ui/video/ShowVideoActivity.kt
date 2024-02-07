package com.tearas.resizevideo.ui.video

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.MediaController
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityShowVideoBinding

class ShowVideoActivity : BaseActivity<ActivityShowVideoBinding>() {
    override fun getViewBinding(): ActivityShowVideoBinding {
        return ActivityShowVideoBinding.inflate(layoutInflater)
    }

    override fun initView() {
        binding.apply {
            setToolbar(
                binding.toolbar,
                getString(R.string.player),
                getDrawable(R.drawable.baseline_arrow_back_24)!!
            )
            val path = intent.getStringExtra("path")
            val mediaController = MediaController(this@ShowVideoActivity)
            mediaController.setAnchorView(video)
            video.setMediaController(mediaController)
            video.setVideoPath(path)
            video.start()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}