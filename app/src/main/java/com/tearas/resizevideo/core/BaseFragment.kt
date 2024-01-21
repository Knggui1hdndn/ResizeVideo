package com.tearas.resizevideo.core

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
 import androidx.viewbinding.ViewBinding


abstract class BaseFragment<VB : ViewBinding>(layout: Int) : Fragment(layout) {
    private var _binding: VB? = null
    private var requestCode = 0
    open val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = getViewBinding(view)
        initData()
        initView()
        initObserver()
    }
    fun Fragment.setToolbar(
        toolbar: Toolbar,
        titleToolbar: String? = null,
        iconDrawable: Drawable,
        enabled: Boolean = true
    ) {
        val appCompatActivity = requireActivity() as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)

        appCompatActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(iconDrawable)
            setDisplayShowHomeEnabled(enabled)
            if (titleToolbar != null) {
                title = titleToolbar
            }
        }
    }
    open fun initObserver() {}

    abstract fun getViewBinding(view: View): VB
    open fun initView() {}
    open fun initData() {}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}