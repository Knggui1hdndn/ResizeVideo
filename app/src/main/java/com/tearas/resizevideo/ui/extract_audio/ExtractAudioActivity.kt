package com.tearas.resizevideo.ui.extract_audio

import android.annotation.SuppressLint
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityExtractAudioBinding
import com.tearas.resizevideo.ffmpeg.IProcessFFmpeg
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.ffmpeg.VideoCommandProcessor
import com.tearas.resizevideo.ffmpeg.VideoProcess
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.ui.DialogLoading
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.IntentUtils.getActionMedia
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.Utils.startActivityResult


class ExtractAudioActivity : BaseActivity<ActivityExtractAudioBinding>(), IProcessFFmpeg {
    override fun getViewBinding(): ActivityExtractAudioBinding {
        return ActivityExtractAudioBinding.inflate(layoutInflater)
    }

    private lateinit var media: OptionMedia
    private lateinit var videoInfo: MediaInfo
    private lateinit var viewModel: ExtractViewModel
    private lateinit var dialogLoading: DialogLoading

    override fun initData() {
        viewModel = ViewModelProvider(this@ExtractAudioActivity)[ExtractViewModel::class.java]
        media = intent.getOptionMedia()!!
        videoInfo = media.dataOriginal[0]
        binding.extractViewModel = viewModel
        binding.lifecycleOwner = this
        dialogLoading = DialogLoading()
    }

    override fun initObserver() {
        val handle = HandleMediaVideo(this@ExtractAudioActivity)
        val videoCommandProcessor = VideoCommandProcessor(
            this@ExtractAudioActivity,
            handle.getPathVideoCacheFolder(),
            handle.getPathVideoCacheFolder()
        )

        val videoProcess = VideoProcess.Builder(this@ExtractAudioActivity, media)

        viewModel.onClickContinues.observe(this@ExtractAudioActivity) { typeAudio ->
            dialogLoading.show(supportFragmentManager, DialogLoading::class.simpleName)
            val listInput = videoCommandProcessor.createCommandList(
                media.copy(
                    mimetype = typeAudio,
                    mediaAction = MediaAction.ExtractAudio
                )
            )
            videoProcess.compressAsync(
                 listInput,
                this@ExtractAudioActivity
            )
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initView() {
        binding.mediaInfo = videoInfo
        setToolbar(
            binding.toolbar,
            "Audio Options",
            getDrawable(R.drawable.baseline_arrow_back_24)!!
        )
        showNativeAds(binding.nativeAds) {}
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onFailure(error: String) {
        dialogLoading.dismiss()
        showMessage(error)
    }

    override fun onSuccess() {
        dialogLoading.dismiss()
    }

    override fun onFinish(mediaInfoOutput: List<MediaInfo>) {
        startActivityResult(media.dataOriginal, mediaInfoOutput)
    }

    override fun processElement(currentElement: Int, percentage: Int) {
    }


}