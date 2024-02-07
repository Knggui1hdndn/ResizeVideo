package com.tearas.resizevideo.ui.result

import android.view.View
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseDialogFragment
import com.tearas.resizevideo.databinding.FragmentDialogCustomSpeedBinding
import com.tearas.resizevideo.databinding.FragmentDialogRenameBinding

class RenameDialogFragment(private val onValue: (String) -> Unit) :
    BaseDialogFragment<FragmentDialogRenameBinding>(R.layout.fragment_dialog_rename) {
    override fun getViewBinding(view: View): FragmentDialogRenameBinding {
        return FragmentDialogRenameBinding.bind(view)
    }

    override fun initView() {
        binding.apply {

            dismiss.setOnClickListener {
                dismiss()
            }
            save.setOnClickListener {
                val value = value.text.toString()
                if (value.isNotBlank()) {
                    onValue.invoke(value)
                    dismiss()
                }
            }
        }
    }
}