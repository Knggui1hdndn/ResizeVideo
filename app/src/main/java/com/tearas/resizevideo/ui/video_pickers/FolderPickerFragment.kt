package com.tearas.resizevideo.ui.video_pickers

import android.provider.MediaStore
import android.view.View
import androidx.navigation.fragment.findNavController
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseFragment
import com.tearas.resizevideo.databinding.FragmentFolderPickerBinding
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.MyMenu.showPopUpMenuSort

class FolderPickerFragment :
    BaseFragment<FragmentFolderPickerBinding>(R.layout.fragment_folder_picker) {
    override fun getViewBinding(view: View): FragmentFolderPickerBinding {
        return FragmentFolderPickerBinding.bind(view)
    }

    private var orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC"
    private lateinit var folderAdapter: FolderAdapter
    private var size = 0
    private lateinit var handlerVideo: HandleMediaVideo
    override fun initData() {
        handlerVideo = HandleMediaVideo(requireActivity())
        folderAdapter = FolderAdapter()
        folderAdapter.submitData = handlerVideo.getFolderContainVideo()
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

            rcyFolder.adapter = folderAdapter
            folderAdapter.setOnClickListener {
                val videoFolder = MainPickerFragmentDirections.actionMainPickerFragmentToPickerFragment(it)
                findNavController().navigate(videoFolder)
            }
        }
    }

    private fun submitData(orderBy: String) {
        folderAdapter.submitData = handlerVideo.getFolderContainVideo(orderBy)
        binding.count.text = folderAdapter.submitData.size.toString()
        size = folderAdapter.submitData.size
    }
}