package com.tearas.resizevideo.ffmpeg

 import com.tearas.resizevideo.model.MediaInfoResults

interface IProcessFFmpeg {
    fun processElement(currentElement: Int, percentage: Int)
    fun onCurrentElement(position: Int) {}
    fun onSuccess() {}
    fun onFailure() {}
    fun onFinish(mediaInfoResults: MediaInfoResults) {}
    fun result(elementSuccess: Int, elementFailure: Int) {}
}