package com.tearas.resizevideo.model

import com.tearas.resizevideo.ffmpeg.MediaAction
import java.io.Serializable

data class OptionMedia(
    val dataOriginal: MediaInfos,
    var optionCompressType: OptionCompressType? = null,
    var bitrate: Long = 0,
    var frameRate: Int = 0,
    val fileSize: Long = 0,//Kbps
    val mimetype: String? = null,
    val mediaAction: MediaAction,
    val newResolution: Resolution = Resolution(),
    var startTime: Long = 0,
    var endTime: Long = 0,
    var speed: Float = 1.0f,
    var withAudio: Boolean = true,
    var isFastVideo: Boolean = true,
    val isPickMultiple: Boolean = dataOriginal.size > 0,
) : Serializable {
    override fun toString(): String {
        return super.toString()
    }
}