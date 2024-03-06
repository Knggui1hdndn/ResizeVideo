package com.tearas.resizevideo.ui.compressed

import android.annotation.SuppressLint
import android.content.Intent
import android.text.format.Formatter
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseBottomSheetFragment
import com.tearas.resizevideo.databinding.FragmentBottomSheetBinding
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.ui.compare.CompareActivity
import com.tearas.resizevideo.ui.result.ResultActivity
import com.tearas.resizevideo.ui.video.ShowVideoActivity
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.HandleSaveResult

import com.tearas.resizevideo.utils.IntentUtils.passMediaInput
import com.tearas.resizevideo.utils.IntentUtils.passMediaOutput
import com.tearas.resizevideo.utils.Utils
import com.tearas.resizevideo.utils.Utils.share
import java.io.File
import java.io.FileInputStream

class MoreBottomSheetFragment(private val mediaInfo: MediaInfo, private val onDelete: () -> Unit) :
    BaseBottomSheetFragment<FragmentBottomSheetBinding>(R.layout.fragment_bottom_sheet) {
    override fun getViewBinding(view: View): FragmentBottomSheetBinding {
        return FragmentBottomSheetBinding.bind(view)
    }

    override fun initView() {
        setData()
        binding.apply {

            val handleMediaVideo = HandleMediaVideo(requireActivity())
            val handleSaveResult = HandleSaveResult(requireActivity())
            val pathInput = handleSaveResult.getPathInput(mediaInfo.path)
            val intent = Intent(requireActivity(), ResultActivity::class.java)
            val mediaInput = handleMediaVideo.getVideoByPath(pathInput.toString())
            save.setOnClickListener {
                val check = handleMediaVideo.saveFileToExternalStorage(
                    requireActivity(),
                    mediaInfo.isVideo(),
                    FileInputStream(mediaInfo.path),
                    mediaInfo.name
                )
                if (check == null) File(mediaInfo.path).delete()
            }
            play.setOnClickListener {
                val intent = Intent(requireActivity(), ShowVideoActivity::class.java)
                startActivity(intent.apply {
                    putExtra("path", mediaInfo.path)
                })
            }

            result.setOnClickListener {
                if (mediaInput != null) {
                    startActivity(intent.apply {
                        intent.passMediaOutput(listOf(mediaInfo))
                        intent.passMediaInput(listOf(mediaInput))
                    })
                } else {
                    (requireActivity() as CompressedActivity).showMessage("Video original not found")
                }
            }

            compare.setOnClickListener {
                if (mediaInput != null) startCompareActivity(Pair(mediaInput, mediaInfo)) else {
                    (requireActivity() as CompressedActivity).showMessage("Video original not found")
                }
            }
            share.setOnClickListener {
                requireContext().share(
                    mediaInfo.isVideo(),
                    Utils.getUri(requireContext(), mediaInfo.path)
                )
            }

            delete.setOnClickListener {
                dismissNow()
                onDelete()
            }
        }
    }

    private fun startCompareActivity(item: Pair<MediaInfo, MediaInfo>) {
        val intent = Intent(context, CompareActivity::class.java)
        intent.putExtra("Media", item)
        requireContext().startActivity(intent)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setData() {
        binding.apply {
            name.text = mediaInfo.name
            size.text = Formatter.formatFileSize(context, mediaInfo.size)
            time.text = mediaInfo.time
            if (mediaInfo.path.startsWith("content")) {
                Glide.with(this@MoreBottomSheetFragment)
                    .load(mediaInfo.path)
                    .error(requireContext().getDrawable(R.drawable.img))
                    .into(binding.thumbnail);
            } else {
                Glide.with(this@MoreBottomSheetFragment)
                    .load("file:///" + mediaInfo.path)
                    .error(requireContext().getDrawable(R.drawable.img))
                    .into(binding.thumbnail);
            }
        }
    }
}