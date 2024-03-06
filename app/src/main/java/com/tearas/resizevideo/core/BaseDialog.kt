package com.tearas.resizevideo.core

import android.view.View
import com.tearas.resizevideo.R
import com.tearas.resizevideo.databinding.FragmentDialogBaseBinding
 import com.tearas.resizevideo.model.DialogModel

interface DialogClickListener {
    fun onPositive()
    fun onNegative()
}

class BaseDialog(
    private val dialogModel: DialogModel,
    private val dialogClickListener: DialogClickListener?
) :
    BaseDialogFragment<FragmentDialogBaseBinding>(R.layout.fragment_dialog_base) {


    override fun getViewBinding(view: View): FragmentDialogBaseBinding {
        return FragmentDialogBaseBinding.bind(view)
    }

    override fun initView() {
        binding.apply {
            dialogModel = this@BaseDialog.dialogModel
            dismiss.setOnClickListener {
                dialogClickListener?.onNegative()
                dismiss()
            }
            save.setOnClickListener {
                dialogClickListener?.onPositive()
                dismiss()
            }
        }
    }
}
