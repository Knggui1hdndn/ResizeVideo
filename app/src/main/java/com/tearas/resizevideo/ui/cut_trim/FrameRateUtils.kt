package com.tearas.resizevideo.ui.cut_trim

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log

object FrameRateUtils {
    fun getFrames(path: String): ArrayList<Bitmap> {
        val frames = ArrayList<Bitmap>()
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(path)

            val duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                    ?: 0

            val frameInterval = duration * 1000 / 10.0

            for (i in 0 until 10) {
                // Get the frame at a specific time
                val timeUs = (i * frameInterval).toLong()
                val bitmap =
                    retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_NEXT_SYNC)

                bitmap?.let {
                    frames.add(it)
                }
            }
            Log.d("Error getting frames", frames.toString())

        } catch (e: Exception) {
            Log.d("Error getting frames", e.message.toString())
            e.printStackTrace()
        } finally {
            retriever.release()
        }

        return frames
    }

}