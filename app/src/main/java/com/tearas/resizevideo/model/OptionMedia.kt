package com.tearas.resizevideo.model

import com.tearas.resizevideo.ffmpeg.MediaAction
import java.io.Serializable

data class OptionMedia(
    val data: MediaInfos,
    val size: String? = null,
    val mimetype: String? = null,
    val resolution: Resolution? = null,
    val mediaAction: MediaAction
) : Serializable {
    override fun toString(): String {
        return super.toString()
    }
}