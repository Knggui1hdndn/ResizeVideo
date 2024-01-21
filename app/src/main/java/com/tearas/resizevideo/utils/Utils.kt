package com.tearas.resizevideo.utils

import android.annotation.SuppressLint
import android.os.Environment
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.TimeZone

object Utils {
    val mimeAudio = arrayOf("mp3", "aac", "m4a", "wav", "flac", "ogg")

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        .apply { timeZone = TimeZone.getTimeZone("GMP") }

    private val decimalFormat = DecimalFormat("#").apply {
        roundingMode = RoundingMode.CEILING
    }

    fun Float.formatToInt(): Int = decimalFormat.format(this).toInt()
    fun Long.formatTime(): String = simpleDateFormat.format(this)

}