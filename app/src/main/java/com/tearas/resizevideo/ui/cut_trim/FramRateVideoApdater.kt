package com.tearas.resizevideo.ui.cut_trim

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tearas.resizevideo.core.BaseAdapter
import com.tearas.resizevideo.databinding.ItemFramRateBinding

class FramRateVideoApdater(private val context: Context) :
    BaseAdapter<ItemFramRateBinding, Bitmap>() {
    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemFramRateBinding {
        return ItemFramRateBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemFramRateBinding, item: Bitmap) {
        binding.img.setImageBitmap(item)
    }
}