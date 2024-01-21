package com.tearas.resizevideo.ui.video_pickers

import android.content.Context
import android.os.Build
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.knd.duantotnghiep.testsocket.core.BaseAdapter
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ItemVideoBinding
import com.tearas.resizevideo.model.MediaInfo

interface IOnItemClickListener {
    fun onItemClick(mediaInfo: MediaInfo)
}

class VideoAdapter(private val context: Context, private val onItemClick: IOnItemClickListener) :
    BaseAdapter<ItemVideoBinding, MediaInfo>() {

    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemVideoBinding {
        return ItemVideoBinding.inflate(inflater, parent, false)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBind(binding: ItemVideoBinding, item: MediaInfo) {
        binding.apply {
            name.text = item.name
            size.text = Formatter.formatFileSize(context, item.size)
            time.text = item.time
            resolution.text = item.resolution.toString()
            applyItemSelectionStyle(item, name, overlay)

            Glide.with(context)
                .load("file:///" + item.path)
                .into(binding.thumbnail);

            mItem.setOnClickListener {
                item.isSelected = !item.isSelected
                applyItemSelectionStyle(item, name, overlay)
                onItemClick.onItemClick(item)
            }
        }
    }

    private fun applyItemSelectionStyle(item: MediaInfo, name: TextView, overlay: View) {
        name.setTextColor(
            if (item.isSelected) context.getColor(R.color.maintream)
            else context.getColor(R.color.black)
        )
        overlay.visibility = if (item.isSelected) View.VISIBLE else View.GONE
    }

}