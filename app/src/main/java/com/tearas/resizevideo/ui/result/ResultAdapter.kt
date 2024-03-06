package com.tearas.resizevideo.ui.result


import android.content.Intent
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.jaygoo.widget.RangeSeekBar
import com.tearas.resizevideo.core.BaseAdapter
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.ItemRsBinding
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.ui.compare.CompareActivity
import com.tearas.resizevideo.ui.video.ShowVideoActivity
import com.tearas.resizevideo.utils.Utils.loadImage

class ResultAdapter(
    private val context: FragmentActivity,
    private val sizeBefore: Long = 0,
    private val value: (Int, String) -> Unit
) :
    BaseAdapter<ItemRsBinding, Pair<MediaInfo, MediaInfo>>() {
    override fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): ItemRsBinding {
        return ItemRsBinding.inflate(inflater, parent, false)
    }


    private lateinit var binding: ItemRsBinding
    override fun onBind(binding: ItemRsBinding, item: Pair<MediaInfo, MediaInfo>) {
        this.binding = binding
        val mediaBefore = item.first
        val mediaAfter = item.second
        val sizeBefore = if (this.sizeBefore != 0L) this.sizeBefore else mediaBefore.size
        if (mediaAfter.isVideo()) {
            context.loadImage(
                "file:///" + mediaBefore.path,
                binding.thumbnail
            )
            binding.icPlay.setImageResource(R.drawable.icon_play)
        } else {
            binding.icPlay.setImageResource(R.drawable.music)
        }

        binding.apply {
            setCommonData(mediaAfter)
            val maxProgress = Math.max(mediaAfter.size, sizeBefore.toLong())
            setSeekBarAndSize(maxProgress, seekCompress, sizeAfter, mediaAfter.size.toFloat())
            setSeekBarAndSize(maxProgress, seekOriginal, binding.sizeBefore, sizeBefore.toFloat())
            btnCompare.setOnClickListener {
                startCompareActivity(item)
            }
            thumbnail.setOnClickListener {
                val intent = Intent(context, ShowVideoActivity::class.java)
                intent.putExtra("path", mediaAfter.path)
                context.startActivity(intent)
            }
            btnRenam.setOnClickListener {
                RenameDialogFragment {
                    value(submitData.indexOf(item), it)
                }.show(context.supportFragmentManager, RenameDialogFragment::class.simpleName)
            }
        }
    }

    private fun setCommonData(mediaAfter: MediaInfo) {
        binding.apply {
            name.text = mediaAfter.name
            mLinearLayout.weightSum =
                if (mediaAfter.isVideo() && this@ResultAdapter.sizeBefore == 0L) {
                    btnCompare.visibility = View.VISIBLE
                    10f
                } else {
                    btnCompare.visibility = View.GONE
                    5f
                }
        }
    }

    private fun setSeekBarAndSize(
        maxProgress: Long,
        seekBar: RangeSeekBar, sizeTextView: TextView, size: Float
    ) {
        seekBar.isEnabled = false
        seekBar.setRange(0f, maxProgress.toFloat())
        seekBar.setProgress(size)
        sizeTextView.text = Formatter.formatFileSize(context, size.toLong())
    }

    private fun startCompareActivity(item: Pair<MediaInfo, MediaInfo>) {
        val intent = Intent(context, CompareActivity::class.java)
        intent.putExtra("Media", item)
        context.startActivity(intent)
    }
}