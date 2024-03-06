package com.tearas.resizevideo.ui.extract_audio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.ffmpeg.VideoProcess
import com.tearas.resizevideo.ui.DialogLoading


class ExtractViewModel : ViewModel() {
    private val _selectedRadioIndex = MutableLiveData<Int>()
    val selectedRadioIndex: LiveData<Int> = _selectedRadioIndex
    val onClickContinues = MutableLiveData<String>()

    private val mimeAudio = arrayOf("mp3", "aac", "m4a", "wav", "flac", "ogg")

    init {
        _selectedRadioIndex.value = 0
    }

    fun setSelectedRadioIndex(index: Int) {
        _selectedRadioIndex.value = index
    }

    fun setOnClickContinues() {
        onClickContinues.value = mimeAudio[selectedRadioIndex.value!!]
    }
}