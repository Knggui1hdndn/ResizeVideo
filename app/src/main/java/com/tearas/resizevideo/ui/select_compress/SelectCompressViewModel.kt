package com.tearas.resizevideo.ui.select_compress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tearas.resizevideo.model.Resolution

class SelectCompressViewModel : ViewModel() {
    private val _resolutionOriginLiveData = MutableLiveData<Resolution?>()
    val resolutionOriginLiveData: LiveData<Resolution?> = _resolutionOriginLiveData

    private val _resolutionLiveData = MutableLiveData<Resolution>()
    val resolutionLiveData: LiveData<Resolution> = _resolutionLiveData

    private val _resolutionCustomLiveData = MutableLiveData<Resolution>()
    val resolutionCustomLiveData: LiveData<Resolution> = _resolutionCustomLiveData

    private val _selectedIndex = MutableLiveData<Int>()
    val selectedIndex: LiveData<Int> = _selectedIndex

    fun postCustomResolution(resolution: Resolution) {
        _resolutionCustomLiveData.postValue(resolution)
    }

    fun postResolution(resolution: Resolution) {
        _resolutionLiveData.postValue(resolution)
    }

    fun postResolutionOrigin(resolution: Resolution?) {
        _resolutionOriginLiveData.postValue(resolution)
    }
}