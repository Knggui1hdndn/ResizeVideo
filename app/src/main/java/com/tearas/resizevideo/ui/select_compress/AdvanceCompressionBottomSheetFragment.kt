package com.tearas.resizevideo.ui.select_compress

import android.annotation.SuppressLint
import android.content.Intent
import android.text.format.Formatter
import android.view.View
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseBottomSheetFragment
import com.tearas.resizevideo.databinding.AdvanceCompressionBinding
import com.tearas.resizevideo.databinding.FragmentBottomSheetBinding
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.model.OptionCompress
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

interface ChoseAdvance {
    fun onChoseAdvance(
        optionCompress: OptionCompress,
        bitRate: Long,
        frameRate: Int
    )
}

class AdvanceCompressionBottomSheetFragment(
    private var bitrate: Long,
    private val choseAdvance: ChoseAdvance
) :
    BaseBottomSheetFragment<AdvanceCompressionBinding>(R.layout.advance_compression) {
    private var listSize: Array<OptionCompress> =
        arrayOf(
            OptionCompress.Small,
            OptionCompress.Medium,
            OptionCompress.Large,
            OptionCompress.Origin,
        )

    override fun getViewBinding(view: View): AdvanceCompressionBinding {
        return AdvanceCompressionBinding.bind(view)
    }

    private var size: OptionCompress = OptionCompress.Medium
     override fun initView() {
        binding.apply {
            radioGroup3.setOnCheckedChangeListener { radioGroup, checkedId ->
                val index = radioGroup.indexOfChild(radioGroup.findViewById<View>(checkedId))
                size = listSize[index]
            }

            sliderBitrate.value = bitrate.toFloat()
            btnConfirm.setOnClickListener {
                choseAdvance.onChoseAdvance(
                    size,
                    sliderBitrate.value.toLong(),
                    siderFrameRate.value.toInt()
                )
                dismissNow()
            }
        }
    }
}