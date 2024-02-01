package com.tearas.resizevideo.ui.process

import android.annotation.SuppressLint
import android.content.Intent
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityProcessBinding
import com.tearas.resizevideo.ffmpeg.IProcessFFmpeg
import com.tearas.resizevideo.ffmpeg.VideoCommandProcessor
import com.tearas.resizevideo.ffmpeg.VideoProcess
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.ui.result.ResultActivity
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.IntentUtils.passMediaResults

class ProcessActivity : BaseActivity<ActivityProcessBinding>(), IProcessFFmpeg {

    private lateinit var optionMedia: OptionMedia
    override fun initData() {
        optionMedia = intent.getOptionMedia()!!
        val handle = HandleMediaVideo(this)
        val videoCommandProcessor = VideoCommandProcessor(handle.getPathFolderVideoMyApp(),handle.getPathFolderAudioMyApp())
        val listInput = videoCommandProcessor.createCommandList(optionMedia)
        VideoProcess.Builder(this).compressAsync(listInput, this)
    }

    override fun initView() {
        binding.apply {
            setupViewPager()
            cancel.setOnClickListener { VideoProcess.Builder.cancel() }
        }
    }

    private fun setupViewPager() {
        val processAdapter = ProcessAdapter(this)
        processAdapter.submitData = optionMedia.data
        binding.viewPager.adapter = processAdapter
        binding.circleIndicator.setViewPager(binding.viewPager)
    }


    override fun getViewBinding(): ActivityProcessBinding {
        return ActivityProcessBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun processElement(currentElement: Int, percentage: Int) {
        binding.apply {
            position.text = "$currentElement/${optionMedia.data.size}"
            percent.text = "$percentage%"
            progressBar.progress = percentage
        }
    }

    override fun onCurrentElement(position: Int) {
        binding.viewPager.setCurrentItem(position, true)
    }

    override fun onFinish(mediaInfoResults: List<MediaInfo>) {
      //  val intent = Intent(this, ResultActivity::class.java)
        //        intent.passMediaResults(mediaInfoResults)
        //        startActivity(intent)
        //        finish()
    }

    override fun onFailure(error: String) {
        showMessage(error)
    }
}
