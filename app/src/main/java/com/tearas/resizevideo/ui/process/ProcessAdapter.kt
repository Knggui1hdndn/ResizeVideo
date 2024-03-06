package com.tearas.resizevideo.ui.process

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tearas.resizevideo.core.BaseAdapter
import com.tearas.resizevideo.databinding.ItemProcessVideoBinding
import com.tearas.resizevideo.model.MediaInfo

class ProcessAdapter(val context: Context) : BaseAdapter<ItemProcessVideoBinding, MediaInfo>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemProcessVideoBinding {
        return ItemProcessVideoBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemProcessVideoBinding, item: MediaInfo) {
        Glide.with(context)
            .load("file:///" + item.path)
            .into(binding.thumbnail);
    }
}