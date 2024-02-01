package com.tearas.resizevideo.ffmpeg

class CommandConfiguration {
    private val stringBuilder = StringBuilder()

    fun appendCommand(command: String): CommandConfiguration {
        stringBuilder.append("$command ")
        return this
    }

    fun getCommand() = stringBuilder.toString()

    companion object {
        fun getInstance(): CommandConfiguration {
            return CommandConfiguration()
        }
    }
}