package com.tearas.resizevideo.ui.video_pickers

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.format.Formatter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.knd.duantotnghiep.testsocket.core.BaseAdapter
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ItemVideoBinding
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.model.MediaInfo

interface IOnItemClickListener {
    fun onItemClick(mediaInfo: MediaInfo)
    fun showNotification(isPremium: Boolean = false, message: String)
}

class VideoAdapter(
    private val context: Context,
    private val mediaAction: MediaAction,
    private val isPremium: Boolean,
    private val onItemClick: IOnItemClickListener
) :
    BaseAdapter<ItemVideoBinding, MediaInfo>() {

    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemVideoBinding {
        return ItemVideoBinding.inflate(inflater, parent, false)
    }

    private fun countItemsSelected() = submitData.stream().filter { it.isSelected }.count().toInt()

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
                .error(context.getDrawable(R.drawable.img))
                .into(binding.thumbnail);

            mItem.setOnClickListener {

                if (!isPremium && countItemsSelected() == 3 && mediaAction is MediaAction.CompressVideo && !item.isSelected) {
                    onItemClick.showNotification(isPremium, message = "")
                    return@setOnClickListener
                }
                if (countItemsSelected() == 1 && mediaAction !is MediaAction.CompressVideo && !item.isSelected) {
                    onItemClick.showNotification(message = "")
                    return@setOnClickListener
                }
                item.isSelected = !item.isSelected
                onItemClick.onItemClick(item)
                applyItemSelectionStyle(item, name, overlay)
            }
        }
    }

    private fun applyItemSelectionStyle(item: MediaInfo, name: TextView, overlay: View) {
        if (item.isSelected) name.setTextColor(context.getColor(R.color.maintream)) else name.setTextAppearance(
            R.style.Theme_ResizeVideo
        )
        overlay.visibility = if (item.isSelected) View.VISIBLE else View.GONE
    }
}