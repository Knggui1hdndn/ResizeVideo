package com.tearas.resizevideo.ffmpeg

class CommandConfiguration {
    private val stringBuilder = StringBuilder()

    fun appendCommand(command: String): CommandConfiguration {
        stringBuilder.append("${command.trim()} ")
        return this
    }

    fun getCommand() = stringBuilder.toString().trim()

    companion object {
        fun getInstance(): CommandConfiguration {
            return CommandConfiguration()
        }
    }
}