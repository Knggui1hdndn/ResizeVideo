package com.tearas.resizevideo.ui.process

import android.view.View
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseDialogFragment
import com.tearas.resizevideo.databinding.FragmentDialogBackBinding
import com.tearas.resizevideo.databinding.FragmentDialogCustomSpeedBinding
import com.tearas.resizevideo.databinding.FragmentDialogDeleteBinding

class BackDialogFragment(private val onCancelJob: () -> Unit) :
    BaseDialogFragment<FragmentDialogBackBinding>(R.layout.fragment_dialog_back) {
    override fun getViewBinding(view: View): FragmentDialogBackBinding {
        return FragmentDialogBackBinding.bind(view)
    }

    override fun initView() {
        binding.apply {

            dismiss.setOnClickListener {
                dismiss()
            }
            save.setOnClickListener {
                onCancelJob.invoke()
                dismiss()
            }
        }
    }
}
