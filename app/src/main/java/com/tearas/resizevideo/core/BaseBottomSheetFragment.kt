package com.tearas.resizevideo.core

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<VB : ViewBinding>(private val layout: Int) :
    BottomSheetDialogFragment(layout) {
    private var _binding: VB? = null
    val binding get() = _binding!!
    private lateinit var activity: FragmentActivity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity = requireActivity()
        _binding = getViewBinding(view)

        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val layoutParams = bottomSheet?.layoutParams
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            layoutParams?.height = WindowManager.LayoutParams.MATCH_PARENT
            bottomSheet?.layoutParams = layoutParams
        }

        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun getViewBinding(view: View): VB
    abstract fun initView()
    open fun initObserve() {}
}