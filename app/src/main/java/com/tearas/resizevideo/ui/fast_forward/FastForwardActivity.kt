package com.tearas.resizevideo.ui.fast_forward

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.MediaController
import android.widget.TextView
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityFastForwardBinding
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.ui.select_compress.SelectCompressActivity
import com.tearas.resizevideo.utils.IntentUtils.getOptionMedia
import com.tearas.resizevideo.utils.IntentUtils.passOptionMedia
import java.util.logging.Handler
import kotlin.properties.Delegates


class FastForwardActivity : BaseActivity<ActivityFastForwardBinding>() {
    override fun getViewBinding(): ActivityFastForwardBinding {
        return ActivityFastForwardBinding.inflate(layoutInflater)
    }

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying: Boolean = false
    private var speed: Float = 1.0F
    private lateinit var options: Array<TextView>
    private val speeds: HashMap<Int, Float> = hashMapOf(
        R.id.txt1x to 1f,
        R.id.txt1_25x to 1.25f,
        R.id.txt1_5x to 1.5f,
        R.id.txt2x to 2f,
        R.id.txt2_5x to 2.5f,
        R.id.txt3x to 3f
    )

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun initView() {
        options = arrayOf(
            binding.txt1x,
            binding.txt125x,
            binding.txt15x,
            binding.txt2x,
            binding.txt25x,
            binding.txt3x,
            binding.customSpeed
        )
        binding.apply {
            setToolbar(
                binding.toolbar, "Fast Forward", getDrawable(R.drawable.baseline_arrow_back_24)!!
            )
            setUpVideo()
            setOnClickSpeed()

            frameLayout.setOnClickListener {
                isPlaying = !isPlaying
                isVisibilityPlaying()
                if (isPlaying) videoView.start() else videoView.pause()
            }
        }
    }

    private fun isVisibilityPlaying() {
        binding.play.visibility = if (isPlaying) View.GONE else View.VISIBLE
    }

    private fun setOnClickSpeed() {
        options.forEach { it ->
            it.setOnClickListener { it ->
                if (it.id == R.id.customSpeed) {
                    showSpeedDialog()
                } else {
                    setBackground(it.id)
                    val speed = speeds[it.id]
                    setSpeed(speed!!)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showSpeedDialog() {
        CustomSpeedDialogFragment {
            setSpeed(it)
            binding.customSpeed.text = "$it x"
        }.show(
            supportFragmentManager, CustomSpeedDialogFragment::class.simpleName
        )
    }

    private fun setSpeed(it: Float) {
        mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(it)
        speed =it
    }

    private fun setUpVideo() {
        binding.apply {
            val path = intent.getOptionMedia()!!.data[0].path
            videoView.setOnPreparedListener {
                mediaPlayer = it
            }
            videoView.setOnCompletionListener {
                videoView.seekTo(0)
            }
            videoView.setVideoPath(path)
            videoView.start()
        }
    }

    override fun getMenu(): Int {
        return R.menu.menu_done
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.done) {
            val intent = Intent(this, FastForwardOptionsActivity::class.java)
            intent.passOptionMedia(createOptionMedia())
            startActivity(intent)
            return true
        }
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setBackground(idChecked: Int) {
        options.forEach {
            if (it.id == idChecked) {
                it.background = getDrawable(R.drawable.radio_speed_checked)
                it.setTextColor(getColor(R.color.maintream))
            } else {
                it.background = getDrawable(R.drawable.radio_speed_unchecked)
                it.setTextColor(ColorStateList.valueOf(Color.GRAY))
            }
        }
    }

    private fun createOptionMedia(): OptionMedia {
        return intent.getOptionMedia()!!.copy(
            speed =this.speed
        )
    }
}