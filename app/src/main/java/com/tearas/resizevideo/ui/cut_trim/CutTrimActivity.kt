package com.tearas.resizevideo.ui.cut_trim


import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import com.arthenica.mobileffmpeg.FFprobe
import com.google.android.material.button.MaterialButton
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ActivityCutTrimBinding
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.ui.select_compress.SelectCompressActivity
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.IntentUtils.passOptionMedia
import com.tearas.resizevideo.utils.Utils


class CutTrimActivity : BaseActivity<ActivityCutTrimBinding>() {
    override fun getViewBinding(): ActivityCutTrimBinding {
        return ActivityCutTrimBinding.inflate(layoutInflater)
    }

    private var idButtonClicked: Int = R.id.trim
    private var left = 0f
    private var right = 0f
    private var isPlaying: Boolean = false
    private lateinit var path: String
    private lateinit var optionMedia: OptionMedia
    private lateinit var runnable: Runnable
    override fun initData() {
        optionMedia = intent.getOptionMedia()!!
        val media = optionMedia.data[0]
        path = media.path
    }

    override fun initView() {

        binding.apply {
            setToolbar(
                binding.toolbar,
                "Edit Video",
                getDrawable(R.drawable.baseline_arrow_back_24)!!
            )

            setUpDefaultRangeSeekBar(path)
            setAdapterFrameVideo()
            video.setVideoPath(path)

            cut.setOnClickListener {
                handleButtonClick(R.anim.cut_transition, ::setUpUICutVideo, cut, trim)
            }

            trim.setOnClickListener {
                handleButtonClick(R.anim.trim_transition, ::setUpUITrimVideo, trim, cut)
            }

            frameLayout.setOnClickListener {
                isPlaying = !isPlaying
                isVisibilityPlaying()
                if (isPlaying) video.start() else video.pause()
            }
        }
    }

    override fun getMenu(): Int {
        return R.menu.menu_done
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.done) {
            binding.progressBar.visibility = View.VISIBLE
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, SelectCompressActivity::class.java)
                intent.passOptionMedia(createOptionMedia())
                startActivity(intent)
                binding.progressBar.visibility = View.GONE
            }, 1000)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createOptionMedia(): OptionMedia {
        return OptionMedia(
            data = optionMedia.data,
            mediaAction = if (idButtonClicked == R.id.trim) MediaAction.CutOrTrim.TrimVideo else MediaAction.CutOrTrim.CutVideo,
            endTime = (right / 1000).toLong(),
            startTime = (left / 1000).toLong()
        )
    }

    val handler = android.os.Handler(Looper.getMainLooper()!!)

    init {
        val runnable1 = Runnable {
            handler.post(runnable)
        }
        runnable = Runnable {
            if (idButtonClicked == R.id.trim) {
                binding.video.seekTo(left.toInt())
                handler.postDelayed(runnable1, (right - left).toLong())
            } else {
                val duration = FFprobe.getMediaInformation(path).duration.toFloat().toLong() * 1000
                var firstPartStartTime = 0L
                var firstPartEndTime = 0L
                var secondPartStartTime = 0L
                var secondPartEndTime = 0L

                if (left > 0) {
                    firstPartStartTime = 0L
                    firstPartEndTime = left.toLong()
                    secondPartStartTime = right.toLong()
                    secondPartEndTime = duration
                } else {
                    firstPartStartTime = right.toLong()
                    firstPartEndTime = duration
                    secondPartStartTime = duration
                    secondPartEndTime = duration
                }

                binding.video.seekTo(firstPartStartTime.toInt())
                handler.postDelayed({
                    binding.video.seekTo(secondPartStartTime.toInt())
                }, firstPartEndTime)

                handler.postDelayed({
                    handler.post(runnable)
                }, secondPartEndTime)
            }
        }
    }

    private fun handleButtonClick(
        transitionResId: Int,
        uiSetupFunction: () -> Unit,
        activeButton: MaterialButton,
        inactiveButton: MaterialButton,
    ) {
        if (idButtonClicked != activeButton.id) {
            val animation = AnimationUtils.loadAnimation(this@CutTrimActivity, transitionResId)
            binding.overlay.startAnimation(animation)
            uiSetupFunction()

            activeButton.setTextColor(Color.WHITE)
            activeButton.iconTint = ColorStateList.valueOf(Color.WHITE)

            inactiveButton.setTextColor(Color.GRAY)
            inactiveButton.iconTint = ColorStateList.valueOf(Color.GRAY)
            idButtonClicked = activeButton.id
            handler.removeCallbacksAndMessages(null)
            handler.post(runnable)
        }
    }

    private fun isVisibilityPlaying() {
        binding.play.visibility = if (isPlaying) View.GONE else View.VISIBLE
    }

    private fun setUpUICutVideo() {
        binding.includeCutTrim.apply {
            setRangeSeekBarColorTopBottom(getColor(R.color.maintream), Color.WHITE)
            setThumbAndColorProgress(false)
            setProgress()
            rangeProgress.setProgress(left, right)
        }
    }

    private fun setUpUITrimVideo() {
        binding.includeCutTrim.apply {
            setRangeSeekBarColorTopBottom(Color.WHITE, getColor(R.color.maintream))
            binding.includeCutTrim.rangeProgress.progressDefaultColor = getColor(R.color.maintream1)
            setThumbAndColorProgress(true)
            setProgress()
            rangeProgress.setProgress(left, right)
        }
    }

    private fun setRangeSeekBarColorTopBottom(
        progressDefaultColor: Int, progressColor: Int
    ) {
        binding.includeCutTrim.apply {
            rangeTop.setProgressColor(progressDefaultColor, progressColor)
            rangeBottom.setProgressColor(progressDefaultColor, progressColor)
        }
    }


    private fun setThumbAndColorProgress(isTrim: Boolean) {
        binding.includeCutTrim.apply {
            rangeProgress.leftSeekBar.thumbDrawableId =
                if (isTrim) R.drawable.custom_thumb else R.drawable.custom_thumb_1
            rangeProgress.rightSeekBar.thumbDrawableId =
                if (!isTrim) R.drawable.custom_thumb else R.drawable.custom_thumb_1
            if (isTrim) {
                rangeProgress.setProgressColor(getColor(R.color.maintream1), Color.TRANSPARENT)
            } else {
                rangeProgress.setProgressColor(Color.TRANSPARENT, getColor(R.color.maintream1))
            }
        }
    }


    private fun setProgress() {
        binding.includeCutTrim.apply {
            rangeBottom.setProgress(left, right)
            rangeTop.setProgress(left, right)
            rangeTime.setProgress(left, right)
        }
    }

    private fun setAdapterFrameVideo() {
        val frameRateVideoAdapter = FramRateVideoApdater(this@CutTrimActivity)
        frameRateVideoAdapter.submitData = FrameRateUtils.getFrames(path)
        binding.includeCutTrim.recyclerView.adapter = frameRateVideoAdapter
    }

    private fun setUpDefaultRangeSeekBar(path: String) {

        binding.includeCutTrim.apply {
            setUnEnabled(rangeBottom, rangeTop, rangeTime)
            right = FFprobe.getMediaInformation(path).duration.toFloat() * 1000
            setDefaultProgress(right)
            setUpUITrimVideo()

            rangeTime.rightSeekBar.setIndicatorText(
                Utils.formatTime(right.toLong())
            )

            rangeTime.leftSeekBar.setIndicatorText(
                "00:00:00"
            )

            rangeProgress.setOnRangeChangedListener(object : OnRangeChangedListener {
                override fun onRangeChanged(
                    view: RangeSeekBar?,
                    leftValue: Float,
                    rightValue: Float,
                    isFromUser: Boolean
                ) {


                    left = leftValue
                    right = rightValue

                    if (!isFromUser) {
                        handler.removeCallbacksAndMessages(null)
                        handler.post(runnable)
                    }

                    setProgress()

                    rangeTime.leftSeekBar.setIndicatorText(
                        Utils.formatTime(leftValue.toLong())
                    )

                    rangeTime.rightSeekBar.setIndicatorText(
                        Utils.formatTime(rightValue.toLong())
                    )

                }

                override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

                }

                override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

                }
            })
        }
    }

    private fun setUnEnabled(vararg range: RangeSeekBar) {
        range.forEach {
            it.isEnabled = false
        }
    }

    private fun setDefaultProgress(timeSecond: Float) {
        binding.includeCutTrim.apply {
            val array = arrayOf(rangeTop, rangeBottom, rangeProgress, rangeTime)
            array.forEach {
                it.setRange(0f, timeSecond);
                it.setProgress(0f, timeSecond)
            }
        }
    }
}