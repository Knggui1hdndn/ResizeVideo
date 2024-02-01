package com.tearas.resizevideo.utils;

import android.content.Intent;
import android.os.Build;
import com.google.gson.Gson;
import com.tearas.resizevideo.ffmpeg.MediaAction;
import com.tearas.resizevideo.model.MediaInfo
import com.tearas.resizevideo.model.MediaInfos
import com.tearas.resizevideo.model.OptionMedia;

object IntentUtils {

    private const val MEDIA = "media";
    private const val OPTION = "option_media";
    private const val ACTION = "action";
    private const val MEDIA_OUTPUT = "MEDIA_OUTPUT";
    private const val MEDIA_INPUT = "MEDIA_INPUT";

    fun Intent.getActionMedia() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(ACTION, MediaAction::class.java)
    } else {
        getSerializableExtra(ACTION) as MediaAction
    }

    fun Intent.getMediaOutput() =
        Gson().fromJson(getStringExtra(MEDIA_OUTPUT), MediaInfos::class.java)!!

    fun Intent.getMediaInput() =
        Gson().fromJson(getStringExtra(MEDIA_INPUT), MediaInfos::class.java)!!

    fun Intent.getOptionMedia() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(OPTION, OptionMedia::class.java)
    } else {
        getSerializableExtra(OPTION) as OptionMedia
    }

    fun Intent.passActionMedia(action: MediaAction) = putExtra(ACTION, action)

    fun Intent.passMediaOutput(mediaOutput: List<MediaInfo>) =
        putExtra(MEDIA_OUTPUT, Gson().toJson(mediaOutput))

    fun Intent.passMediaInput(mediaInput: List<MediaInfo>) =
        putExtra(MEDIA_INPUT, Gson().toJson(mediaInput))

    fun Intent.passOptionMedia(
        optionMedia: OptionMedia
    ) = putExtra(OPTION, optionMedia)
}
