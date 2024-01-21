package com.tearas.resizevideo.ui.video_pickers

import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseFragment
import com.tearas.resizevideo.databinding.FragmentVideoPickerBinding
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.MyMenu.showPopUpMenuSort

class VideoPickerFragment :
    BaseFragment<FragmentVideoPickerBinding>(R.layout.fragment_video_picker) {

    override fun getViewBinding(view: View): FragmentVideoPickerBinding {
        return FragmentVideoPickerBinding.bind(view)
    }

    private lateinit var adapter: VideoAdapter
    private var size = 0
    private lateinit var handlerVideo: HandleMediaVideo
    private var orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC"
    private val viewModel: PickerViewModel by lazy {
        ViewModelProvider(requireActivity())[PickerViewModel::class.java]
    }

    override fun initData() {
        handlerVideo = HandleMediaVideo(requireActivity())
        adapter = VideoAdapter(requireActivity(), object : IOnItemClickListener {
            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            override fun onItemClick(mediaInfo: MediaInfo) {
                viewModel.insertVideo(mediaInfo)
            }
        })
    }

    override fun initObserver() {
        viewModel.videosLiveData.observe(this) { info ->
            val index = adapter.submitData.indexOfLast { it.id == info.id }
            index.takeIf { it != -1 }?.let { idx ->
                adapter.notifyItemChanged(idx, adapter.submitData[index]
                    .apply { isSelected = info.isSelected }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (handlerVideo.compareQuantity(size)) {
            submitData(orderBy)
        }
    }

    override fun initView() {
        binding.apply {

            sortBy.setOnClickListener {
                sortBy.showPopUpMenuSort {
                    orderBy = it
                    submitData(orderBy)
                }
            }

            videoAdapter.adapter = adapter

        }
    }

    private fun submitData(orderBy: String) {
        adapter.submitData = handlerVideo.getAllVideo(orderBy)
        binding.count.text = adapter.submitData.size.toString()
        size = adapter.submitData.size
    }
}