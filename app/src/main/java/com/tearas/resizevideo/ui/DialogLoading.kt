package com.tearas.resizevideo.ui

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseDialogFragment
import com.tearas.resizevideo.databinding.FragmentDialogCustomSpeedBinding
import com.tearas.resizevideo.databinding.FragmentDialogLoadingBinding

class DialogLoading :
    BaseDialogFragment<FragmentDialogLoadingBinding>(R.layout.fragment_dialog_loading) {
    override fun getViewBinding(view: View): FragmentDialogLoadingBinding {
        return FragmentDialogLoadingBinding.bind(view)
    }

    companion object {
        private var dialogLoading: DialogLoading = DialogLoading()
        fun show(context: FragmentActivity) {
            dialogLoading.show(context.supportFragmentManager, DialogLoading::class.simpleName)
        }

        fun hide() = dialogLoading.dismiss()

    }

    override fun initView() {

    }
}