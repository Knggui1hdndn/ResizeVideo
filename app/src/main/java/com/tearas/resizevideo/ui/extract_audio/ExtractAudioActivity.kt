package com.tearas.resizevideo.ui.extract_audio

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityExtractAudioBinding
import com.tearas.resizevideo.ffmpeg.IProcessFFmpeg
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.ffmpeg.VideoCommandProcessor
import com.tearas.resizevideo.ffmpeg.VideoProcess
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.ui.DialogLoading
import com.tearas.resizevideo.ui.fast_forward.FastForwardOptionsActivity
import com.tearas.resizevideo.ui.process.ProcessActivity
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.IntentUtils.passOptionMedia
import com.tearas.resizevideo.utils.Utils.startActivityResult

class ExtractAudioActivity : BaseActivity<ActivityExtractAudioBinding>(), IProcessFFmpeg {
    override fun getViewBinding(): ActivityExtractAudioBinding {
        return ActivityExtractAudioBinding.inflate(layoutInflater)
    }

    private lateinit var media: OptionMedia
    private lateinit var videoInfo: MediaInfo
    val mimeAudio = arrayOf("mp3", "aac", "m4a", "wav", "flac", "ogg")
    private var index = 0
    override fun initData() {
        media = intent.getOptionMedia()!!
        videoInfo = media.dataOriginal[0]
    }

    private lateinit var dialogLoading: DialogLoading
    override fun initView() {
        dialogLoading = DialogLoading()
        setDataVideoInfo()
        setToolbar(
            binding.toolbar, "Audio Options", getDrawable(R.drawable.baseline_arrow_back_24)!!
        )
        binding.apply {
            setOnChecked()
            showNativeAds(nativeAds) {}
            val handle = HandleMediaVideo(this@ExtractAudioActivity)
            val videoCommandProcessor = VideoCommandProcessor(
                this@ExtractAudioActivity,
                handle.getPathVideoCacheFolder(),
                handle.getPathVideoCacheFolder()
            )

            val videoProcess = VideoProcess.Builder(this@ExtractAudioActivity)
            continues.setOnClickListener {
                dialogLoading.show(supportFragmentManager, DialogLoading::class.simpleName)
                val listInput = videoCommandProcessor.createCommandList(
                    media.copy(
                        mimetype = mimeAudio[index],
                        mediaAction = MediaAction.ExtractAudio
                    )
                )
                videoProcess.compressAsync(listInput, this@ExtractAudioActivity)
            }
        }
    }

    private fun setOnChecked() {
        binding.apply {
            val listCheck = arrayListOf(rdi1, rdi2, rdi3, rdi4, rdi5, rdi6)
            listCheck.forEachIndexed { index, radioButton ->
                radioButton.setOnClickListener {
                    this@ExtractAudioActivity.index = index
                    listCheck.forEach {
                        it.isChecked = it.id == radioButton.id
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDataVideoInfo() {
        binding.apply {
            size.text = Formatter.formatFileSize(this@ExtractAudioActivity, videoInfo.size)
            name.text = videoInfo.name
            time.text = videoInfo.time
            resolution.text = videoInfo.resolution.toString()
            Glide.with(this@ExtractAudioActivity)
                .load("file:///" + videoInfo.path)
                .into(binding.thumbnail);
        }
    }

    override fun onFailure(error: String) {
        dialogLoading.dismiss()
        showMessage(error)
    }

    override fun onSuccess() {
        dialogLoading.dismiss()
    }

    override fun onFinish(mediaInfoOutput: List<MediaInfo>) {
        Log.d("ExtractAudioActivitysssssssss", "pdfafoakpdf")
        startActivityResult(media.dataOriginal, mediaInfoOutput)
    }

    override fun processElement(currentElement: Int, percentage: Int) {
        Log.d("ExtractAudioActivitysssssssss", "pdfafoakpdf" + percentage)
    }


}