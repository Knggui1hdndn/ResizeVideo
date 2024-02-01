package com.tearas.resizevideo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Environment
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.ui.result.ResultActivity
import com.tearas.resizevideo.utils.IntentUtils.passMediaInput
import com.tearas.resizevideo.utils.IntentUtils.passMediaOutput
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.TimeZone

object Utils {
    fun Context.startActivityResult(mediaInput: List<MediaInfo>, mediaOutPut: List<MediaInfo>) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.passMediaOutput(mediaOutPut)
        intent.passMediaInput(mediaInput)
        startActivity(intent)
    }

    @SuppressLint("SimpleDateFormat")
    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        .apply { timeZone = TimeZone.getTimeZone("GMP") }

    private val decimalFormat = DecimalFormat("#").apply {
        roundingMode = RoundingMode.CEILING
    }

    fun Float.formatToInt(): Int = decimalFormat.format(this).toInt()
    fun formatTime(time: Long): String = simpleDateFormat.format(time)

}