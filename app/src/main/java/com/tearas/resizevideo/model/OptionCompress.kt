package com.tearas.resizevideo.model

import java.io.Serializable

sealed class OptionCompress : Serializable {
    data object Small : OptionCompress()
    data object Medium : OptionCompress()
    data object Large : OptionCompress()
    data object Origin : OptionCompress()
    data object Custom : OptionCompress()
    data object CustomFileSize : OptionCompress()
    data object AdvanceOption : OptionCompress()

}