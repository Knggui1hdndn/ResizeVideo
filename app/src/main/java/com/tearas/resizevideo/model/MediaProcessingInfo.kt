package com.tearas.resizevideo.model

import android.content.Context
import android.os.Environment
import android.util.Log
import com.tearas.resizevideo.ffmpeg.MediaAction
import com.tearas.resizevideo.utils.HandleMediaVideo

data class MediaProcessingInfo(
    val inputs: MediaInfo,
    var output: MediaInfo,
) {
    private lateinit var handleMediaVideo: HandleMediaVideo
    private fun commandCompressVideo(): String {
        Log.d("CommandCompressVideo","-i ${inputs.path} " +
                "-s ${output.resolution!!.height}x${output.resolution!!.width} -b:v  copy " +
                output.path)
        return "-i ${inputs.path} " +
                "-s ${output.resolution!!.height}x${output.resolution!!.width} -b:v  copy " +
                output.path
    }

    private fun commandCutCompress(): String {
        return "-i ${inputs.path} " +
                "-s ${output.resolution!!.height}x${output.resolution!!.width} -b:v  copy " +
                output.path
    }

    private fun commandExtractAudio(): String {
        return "-i ${inputs.path} " +
                "-s ${output.resolution!!.height}x${output.resolution!!.width} -b:v  copy " +
                output.path
    }

    private fun commandFastForward(): String {
        return "-i ${inputs.path} " +
                "-s ${output.resolution!!.height}x${output.resolution!!.width} -b:v  copy " +
                output.path
    }

    fun getCommandLine(context: Context, mediaAction: MediaAction): String {
        handleMediaVideo = HandleMediaVideo(context)
        return when (mediaAction) {
            is MediaAction.CompressVideo -> commandCompressVideo()
            is MediaAction.CutCompress -> commandCutCompress()
            is MediaAction.ExtractAudio -> commandExtractAudio()
            is MediaAction.FastForward -> commandFastForward()
        }
    }

}