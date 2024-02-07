package com.tearas.resizevideo.ui.result

import android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.tearas.resizevideo.R
import com.tearas.resizevideo.core.BaseDialogFragment
import com.tearas.resizevideo.databinding.FragmentDialogRenameBinding
import com.tearas.resizevideo.databinding.FragmentDialogReplaceBinding
import com.tearas.resizevideo.utils.RequestPermission

class ReplaceDialogFragment( private val onReplace: () -> Unit) :
    BaseDialogFragment<FragmentDialogReplaceBinding>(R.layout.fragment_dialog_replace) {
    override fun getViewBinding(view: View): FragmentDialogReplaceBinding {
        return FragmentDialogReplaceBinding.bind(view)
    }

    override fun initView() {
        binding.apply {

            dismiss.setOnClickListener {
                dismiss()
            }
            save.setOnClickListener {

                    onReplace()

            }
        }
    }
}