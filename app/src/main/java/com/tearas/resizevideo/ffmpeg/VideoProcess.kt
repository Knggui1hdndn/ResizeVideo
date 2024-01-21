package com.tearas.resizevideo.ffmpeg

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.tearas.resizevideo.model.MediaInfoResults
import com.tearas.resizevideo.model.MediaProcessingInfo
import com.tearas.resizevideo.utils.Utils
import com.tearas.resizevideo.utils.Utils.formatTime
import com.tearas.resizevideo.utils.Utils.formatToInt


class VideoProcess {
    companion object {
        fun cancel() = FFmpeg.cancel()
    }

    class Builder(private val context: Context, private val action: MediaAction) {
        init {
            Config.enableStatisticsCallback { newStatistics ->
                val process = (newStatistics.time * 1.0f / duration) * 100
                val percent = process.formatToInt()
                val min = minOf(100, percent)
                if (min == 100) {
                    inputs[currentIndex].output.apply {
                        size = newStatistics.size
                        time = newStatistics.time.toLong().formatTime()
                    }
                }
                iProcess.processElement(currentIndex, min)
            }
        }


        private val inputs = ArrayList<MediaProcessingInfo>()
        private val mediaInfoResults = MediaInfoResults()
        private var countSuccess = 0
        private var countFailed = 0
        private var duration = 0
        private var currentIndex = 0
        private lateinit var iProcess: IProcessFFmpeg


        fun compressAsync(
            inputsFFmqegs: List<MediaProcessingInfo>,
            iProcess: IProcessFFmpeg
        ) {
            this.iProcess = iProcess
            inputs.addAll(inputsFFmqegs)
            processFileSet(inputs, 0)
        }


        private fun processFileSet(
            fileSet: List<MediaProcessingInfo>,
            currentIndex: Int = 0,
        ) {
            this.currentIndex = currentIndex
            if (currentIndex >= fileSet.size) {
                iProcess.onFinish(mediaInfoResults)
                resetToZero()
                return
            }

            val currentFile = fileSet.elementAt(currentIndex)
            val command = currentFile.getCommandLine(context, action)

            FFmpeg.executeAsync(command) { _, returnCode ->
                if (returnCode == Config.RETURN_CODE_SUCCESS) {
                    iProcess.onSuccess()
                    countSuccess += 1
                } else {
                    iProcess.onFailure()
                    countFailed += 1
                }
                iProcess.result(countSuccess, countFailed)
                processFileSet(fileSet, currentIndex + 1)
                iProcess.onCurrentElement(currentIndex)
            }

            if (action != MediaAction.CutCompress) {
                val mp = MediaPlayer.create(context, Uri.parse("file://" + currentFile.inputs.path))
                try {
                    this.duration = mp.duration
                } finally {
                    mp.release()
                }
            }
        }

        private fun resetToZero() {
            countSuccess = 0
            countFailed = 0
            duration = 0
            currentIndex = 0
            inputs.clear()
            mediaInfoResults.clear()
        }
    }
}