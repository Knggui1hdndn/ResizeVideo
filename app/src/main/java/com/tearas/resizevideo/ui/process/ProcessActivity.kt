package com.tearas.resizevideo.ui.process

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import com.arthenica.ffmpegkit.FFmpegKit
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.core.DialogClickListener
import com.tearas.resizevideo.databinding.ActivityProcessBinding
import com.tearas.resizevideo.ffmpeg.IProcessFFmpeg
import com.tearas.resizevideo.ffmpeg.VideoCommandProcessor
import com.tearas.resizevideo.ffmpeg.VideoProcess
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.model.OptionCompressType
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.ui.error.ShowErrorActivity
import com.tearas.resizevideo.ui.result.ResultActivity
import com.tearas.resizevideo.utils.DialogUtils
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.IntentUtils.getActionMedia
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.IntentUtils.passActionMedia
import com.tearas.resizevideo.utils.IntentUtils.passMediaInput
import com.tearas.resizevideo.utils.IntentUtils.passMediaOutput
import com.tearas.resizevideo.utils.Utils.startToMainActivity

class ProcessActivity : BaseActivity<ActivityProcessBinding>(), IProcessFFmpeg {

    private lateinit var optionMedia: OptionMedia

    override fun onBackPressed() {
        super.onBackPressed()
        showBackDialog()
    }

    private fun showBackDialog() {
        DialogUtils.showDialogBack(this, object : DialogClickListener {
            override fun onPositive() {
                startToMainActivity()
                FFmpegKit.cancel()
            }

            override fun onNegative() {

            }
        })
    }

    private lateinit var listInput: List<String>
    override fun initData() {
        optionMedia = intent.getOptionMedia()!!
        val handle = HandleMediaVideo(this)
        val videoCommandProcessor = VideoCommandProcessor(
            this,
            handle.getPathVideoCacheFolder(),
            handle.getPathVideoCacheFolder()
        )
        listInput = videoCommandProcessor.createCommandList(optionMedia)

        if (optionMedia.optionCompressType is OptionCompressType.CustomFileSize) {
            VideoProcess.Builder(this, optionMedia)
                .twoCompressAsync(listInput, this)
        } else {
            VideoProcess.Builder(this, optionMedia)
                .compressAsync(listInput, this)
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
                position.text = "${currentElement + 1}/${listInput.size}"
                percent.text = "$percentage%"
                progressBar.setProgress(percentage, true)
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
        intent.passActionMedia(this.intent.getActionMedia()!!)
        startActivity(intent)
        finish()
    }

    override fun onFailure(error: String) {
        startActivity(Intent(this, ShowErrorActivity::class.java))
        finish()
    }
}
