package com.tearas.resizevideo.ui.video_pickers

import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseFragment
import com.tearas.resizevideo.databinding.FragmentVideoFolderBinding
import com.tearas.resizevideo.model.FolderInfo
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.IntentUtils.getActionMedia
import com.tearas.resizevideo.utils.MyMenu.showPopUpMenuSort

class VideoFolderFragment :
    BaseFragment<FragmentVideoFolderBinding>(R.layout.fragment_video_folder) {
    override fun getViewBinding(view: View): FragmentVideoFolderBinding {
        return FragmentVideoFolderBinding.bind(view)
    }

    private var orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC"
    private lateinit var folderInfo: FolderInfo
    private lateinit var videos: List<MediaInfo>
    private val args: VideoFolderFragmentArgs by navArgs()
    private var size = 0
    private lateinit var handlerVideo: HandleMediaVideo
    private lateinit var adapter: VideoAdapter
    private val viewModel: PickerViewModel by lazy {
        ViewModelProvider(requireActivity())[PickerViewModel::class.java]
    }

    override fun initData() {
        folderInfo = args.FolderInfo
        videos = viewModel.videos
        handlerVideo = HandleMediaVideo(requireActivity())
        adapter = VideoAdapter(requireActivity(),
            requireActivity().intent.getActionMedia()!!,
            (requireActivity() as MainPickerActivity).proApplication.isSubVip,
            object : IOnItemClickListener {
                override fun onItemClick(mediaInfo: MediaInfo) {
                    viewModel.insertVideo(mediaInfo)
                }

                override fun showNotification(isPremium: Boolean, message: String) {

                }
            })
    }

    override fun initView() {
        binding.apply {

            val iconNavigation = AppCompatResources.getDrawable(
                requireActivity(),
                R.drawable.baseline_arrow_back_24
            )!!

            setToolbar(
                mToolbar,
                folderInfo.name,
                iconNavigation,
                true
            )

            videoAdapter.adapter = adapter


            sortBy.setOnClickListener {
                sortBy.showPopUpMenuSort {
                    orderBy = it
                    submitData(orderBy)
                }
            }
            binding.mToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun submitData(orderBy: String) {
        val newList = handlerVideo.getVideoByIdBucket(folderInfo.id, orderBy)
        adapter.submitData = ArrayList(newList.mapIndexed { index, videoInfo ->
            val isSelected = videos.find { it.id == videoInfo.id } != null
            videoInfo.copy(isSelected = isSelected)
        })

        binding.count.text = adapter.submitData.size.toString()
        size = adapter.submitData.size
    }

    override fun onResume() {
        super.onResume()
        if (handlerVideo.compareQuantity(size)) {
            submitData(orderBy)
        }
    }
}