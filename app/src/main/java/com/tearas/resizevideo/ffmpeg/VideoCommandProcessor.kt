package com.tearas.resizevideo.ffmpeg

import android.content.Context
import android.util.Log
import com.arthenica.ffmpegkit.FFprobeKit
import com.tearas.resizevideo.model.MediaInfo

import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.model.OptionCompress
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.utils.ResolutionUtils.calculateResolution
import com.tearas.resizevideo.utils.ResolutionUtils.calculateResolutionByRadio
import com.tearas.resizevideo.utils.Utils

class VideoCommandProcessor(
    private val context: Context,
    private val pathOutputFolderVideo: String,
    private val pathOutputFolderAudio: String
) {
    private fun compressByFileSize(inputPath: String, targetBit: Long): String {
        val logFilePath = "${context.cacheDir}/ffmpeg2pass"
        return CommandConfiguration.getInstance()
            .appendCommand("-y")
            .appendCommand("-i")
            .appendCommand(inputPath)
//            .appendCommand("-c:v libx264")
            .appendCommand("-r 60")
            .appendCommand("-passlogfile")
            .appendCommand(logFilePath)
            .appendCommand("-preset medium")
            .appendCommand("-b:v $targetBit")
            .appendCommand("-b:a 127k")
            .appendCommand("-pass 1")
            .appendCommand("-c:a aac")
            .appendCommand("-b:a 128k")
            .appendCommand("-f mp4")
            .appendCommand("/dev/null")
            .appendCommand("-y -i")
            .appendCommand(inputPath)
//            .appendCommand("-c:v libx264")
            .appendCommand("-r 60")
            .appendCommand("-passlogfile")
            .appendCommand(logFilePath)
            .appendCommand("-preset medium")
            .appendCommand("-b:v  $targetBit")
            .appendCommand("-pass 2")
            .appendCommand("-c:a aac")
            .appendCommand("-b:a 127k")
            .appendCommand(getPathOutPut())
            .getCommand()
    }

    private fun compressVideoCommand(
        resolution: Resolution,
        inputPath: String,
        option: OptionCompress,
        fileSizeCompress: Long?,
        frameRate: Int,
        bitrate: Long
    ): String {
        return when (option) {
            is OptionCompress.CustomFileSize -> {
                val originalDuration =
                    FFprobeKit.getMediaInformation(inputPath).mediaInformation.duration.toFloat()
                        .toLong()
                val targetBitrate = (fileSizeCompress!! / originalDuration) - 128000
                val x = compressByFileSize(inputPath, targetBitrate)
                 x
            }

            is OptionCompress.Origin -> {
                CommandConfiguration.getInstance()
                    .appendCommand("-i $inputPath")
                    .appendCommand("-preset medium")
                    .appendCommand(getPathOutPut())
                    .getCommand()
            }

            is OptionCompress.AdvanceOption -> {
                CommandConfiguration.getInstance()
                    .appendCommand("-i $inputPath")
                    .appendCommand("-s ${resolution.width}x${resolution.height}")
                    .appendCommand("-r $frameRate")
                    .appendCommand("-b:v ${bitrate}k")
                    .appendCommand("-preset medium")
                    .appendCommand(getPathOutPut())
                    .getCommand()
            }

            else -> {
                CommandConfiguration.getInstance()
                    .appendCommand("-i $inputPath")
                    .appendCommand("-s ${resolution.width}x${resolution.height}")
                    .appendCommand(getPathOutPut())
                    .getCommand()
            }
        }
    }


    private fun getPathOutPut(mime: String = "mp4", isVideo: Boolean = true) = if (isVideo) {
        "$pathOutputFolderVideo/VDI_TERAS_${System.currentTimeMillis()}${(Math.random() * 1000).toInt()}.$mime"
    } else {
        "$pathOutputFolderAudio/AUDIO_TERAS_${System.currentTimeMillis()}${(Math.random() * 1000).toInt()}.$mime"
    }


    private fun trimVideoCommand(
        resolution: Resolution,
        inputPath: String,
        startTime: Long,
        endTime: Long
    ): String {
        return CommandConfiguration.getInstance()
            .appendCommand("-i $inputPath")
            .appendCommand("-s ${resolution.width}x${resolution.height}")
            .appendCommand("-ss ${Utils.formatTime(startTime * 1000)}")
            .appendCommand("-t ${Utils.formatTime((endTime - startTime) * 1000)}")
            .appendCommand("-c copy")
            .appendCommand(getPathOutPut())
            .getCommand()

    }

    private fun cutVideoCommand(
        resolution: Resolution,
        inputPath: String,
        startTime: Long,
        endTime: Long
    ): String {
        return CommandConfiguration.getInstance()
            .appendCommand("-i $inputPath")
            .appendCommand("-s ${resolution.width}x${resolution.height}")
            .appendCommand("-vf  \"select='not(between(t,$startTime,$endTime))',  setpts=N/FRAME_RATE/TB\"")
            .appendCommand(" -af \"aselect='not(between(t,$startTime,$endTime))', asetpts=N/SR/TB\"")
            .appendCommand(getPathOutPut())
            .getCommand();
    }

    private fun extractAudioCommand(mime: String, inputPath: String): String {

        return CommandConfiguration.getInstance()
            .appendCommand("-i $inputPath")
            .appendCommand("-vn")
            .appendCommand(getPathOutPut(mime, false))
            .getCommand()
    }

    //mp3 flac ogg
    private fun fastForwardCommand(
        resolution: Resolution,
        withAudio: Boolean,
        speed: Float,
        inputPath: String
    ): String {
        val commandProcessor = CommandConfiguration.getInstance()
        if (resolution.height % 2 != 0) resolution.height = resolution.height + 1
        commandProcessor
            .appendCommand("-i $inputPath")
            .appendCommand("-s ${resolution.width}x${resolution.height}")
            .appendCommand("-filter_complex")
        if (withAudio) {
            commandProcessor.appendCommand("[0:v]setpts=${1.0 / speed}*PTS[v];[0:a]atempo=$speed[a]")
                .appendCommand("-map [v] -map [a]")
        } else {
            commandProcessor.appendCommand("[0:v]setpts=${1.0 / speed}*PTS[v]")
                .appendCommand("-map [v]")
        }
             .appendCommand(getPathOutPut())
        return commandProcessor.getCommand()

    }

    fun createCommandList(optionMedia: OptionMedia): List<String> {
        var resolution: Resolution
        return optionMedia.dataOriginal.map { mediaItem ->
            resolution = calculateResolutionForCompressVideo(mediaItem, optionMedia)

            when (optionMedia.mediaAction) {
                is MediaAction.CompressVideo -> compressVideoCommand(
                    resolution,
                    mediaItem.path,
                    optionMedia.optionCompress!!,
                    optionMedia.fileSize,
                    optionMedia.frameRate,
                    optionMedia.bitrate
                )

                is MediaAction.CutOrTrim.CutVideo -> {
                    cutVideoCommand(
                        resolution,
                        mediaItem.path,
                        optionMedia.startTime,
                        optionMedia.endTime
                    )
                }

                is MediaAction.CutOrTrim.TrimVideo -> trimVideoCommand(
                    resolution,
                    mediaItem.path,
                    optionMedia.startTime,
                    optionMedia.endTime
                )

                is MediaAction.ExtractAudio -> extractAudioCommand(
                    optionMedia.mimetype!!,
                    mediaItem.path
                )

                is MediaAction.FastForward -> fastForwardCommand(
                    resolution,
                    optionMedia.withAudio,
                    optionMedia.speed,
                    mediaItem.path
                )

                else -> {
                    ""
                }
            }
        }
    }

    private fun calculateResolutionForCompressVideo(
        mediaItem: MediaInfo,
        optionMedia: OptionMedia
    ): Resolution {
        // Khởi tạo giá trị resolution ban đầu
        var resolution = Resolution()

        // Kiểm tra điều kiện và tính toán resolution mới dựa trên action và option
        if ((optionMedia.mediaAction is MediaAction.CompressVideo &&
                    optionMedia.optionCompress != OptionCompress.CustomFileSize) ||
            optionMedia.mediaAction is MediaAction.CutOrTrim.CutVideo ||
            optionMedia.mediaAction is MediaAction.CutOrTrim.TrimVideo ||
            optionMedia.mediaAction is MediaAction.FastForward

        ) {
            val originalResolution = mediaItem.resolution!!
            resolution = if (
                optionMedia.optionCompress == OptionCompress.Custom
            ) {
                originalResolution.calculateResolutionByRadio(
                    originalResolution.getRatio(), optionMedia.newResolution.width, null
                )
            } else {
                originalResolution.calculateResolution(optionMedia.optionCompress!!)
            }
        }
        return resolution
    }

}

