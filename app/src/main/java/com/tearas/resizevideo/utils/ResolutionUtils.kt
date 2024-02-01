package com.tearas.resizevideo.utils

import android.util.Log
import com.tearas.resizevideo.model.Resolution

object ResolutionUtils {
    fun Resolution.calculateResolution(size: String): Resolution {
        return when (size) {
            Resolution.SMALL -> this.copy(
                width = width - (width * 1 / 2), height = height - (height * 1 / 2)
            )

            Resolution.MEDIUM -> this.copy(
                width = width - (width * 1 / 3), height = height - (height * 1 / 3)
            )

            Resolution.LARGE -> this.copy(
                width = width - (width * 1 / 4), height = height - (height * 1 / 4)
            )

            Resolution.ORIGIN -> this

            else -> this
        }
    }

    fun calculateResolutionByRadio(radio: Float, width: Int?, height: Int?): Long {
        if ((width == null && height == null) || (width != null && height != null)) {
            throw IllegalArgumentException("Exactly one of width or height should be null")
        }

        return when {
            width != null -> (width / radio).toLong()
            height != null -> (height * radio).toLong()
            else -> -1
        }
    }

    fun Resolution.calculateResolutionByRadio(radio: Float, width: Int?, height: Int?): Resolution {
        if ((width == null && height == null) || (width != null && height != null)) {
            throw IllegalArgumentException("Exactly one of width or height should be null")
        }
        when {
            width != null -> {
                this.width = width
                this.height = (width / radio).toInt()
            }

            height != null -> this.width = (height * radio).toInt()
            else -> 1
        }
        return this
    }
}