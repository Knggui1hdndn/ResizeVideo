package com.tearas.resizevideo.ui.video_pickers

import android.annotation.SuppressLint
import android.content.Intent
import android.text.format.Formatter
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.knd.duantotnghiep.testsocket.core.BaseActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ActivityMainPickerBinding
import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.ui.select_compress.SelectCompressActivity
import com.tearas.resizevideo.utils.IntentUtils
import com.tearas.resizevideo.utils.IntentUtils.getActionMedia
import com.tearas.resizevideo.utils.IntentUtils.passOptionMedia


class MainPickerActivity : BaseActivity<ActivityMainPickerBinding>() {
    private val viewModel: PickerViewModel by lazy {
        ViewModelProvider(this)[PickerViewModel::class.java]
    }

    override fun getViewBinding(): ActivityMainPickerBinding {
        return ActivityMainPickerBinding.inflate(layoutInflater)
    }

    private lateinit var hostNav: NavHostFragment
    override fun initData() {
        hostNav =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    }

    override fun initView() {
        setUpNavigation()
        binding.apply {

            close.setOnClickListener {
                layoutSelected.visibility = View.GONE
                viewModel.videos.clear()
            }

            layoutSelected.setOnClickListener {
                if (viewModel.size() > 0) {
                    val intent = Intent(this@MainPickerActivity, SelectCompressActivity::class.java)
                    intent.passOptionMedia(createOptionMedia())
                    startActivity(intent)
                }
            }

        }
    }

    private fun createOptionMedia(): OptionMedia {
        return OptionMedia(
            viewModel.videos,
            null, null,
            if (viewModel.size() == 1) viewModel.videos[0].resolution!! else null,
            intent.getActionMedia()!!
        )
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        viewModel.videosLiveData.observe(this) { info ->
            val sizeListVideos = viewModel.size()
            binding.layoutSelected.visibility = if (sizeListVideos == 0) {
                View.GONE
            } else {
                View.VISIBLE
            }
            binding.size.text = Formatter.formatFileSize(this, viewModel.sumSizeVideos)
            binding.count.text = "$sizeListVideos selected"
        }
    }

    private fun setUpNavigation() {
        val navController: NavController = hostNav.navController
        AppBarConfiguration(navController.graph)
    }

    override fun onNavigateUp(): Boolean {
        return hostNav.navController.navigateUp() || super.onNavigateUp()
    }
}