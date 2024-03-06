package com.tearas.resizevideo.ui.compressed

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import com.google.android.material.button.MaterialButton
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.core.DialogClickListener
import com.tearas.resizevideo.databinding.ActivityCompressedBinding
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.utils.DialogUtils
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.HandleSaveResult
import java.io.File

class CompressedActivity : BaseActivity<ActivityCompressedBinding>() {
    override fun getViewBinding(): ActivityCompressedBinding {
        return ActivityCompressedBinding.inflate(layoutInflater)
    }

    private lateinit var videoAdapter: VideoCompressedAdapter
    override fun initView() {
        binding.apply {
            setToolbar(
                toolbar,
                getString(R.string.compressed_videos),
                getDrawable(R.drawable.baseline_arrow_back_24)!!
            )

            showBannerAds(bannerAds)
            val videoHandler = HandleMediaVideo(this@CompressedActivity)

            videoAdapter = VideoCompressedAdapter(this@CompressedActivity) {
                showMoreBottomSheet(it)
            }
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                videoAdapter.submitData = videoHandler.getVideoSave()
                rcy.adapter = videoAdapter
                setStyleButtonClick(btnVideoSave)

                btnVideoSave.setOnClickListener {
                    setStyleButtonClick(btnVideoSave)
                    handler.post { videoAdapter.submitData = videoHandler.getVideoSave() }
                }

                btnVideoUnSave.setOnClickListener {
                    handler.post { videoAdapter.submitData = videoHandler.getVideoUnSave() }
                    setStyleButtonClick(btnVideoUnSave)
                }
            }
        }
    }

    private fun showMoreBottomSheet(it: MediaInfo) {
        MoreBottomSheetFragment(it) {
            DialogUtils.showDialogDelete(this, object : DialogClickListener {
                override fun onPositive() {
                    handleDeleteMedia(it)
                }

                override fun onNegative() {

                }
            })
        }.show(supportFragmentManager, MoreBottomSheetFragment::class.simpleName)
    }

    private fun handleDeleteMedia(mediaInfo: MediaInfo) {
        val videoSaveResult = HandleSaveResult(this@CompressedActivity)
        videoSaveResult.delete(mediaInfo.name)
        File(mediaInfo.path).delete()
        val position = videoAdapter.submitData.indexOf(mediaInfo)
        videoAdapter.submitData.remove(mediaInfo)
        videoAdapter.notifyItemRemoved(position)
    }

    private fun setStyleButtonClick(button: MaterialButton) {
        setStyleButtonClickDefault()
        button.apply {
            backgroundTintList = ColorStateList.valueOf(getColor(R.color.maintream))
            setTextColor(Color.WHITE)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setStyleButtonClickDefault() {
        binding.apply {
            btnVideoSave.backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
            btnVideoSave.setTextColor(Color.GRAY)
            btnVideoUnSave.backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
            btnVideoUnSave.setTextColor(Color.GRAY)
        }
    }
}