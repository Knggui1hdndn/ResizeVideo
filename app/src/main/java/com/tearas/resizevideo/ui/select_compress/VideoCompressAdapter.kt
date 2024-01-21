package com.tearas.resizevideo.ui.select_compress


import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.knd.duantotnghiep.testsocket.core.BaseAdapter
import com.tearas.resizevideo.databinding.ItemVideoCompressionBinding
import com.tearas.resizevideo.model.MediaInfo

class VideoCompressAdapter(private val context: Context) : BaseAdapter<ItemVideoCompressionBinding, MediaInfo>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemVideoCompressionBinding {
         return ItemVideoCompressionBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemVideoCompressionBinding, item: MediaInfo) {
       binding.apply {
           size.text= Formatter.formatFileSize(context,item.size)
           name.text=item.name
           time.text=item.time
           resolution.text=item.resolution.toString()
           Glide.with(context)
               .load("file:///" + item.path)
               .into(binding.thumbnail);
       }
    }
}