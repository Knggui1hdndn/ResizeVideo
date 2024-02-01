package com.tearas.resizevideo.ffmpeg

import com.tearas.resizevideo.model.MediaInfo
import java.lang.Error

interface IProcessFFmpeg {
    fun processElement(currentElement: Int, percentage: Int)
    fun onCurrentElement(position: Int) {}
    fun onSuccess() {}
    fun onFailure(error: String) {}
    fun onFinish(mediaInfoOutput: List<MediaInfo>) {}
    fun result(elementSuccess: Int, elementFailure: Int) {}
}