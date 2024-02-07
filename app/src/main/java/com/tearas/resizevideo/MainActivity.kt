package com.tearas.resizevideo

import android.Manifest
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import com.access.pro.callBack.OnShowAdsOpenListener
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.ReturnCode

import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityMainBinding
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.ui.compressed.CompressedActivity
import com.tearas.resizevideo.ui.extract_audio.ShowAudioActivity
import com.tearas.resizevideo.ui.setting.SettingActivity
import com.tearas.resizevideo.ui.video_pickers.MainPickerActivity
import com.tearas.resizevideo.utils.IntentUtils.passActionMedia
import com.tearas.resizevideo.utils.READ_EXTERNAL_STORAGE
import com.tearas.resizevideo.utils.READ_MEDIA_VIDEO
import com.tearas.resizevideo.utils.RequestPermission
import com.tearas.resizevideo.utils.WRITE_EXTERNAL_STORAGE
import com.tearas.resizevideo.utils.checkPermission
import java.io.File
import java.util.LinkedList


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val requestPermission = RequestPermission(this)

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var mediaAction: MediaAction
    override fun initView() {
        binding.apply {

            val draw = AppCompatResources.getDrawable(this@MainActivity, R.drawable.list)!!
                .apply {
                    setTint(Color.WHITE)
                }

            setToolbar(toolbar, null, draw, true)
            showNativeAds(binding.container) {}
            showInterstitial(true) {}
            proApplication.showOpenAds(this@MainActivity, object : OnShowAdsOpenListener {
                override fun onShowAdComplete() {
                }
            })
            navigationView.setCheckedItem(R.id.home)
            navigationView.setNavigationItemSelectedListener { menuItem ->
                handleNavigationItemSelected(menuItem)
                true
            }

            compressVideo.setOnClickListener {
                startPickerVideo(MediaAction.CompressVideo)
            }

            cutCompress.setOnClickListener {
                startPickerVideo(MediaAction.CutOrTrim)
            }

            fastForward.setOnClickListener {
                startPickerVideo(MediaAction.FastForward)
            }

            extractAudio.setOnClickListener {
                startPickerVideo(MediaAction.ExtractAudio)
            }
            compressed.setOnClickListener {
                val intent = Intent(this@MainActivity, CompressedActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.enter_transition, R.anim.exit_transition)
            }
        }
    }

    private fun handleNavigationItemSelected(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.audio -> {
                startActivity(Intent(this@MainActivity, ShowAudioActivity::class.java))
            }

            R.id.compressVideos -> {
                startPickerVideo(MediaAction.CompressVideo)
            }

            R.id.settings -> startActivity(
                Intent(
                    this@MainActivity,
                    SettingActivity::class.java
                )
            )

            R.id.info -> {
                val email = "khangndph20612@fpt.edu.vn"

                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }

            R.id.share -> {
                val textToShare = "Nội dung bạn muốn chia sẻ"

                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, textToShare)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Share:"))
            }

            R.id.star -> {
                val url = "http://play.google.com/store/apps/details?id=com.appsuite.video.size.reducer"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
        if (menuItem.isChecked) menuItem.setChecked(false);
        else menuItem.setChecked(true);
        binding.drawerLayout.closeDrawers()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun initObserver() {
        requestPermission.observe(this) {
            if (it) {
                val intent = Intent(this, MainPickerActivity::class.java)
                intent.passActionMedia(mediaAction)
                startActivity(intent)
            } else {
                showMessage("Permission denied")
            }
        }
    }

    private var permission = arrayOf(
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )

    private fun requestPermissionMedia(): Boolean {
        val isPermissionGranted: Boolean =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission = arrayOf(READ_MEDIA_VIDEO,Manifest.permission.READ_MEDIA_IMAGES)
                checkPermission(READ_MEDIA_VIDEO) && checkPermission(READ_MEDIA_IMAGES)
            } else {
                checkPermission(permission)
            }
        return isPermissionGranted
    }

    @SuppressLint("ResourceType")
    private fun startPickerVideo(mediaAction: MediaAction) {
        this.mediaAction = mediaAction
        val checkPermission = requestPermissionMedia()
        if (checkPermission) {
            val intent = Intent(this, MainPickerActivity::class.java)
            intent.passActionMedia(mediaAction)
            startActivity(intent)
            overridePendingTransition(R.anim.enter_transition, R.anim.exit_transition)
        } else {
            requestPermission.launch(permission)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}