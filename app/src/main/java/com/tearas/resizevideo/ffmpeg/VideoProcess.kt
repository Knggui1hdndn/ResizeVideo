package com.tearas.resizevideo.ffmpeg

import android.content.Context
import android.util.Log
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import com.arthenica.mobileffmpeg.FFprobe
import com.arthenica.mobileffmpeg.MediaInformation
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.utils.HandleMediaVideo
import com.tearas.resizevideo.utils.Utils

import com.tearas.resizevideo.utils.Utils.formatToInt
import java.io.File
import java.util.logging.FileHandler


class VideoProcess {


    class Builder(val context: Context) {
        companion object {
            fun cancel() = FFmpeg.cancel()
        }

        init {
            Config.enableStatisticsCallback { newStatistics ->
                val process = (newStatistics.time * 1.0f / duration) * 100
                val percent = process.formatToInt()
                val min = minOf(100, percent)
                if (min == 100 && mediaInfoOutput.none { it.id == newStatistics.executionId }) {
                    val pathOutput = pathOutputs[currentIndex]
                    try {
                        val mediaInfo = FFprobe.getMediaInformation(pathOutput)

                        mediaInfoOutput.add(
                            createMediaInfo(
                                mediaInfo,
                                newStatistics.executionId,
                                pathOutput
                            )
                        )
                        iProcess.processElement(currentIndex, min)

                    } catch (e: Exception) {

                    }
                }
            }
        }

        private fun createMediaInfo(
            mediaInfo: MediaInformation,
            executionId: Long,
            pathOutput: String
        ): MediaInfo {
            return MediaInfo(
                executionId,
                mediaInfo.filename.substring(mediaInfo.filename.lastIndexOf("/") + 1),
                pathOutput,
                mediaInfo.size.toLong(),
                if (pathOutput.endsWith("mp4")) {
                    Resolution(
                        mediaInfo.mediaProperties.getInt("width"),
                        mediaInfo.mediaProperties.getInt("height")
                    )
                } else null,
                Utils.formatTime(duration.toLong() * 1000),
                pathOutput.substring(pathOutput.lastIndexOf(".") + 1),
                mediaInfo.bitrate.toFloat(),
                false
            )
        }


        private val commands = ArrayList<String>()
        private var pathOutputs = ArrayList<String>()
        private var pathInputs = ArrayList<String>()
        private val mediaInfoOutput = ArrayList<MediaInfo>()
        private val mediaInfoInput = ArrayList<MediaInfo>()
        private var countSuccess = 0
        private var countFailed = 0
        private var duration = 0
        private var currentIndex = 0
        private lateinit var iProcess: IProcessFFmpeg


        fun compressAsync(
            commands: List<String>,
            iProcess: IProcessFFmpeg
        ) {
            this.iProcess = iProcess
            this.commands.addAll(commands)
            commands.forEach {
                pathOutputs.add(it.substring(it.lastIndexOf(" ") + 1))
                pathInputs.add(it.split(" ")[1])
            }
            processFileSet(commands, 0)
        }


        private fun processFileSet(
            commands: List<String>,
            currentIndex: Int = 0,
        ) {
            this.currentIndex = currentIndex
            if (currentIndex >= commands.size) {
                resetToZero()
                return
            }
            try {
                duration = FFprobe.getMediaInformation(pathInputs[currentIndex]).duration.toFloat()
                    .formatToInt() * 1000
            } catch (e: Exception) {
                File(pathOutputs[currentIndex]).delete()
                iProcess.onFailure("Something went wrong please try again")
                return
            }
            FFmpeg.executeAsync(commands[currentIndex]) { _, returnCode ->
                if (returnCode == Config.RETURN_CODE_SUCCESS) {
                    iProcess.onSuccess()
                    countSuccess += 1
                } else {
                    File(pathOutputs[currentIndex]).delete()
                    iProcess.onFailure("Something went wrong please try again")

                    countFailed += 1
                }
                iProcess.result(countSuccess, countFailed)
                processFileSet(commands, currentIndex + 1)
                iProcess.onCurrentElement(currentIndex)
            }
        }

        private fun resetToZero() {
            iProcess.onFinish(mediaInfoOutput)
            countSuccess = 0
            countFailed = 0
            duration = 0
            currentIndex = 0
            commands.clear()
            mediaInfoOutput.clear()
        }
    }
}