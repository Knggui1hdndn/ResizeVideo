package com.tearas.resizevideo.ui.select_compress

import android.view.View
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseBottomSheetFragment
import com.tearas.resizevideo.databinding.AdvanceCompressionBinding
import com.tearas.resizevideo.model.OptionCompressType

interface ChoseAdvance {
    fun onChoseAdvance(
        optionCompressType: OptionCompressType,
        bitRate: Long,
        frameRate: Int
    )
}

class AdvanceCompressionBottomSheetFragment(
    private var bitrate: Long,
    private val choseAdvance: ChoseAdvance
) :
    BaseBottomSheetFragment<AdvanceCompressionBinding>(R.layout.advance_compression) {
    private var listSize: Array<OptionCompressType> =
        arrayOf(
            OptionCompressType.Small,
            OptionCompressType.Medium,
            OptionCompressType.Large,
            OptionCompressType.Origin,
        )

    override fun getViewBinding(view: View): AdvanceCompressionBinding {
        return AdvanceCompressionBinding.bind(view)
    }

    private var size: OptionCompressType = OptionCompressType.Medium
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