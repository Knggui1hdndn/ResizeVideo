package com.tearas.resizevideo.ffmpeg

import com.tearas.resizevideo.model.OptionMedia
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.utils.ResolutionUtils.calculateResolution
import com.tearas.resizevideo.utils.ResolutionUtils.calculateResolutionByRadio
import com.tearas.resizevideo.utils.Utils

class VideoCommandProcessor(
    private val pathOutputFolderVideo: String,
    private val pathOutputFolderAudio: String
) {

    fun compressVideoCommand(resolution: Resolution, inputPath: String): String {
        return CommandConfiguration.getInstance()
            .appendCommand("-i $inputPath")
            .appendCommand("-s ${resolution.width}x${resolution.height}")
            .appendCommand("$pathOutputFolderVideo /VDI_TERAS_${System.currentTimeMillis()}.mp4")
            .getCommand()
    }

    fun trimVideoCommand(
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
            .appendCommand("$pathOutputFolderVideo /VDI_TERAS_${System.currentTimeMillis()}.mp4")
            .getCommand()

    }

    fun cutVideoCommand(
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
            .appendCommand("$pathOutputFolderVideo/VDI_TERAS_${System.currentTimeMillis()}.mp4")
            .getCommand();


    }

    fun extractAudioCommand(mime: String, inputPath: String): String {

        return CommandConfiguration.getInstance()
            .appendCommand("-i $inputPath")
            .appendCommand("-vn")
            .appendCommand("$pathOutputFolderAudio/VDI_TERAS_${System.currentTimeMillis()}.$mime")
            .getCommand()
    }

    //mp3 flac ogg
    fun fastForwardCommand(
        resolution: Resolution,
        withAudio: Boolean,
        speed: Float,
        inputPath: String
    ): String {
        val commandProcessor = CommandConfiguration.getInstance()
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
            .appendCommand(if (withAudio) "" else "")
            .appendCommand("$pathOutputFolderVideo/VDI_TERAS_${System.currentTimeMillis()}.mp4")
        return commandProcessor.getCommand()

    }

    fun createCommandList(optionMedia: OptionMedia): List<String> {
        var resolution = Resolution()
        return optionMedia.data.map { mediaItem ->
            if (optionMedia.mediaAction is MediaAction.CompressVideo ||
                optionMedia.mediaAction is MediaAction.CutOrTrim.CutVideo ||
                optionMedia.mediaAction is MediaAction.CutOrTrim.TrimVideo ||
                optionMedia.mediaAction is MediaAction.FastForward

            ) {
                val originalResolution = mediaItem.resolution!!
                resolution = if (optionMedia.size == Resolution.CUSTOM) {
                    originalResolution.calculateResolutionByRadio(
                        originalResolution.getRatio(), optionMedia.newResolution.width, null
                    )
                } else {
                    originalResolution.calculateResolution(optionMedia.size!!)
                }
            }

            when (optionMedia.mediaAction) {
                is MediaAction.CompressVideo -> compressVideoCommand(resolution, mediaItem.path)
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

                else -> ""
            }
        }
    }
}

