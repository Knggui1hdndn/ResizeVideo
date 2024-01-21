package com.tearas.resizevideo.model

import java.io.Serializable

data class Resolution(val width: Int, val height: Int) : Serializable {
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

    fun calculateResolution(size: String): Resolution {
        return when (size) {
            SMALL -> this.copy(
                width = width - (width * 1 / 2),
                height = height - (height * 1 / 2)
            )

            MEDIUM -> this.copy(
                width = width - (width * 1 / 3),
                height = height - (height * 1 / 3)
            )

            LARGE -> this.copy(
                width = width - (width * 1 / 4),
                height = height - (height * 1 / 4)
            )

            ORIGIN -> this

            else -> Resolution(-1, -1)
        }
    }
}

data class MediaInfo(
    val id: Long,
    val name: String,
    val path: String,
    var size: Long,
    val resolution: Resolution? = null,
    var time: String,
    var mime: String,
    var isSelected: Boolean
) : Serializable {

}

class MediaInfos : ArrayList<MediaInfo>() {}