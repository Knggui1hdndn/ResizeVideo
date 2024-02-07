package com.tearas.resizevideo.ui.result

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.arthenica.ffmpegkit.FFmpegKit
import com.tearas.resizevideo.BuildConfig
import com.tearas.resizevideo.MainActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseActivity
import com.tearas.resizevideo.databinding.ActivityResultBinding
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.HandleSaveResult
import com.tearas.resizevideo.utils.IntentUtils.getMediaInput
import com.tearas.resizevideo.utils.IntentUtils.getMediaOutput
import com.tearas.resizevideo.utils.RequestPermission
import com.tearas.resizevideo.utils.Utils
import com.tearas.resizevideo.utils.Utils.shareMultiple
import com.tearas.resizevideo.utils.Utils.startToMainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream


class ResultActivity : BaseActivity<ActivityResultBinding>() {
    override fun getViewBinding(): ActivityResultBinding {
        return ActivityResultBinding.inflate(layoutInflater)
    }

    private lateinit var dataRs: List<MediaInfo>
    private lateinit var handleSaveResult: HandleSaveResult
    private lateinit var handleMediaVideo: HandleMediaVideo
    override fun initData() {
        dataRs = intent.getMediaOutput()
        handleSaveResult = HandleSaveResult(this@ResultActivity)
        handleMediaVideo = HandleMediaVideo(this@ResultActivity)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initView() {
        setToolbar(
            binding.toolbar,
            getString(R.string.result),
            getDrawable(R.drawable.baseline_arrow_back_24)!!
        )
        binding.apply {
            setUpAdapterRs()
            showNativeAds(binding.container) {

            }

            bottomnavigation.setOnNavigationItemSelectedListener {
                handleNavigationItemSelected(it)
                true
            }
        }
    }

    private fun handleNavigationItemSelected(it: MenuItem) {
        when (it.itemId) {
            R.id.share -> share()

            R.id.replace -> showDialogReplace()

            R.id.save -> handleSaveMedia()
        }
    }

    private val manageAllFilesAccessPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleReplace()
        }


    private fun showDialogReplace() {
        ReplaceDialogFragment {
            val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    handleReplace()
                } else {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri)
                    manageAllFilesAccessPermissionLauncher.launch(intent)
                }
            } else {
                handleReplace()
            }
        }.show(
            supportFragmentManager,
            ReplaceDialogFragment::class.simpleName
        )
    }

    private fun handleReplace() {
        adapter.submitData.forEach {
            handleMediaVideo.replaceFiles(
                it.first.path,
                it.second.path
            )
        }
    }

    private fun share() {
        shareMultiple(
            dataRs[0].isVideo(),
            dataRs.map {
                Utils.getUri(this@ResultActivity, it.path)
            }.toList()
        )
    }

    private fun handleSaveMedia() {
        adapter.submitData.map { it.second }.forEachIndexed { index, mediaInfo ->
            val file = handleMediaVideo.saveFileToExternalStorage(
                this@ResultActivity,
                mediaInfo.isVideo(),
                FileInputStream(mediaInfo.path),
                mediaInfo.name
            )
            file?.let { savedFile ->
                handleSaveResult.getPathInput(mediaInfo.path)?.let { inputPath ->
                    handleSaveResult.save(inputPath, savedFile.path)
                    File(mediaInfo.path).delete()
                }
            }
        }
    }


    private lateinit var adapter: ResultAdapter
    private fun setUpAdapterRs() {
        adapter = ResultAdapter(this) { position, newName ->
            adapter.notifyItemChanged(position, adapter.submitData[position].apply {
                second.name = newName + "." + second.mime
            })
        }
        val mediaOutput = intent.getMediaOutput()
        val mediaInput = intent.getMediaInput()

        adapter.submitData = ArrayList(mediaInput.mapIndexed { index, mediaInfo ->
            Pair(mediaInfo, mediaOutput[index])
        }.toList())

        adapter.submitData.forEachIndexed { index, pair ->
            handleSaveResult.save(
                pair.first.path,
                pair.second.path,
                )
        }
        binding.rcyRs.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startToMainActivity()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}