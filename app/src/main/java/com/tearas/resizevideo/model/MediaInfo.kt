package com.tearas.resizevideo.model

import java.io.Serializable
import java.text.SimpleDateFormat

data class Resolution(var width: Int = 0, var height: Int = 0) : Serializable {
    override fun toString(): String {
        return if (height == 0) "$width" else "$width x $height"
    }


    fun getRatio() = width * 1.0f / height


}

data class MediaInfo(
    val id: Long,
    var name: String,
    val path: String,
    var size: Long,
    var resolution: Resolution? = null,
    var time: String,
    var mime: String,
    var bitrate: Long,
    var isSelected: Boolean = false
) : Serializable {
    fun isVideo() = mime == "mp4"

}

class MediaInfos : ArrayList<MediaInfo>() {}