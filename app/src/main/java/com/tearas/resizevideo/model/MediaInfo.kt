package com.tearas.resizevideo.model

import java.io.Serializable

data class Resolution(var width: Int = 0, var height: Int = 0) : Serializable {
    override fun toString(): String {
        return "$width x $height"
    }

    companion object {
        const val SMALL = "small"
        const val MEDIUM = "medium"
        const val LARGE = "large"
        const val ORIGIN = "origin"
        const val CUSTOM = "custom"
    }

    fun getRatio() = width * 1.0f / height


}

data class MediaInfo(
    val id: Long,
    val name: String,
    val path: String,
    var size: Long,
    val resolution: Resolution? = null,
    var time: String,
    var mime: String,
    var bitrate: Float,
    var isSelected: Boolean = false
) : Serializable

class MediaInfos : ArrayList<MediaInfo>() {}