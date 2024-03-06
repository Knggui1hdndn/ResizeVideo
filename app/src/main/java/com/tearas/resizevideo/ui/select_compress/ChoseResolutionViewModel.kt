package com.tearas.resizevideo.ui.select_compress

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tearas.resizevideo.R
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.utils.ResolutionUtils.calculateResolutionByRadio


class ChoseResolutionViewModel : ViewModel() {
    private val _selectedRadioIndex = MutableLiveData<Int>()
    val selectedRadioIndex: LiveData<Int> = _selectedRadioIndex

    private val _choseResolution = MutableLiveData<Resolution>()
    val choseResolution: LiveData<Resolution> = _choseResolution

    private lateinit var originalResolution: Resolution
    val resolutionOptions: ArrayList<Resolution> = ArrayList()


    fun postOriginalResolution(originalResolution: Resolution) {
        this.originalResolution = originalResolution
    }

    private fun unwrap(context: Context): Activity? {
        var context: Context? = context
        while (context !is Activity && context is ContextWrapper) {
            context = context.baseContext
        }
        return context as Activity?
    }

    fun setSelectedRadioIndex(view: View, index: Int) {
        _selectedRadioIndex.value = index
        if (view.id == R.id.custom) {
            CustomResolutionDialogFragment.getInstance()
                .show(unwrap(view.context) as FragmentActivity)
            return
        }
        _choseResolution.value = resolutionOptions[index]
    }

    fun setTextOptions() {
        val widthDefault = arrayOf(240, 360, 480, 640, 800)
        var count = 0
        widthDefault.forEachIndexed { _, width ->
            val replaceWidth = if (originalResolution.width < width) {
                count += 1
                originalResolution.width
            } else {
                width
            }
            val newResolution = if (originalResolution.width > 0 && originalResolution.height > 0) {
                Resolution().calculateResolutionByRadio(
                    originalResolution.getRatio(), replaceWidth, null
                )
            } else {
                Resolution(width, 0)
            }
            if (count < 2 || newResolution.height == 0) {
                resolutionOptions.add(newResolution)
            }
        }
        _choseResolution.value = resolutionOptions[0]
    }

    fun clearData() {
        resolutionOptions.clear()
    }
}