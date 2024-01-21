package com.tearas.resizevideo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.MenuItem
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat

import com.knd.duantotnghiep.testsocket.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityMainBinding
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.ui.video_pickers.MainPickerActivity
import com.tearas.resizevideo.utils.IntentUtils.passActionMedia
import com.tearas.resizevideo.utils.READ_EXTERNAL_STORAGE
import com.tearas.resizevideo.utils.READ_MEDIA_VIDEO
import com.tearas.resizevideo.utils.RequestPermission
import com.tearas.resizevideo.utils.WRITE_EXTERNAL_STORAGE
import com.tearas.resizevideo.utils.checkPermission

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val requestPermission = RequestPermission(this)

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
private lateinit var mediaAction: MediaAction
    override fun initView() {
        binding.apply {
            setToolbar(
                toolbar,
                null,
                AppCompatResources.getDrawable(this@MainActivity, R.drawable.list)!!
                    .apply {
                        setTint(Color.WHITE)
                    },
                true
            )

            navigationView.setCheckedItem(R.id.home)
            navigationView.setNavigationItemSelectedListener { menuItem ->
                if (menuItem.isChecked) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers()
                true
            }

            compressVideo.setOnClickListener {
                startPickerVideo(MediaAction.CompressVideo)
            }

            cutCompress.setOnClickListener {
                startPickerVideo(MediaAction.CutCompress)
            }

            fastForward.setOnClickListener {
                startPickerVideo(MediaAction.FastForward)
            }

            extractAudio.setOnClickListener {
                startPickerVideo(MediaAction.ExtractAudio)
            }
        }
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
                permission = arrayOf(READ_MEDIA_VIDEO)
                checkPermission(READ_MEDIA_VIDEO)
            } else {
                checkPermission(permission)
            }
        return isPermissionGranted
    }

    @SuppressLint("ResourceType")
    private fun startPickerVideo(mediaAction: MediaAction) {
        this.mediaAction=mediaAction
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