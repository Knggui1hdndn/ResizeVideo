package com.tearas.resizevideo.ui.select_compress

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tearas.resizevideo.core.BaseAdapter
import com.tearas.resizevideo.databinding.ItemOptionCompressionBinding
import com.tearas.resizevideo.model.OptionCompression


class OptionsCompressionAdapter() :
    BaseAdapter<ItemOptionCompressionBinding, OptionCompression>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemOptionCompressionBinding {
        return ItemOptionCompressionBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemOptionCompressionBinding, item: OptionCompression) {
        binding.apply {
            val detail = item.detail
            title = item.title
            description = if (detail.isNotEmpty()) item.description + " - " + detail else item.description
            isSelected = item.isSelected
        }
    }

}