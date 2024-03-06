package com.tearas.resizevideo.model

import java.io.Serializable

sealed class OptionCompressType : Serializable {
    data object Small : OptionCompressType()
    data object Medium : OptionCompressType()
    data object Large : OptionCompressType()
    data object Origin : OptionCompressType()
    data object Custom : OptionCompressType()
    data object CustomFileSize : OptionCompressType()
    data object AdvanceTypeOption : OptionCompressType()

}