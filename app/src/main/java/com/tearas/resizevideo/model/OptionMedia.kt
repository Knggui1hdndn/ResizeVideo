package com.tearas.resizevideo.model

import android.hardware.camera2.CameraConstrainedHighSpeedCaptureSession
import com.tearas.resizevideo.ffmpeg.MediaAction
import java.io.Serializable

data class OptionMedia(
    val data: MediaInfos,
    val size: String? = null,
    val mimetype: String? = null,
    val mediaAction: MediaAction,
    val newResolution: Resolution = Resolution(),
    var startTime: Long = 0,
    var endTime: Long = 0,
    var speed: Float = 1.0f,
    var withAudio: Boolean = true,
    val isPickMultiple: Boolean = data.size > 0,
) : Serializable {
    override fun toString(): String {
        return super.toString()
    }
}