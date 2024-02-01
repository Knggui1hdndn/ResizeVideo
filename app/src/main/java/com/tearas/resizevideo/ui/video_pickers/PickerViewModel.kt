package com.tearas.resizevideo.ui.video_pickers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.model.MediaInfos

class PickerViewModel : ViewModel() {
    val videosLiveData = MutableLiveData<MediaInfo>()
    val closeLiveData = MutableLiveData<Boolean>()
    val videos = MediaInfos()
    var sumSizeVideos = 0L
    fun size() = videos.size
    fun insertVideo(info: MediaInfo) {
         val index = videos.indexOfLast { info.id == it.id }
        if (info.isSelected) {
            videos.add(info)
        } else {
            if (index != -1) videos.removeAt(index)
        }
        sumSizeVideos = videos.sumOf { it.size }
        videosLiveData.postValue(info)
    }
}