package com.tearas.resizevideo.ui.extract_audio

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.knd.duantotnghiep.testsocket.core.BaseAdapter
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ItemAudioBinding
import com.tearas.resizevideo.databinding.ItemVideoCompressedBinding
import com.tearas.resizevideo.databinding.ItemVideoCompressionBinding
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.model.MediaInfo

class ShowAudioCompressedAdapter(private val context: Context ) :
    BaseAdapter<ItemAudioBinding, MediaInfo>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ItemAudioBinding {
        return ItemAudioBinding.inflate(inflater, parent, false)
    }

    override fun onBind(binding: ItemAudioBinding, item: MediaInfo) {
        binding.apply {
            size.text = Formatter.formatFileSize(context, item.size)
            time.text = item.time
            name.text = item.name
        }
    }
}