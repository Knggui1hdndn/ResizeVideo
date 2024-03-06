package com.tearas.resizevideo.ui.compressed

import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.tearas.resizevideo.core.BaseAdapter
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ItemVideoCompressedBinding
import com.tearas.resizevideo.model.MediaInfo

class VideoCompressedAdapter(private val context: FragmentActivity,private val onMore:(MediaInfo)->Unit) :
    BaseAdapter<ItemVideoCompressedBinding, MediaInfo>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemVideoCompressedBinding {
        return ItemVideoCompressedBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemVideoCompressedBinding, item: MediaInfo) {
        binding.apply {
            size.text = Formatter.formatFileSize(context, item.size)
            time.text = item.time

            more.setOnClickListener {
                onMore(item)
            }

            if (item.path.startsWith("content")) {
                Glide.with(context)
                    .load(item.path)
                    .error(context.getDrawable(R.drawable.img))
                    .into(binding.thumbnail);
            } else {
                Glide.with(context)
                    .load("file:///" + item.path)
                    .error(context.getDrawable(R.drawable.img))
                    .into(binding.thumbnail);
            }
        }
    }
}