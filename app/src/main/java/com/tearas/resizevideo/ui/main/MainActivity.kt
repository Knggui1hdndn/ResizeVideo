package com.tearas.resizevideo.ui.main

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.access.pro.callBack.OnShowAdsOpenListener
import com.google.android.material.button.MaterialButton
import com.tearas.resizevideo.R

import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityMainBinding
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.ui.OnTabSelectedListener
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


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val requestPermission = RequestPermission(this)

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var mediaAction: MediaAction

    override fun initView() {
        binding.apply {
            setupToolbar()
            setupAds()
            setupNavigation()
            setUpViewPager()
            mTab.setOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(position: Int) {
                    viewPager.setCurrentItem(position, true)
                }
            })
            mTab.attach(viewPager, "Home", "Files")
            mTab.setTabSelected(0)
        }
    }

    private fun setUpViewPager() {
        val mainViewPager = MainViewPager(this)
        binding.viewPager.adapter = mainViewPager
    }

    private fun setupToolbar() {
        val drawable = AppCompatResources.getDrawable(this@MainActivity, R.drawable.ic_menu)!!
        val originalBitmap = (drawable as BitmapDrawable).bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 24, 24, true)
        val scaledDrawable = BitmapDrawable(resources, scaledBitmap)

        setToolbar(binding.toolbar, "", scaledDrawable, true)
        binding.toolbar.navigationIcon = scaledDrawable

    }

    private fun setupAds() {
        binding.apply {
            showInterstitial(true) {}
        }
    }

    private fun setupNavigation() {
        binding.navigationView.apply {
            setCheckedItem(R.id.home)
            setNavigationItemSelectedListener { menuItem ->
                handleNavigationItemSelected(menuItem)
                true
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
                val url =
                    "http://play.google.com/store/apps/details?id=com.appsuite.video.size.reducer"
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

        proApplication.showOpenAds(this@MainActivity, object : OnShowAdsOpenListener {
            override fun onShowAdComplete() {
                showMessage("Loading")
            }
        })
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
                permission = arrayOf(READ_MEDIA_VIDEO, READ_MEDIA_IMAGES)
                checkPermission(READ_MEDIA_VIDEO) && checkPermission(READ_MEDIA_IMAGES)
            } else {
                checkPermission(permission)
            }
        return isPermissionGranted
    }

    @SuppressLint("ResourceType")
    fun startPickerVideo(mediaAction: MediaAction) {
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