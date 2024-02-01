package com.tearas.resizevideo.ui.fast_forward

import android.view.View
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseDialogFragment
import com.tearas.resizevideo.databinding.FragmentDialogCustomSpeedBinding

class CustomSpeedDialogFragment(private val onValue: (Float) -> Unit) :
    BaseDialogFragment<FragmentDialogCustomSpeedBinding>(R.layout.fragment_dialog_custom_speed) {
    override fun getViewBinding(view: View): FragmentDialogCustomSpeedBinding {
        return FragmentDialogCustomSpeedBinding.bind(view)
    }

    override fun initView() {
        binding.apply {

            dismiss.setOnClickListener {
                dismiss()
            }
            save.setOnClickListener {
                val value = value.text.toString()
                if (value.isNotBlank() && value.toFloat() <= 6f) {
                    onValue.invoke(value.toFloat())
                    dismiss()
                }
            }
        }
    }
}