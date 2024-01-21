package com.tearas.resizevideo.ffmpeg

import java.io.Serializable

sealed class MediaAction(val action: String) : Serializable {
    data object CompressVideo : MediaAction("Compress video")
    data object CutCompress : MediaAction("Cut video")
    data object FastForward : MediaAction("Fast Forward")
    data object ExtractAudio : MediaAction("Extract Audio")
}