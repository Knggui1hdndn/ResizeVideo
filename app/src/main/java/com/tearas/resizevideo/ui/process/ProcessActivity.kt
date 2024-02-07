package com.tearas.resizevideo.ui.process

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedDispatcher
import com.arthenica.ffmpegkit.FFmpegKit
import com.tearas.resizevideo.MainActivity
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityProcessBinding
import com.tearas.resizevideo.ffmpeg.IProcessFFmpeg
import com.tearas.resizevideo.ffmpeg.VideoCommandProcessor
import com.tearas.resizevideo.ffmpeg.VideoProcess
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.model.OptionCompress
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.ui.error.ShowErrorActivity
import com.tearas.resizevideo.ui.result.ResultActivity
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.IntentUtils.passMediaInput
import com.tearas.resizevideo.utils.IntentUtils.passMediaOutput
import com.tearas.resizevideo.utils.Utils.startToMainActivity
import java.io.File
import java.io.FileInputStream

class ProcessActivity : BaseActivity<ActivityProcessBinding>(), IProcessFFmpeg {

    private lateinit var optionMedia: OptionMedia

    override fun onBackPressed() {
        super.onBackPressed()
        showBackDialog()
    }

    private fun showBackDialog() {
        BackDialogFragment {
            startToMainActivity()
            FFmpegKit.cancel()
        }.show(supportFragmentManager, BackDialogFragment::class.simpleName)
    }

    override fun initData() {
        optionMedia = intent.getOptionMedia()!!
        val handle = HandleMediaVideo(this)
        val videoCommandProcessor = VideoCommandProcessor(
            this,
            handle.getPathVideoCacheFolder(),
            handle.getPathVideoCacheFolder()
        )
        val listInput = videoCommandProcessor.createCommandList(optionMedia)
        if (optionMedia.optionCompress is OptionCompress.CustomFileSize) {
            VideoProcess.Builder(this).twoCompressAsync(listInput, this)
        } else {
            VideoProcess.Builder(this).compressAsync(listInput, this)
        }
    }

    override fun initView() {
        binding.apply {
            showNativeAds(nativeAds) {}
            setupViewPager()

            binding.cancel.setOnClickListener {
                showBackDialog()
            }
        }
    }

    private fun setupViewPager() {
        val processAdapter = ProcessAdapter(this)
        processAdapter.submitData = optionMedia.dataOriginal
        binding.viewPager.adapter = processAdapter
        binding.circleIndicator.setViewPager(binding.viewPager)
    }


    override fun getViewBinding(): ActivityProcessBinding {
        return ActivityProcessBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun processElement(currentElement: Int, percentage: Int) {
        runOnUiThread {
            binding.apply {
                position.text = "${currentElement + 1}/${optionMedia.dataOriginal.size}"
                percent.text = "$percentage%"
                progressBar.progress = percentage
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            showBackDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCurrentElement(position: Int) {
        binding.viewPager.setCurrentItem(position, true)
    }

    override fun onFinish(mediaInfoResults: List<MediaInfo>) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.passMediaOutput(mediaInfoResults)
        intent.passMediaInput(this.intent.getOptionMedia()!!.dataOriginal)
        startActivity(intent)
        finish()
    }

    override fun onFailure(error: String) {
        startActivity(Intent(this, ShowErrorActivity::class.java))
        finish()
    }
}
