package com.tearas.resizevideo.ui.video_pickers

import android.view.LayoutInflater
import android.view.ViewGroup
import com.knd.duantotnghiep.testsocket.core.BaseAdapter
import com.tearas.resizevideo.databinding.ItemFolderBinding
import com.tearas.resizevideo.model.FolderInfo

class FolderAdapter : BaseAdapter<ItemFolderBinding, FolderInfo>() {
    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemFolderBinding {
        return ItemFolderBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemFolderBinding, item: FolderInfo) {
        binding.apply {
            name.text = item.name
            count.text = item.count +" videos"
        }
    }

}