package com.tearas.resizevideo.ui.compressed

import android.view.View
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseDialogFragment
import com.tearas.resizevideo.databinding.FragmentDialogCustomSpeedBinding
import com.tearas.resizevideo.databinding.FragmentDialogDeleteBinding

class DeleteDialogFragment(private val onDelete: () -> Unit) :
    BaseDialogFragment<FragmentDialogDeleteBinding>(R.layout.fragment_dialog_delete) {
    override fun getViewBinding(view: View): FragmentDialogDeleteBinding {
        return FragmentDialogDeleteBinding.bind(view)
    }

    override fun initView() {
        binding.apply {

            dismiss.setOnClickListener {
                dismiss()
            }
            save.setOnClickListener {
                onDelete.invoke()
                dismiss()
            }
        }
    }
}
