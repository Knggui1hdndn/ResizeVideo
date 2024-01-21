package com.tearas.resizevideo.utils;

import android.content.Intent;
import android.os.Build;
import com.google.gson.Gson;
import com.tearas.resizevideo.ffmpeg.MediaAction;
import com.tearas.resizevideo.model.MediaInfoResults;
import com.tearas.resizevideo.model.MediaInfos;
import com.tearas.resizevideo.model.OptionMedia;

object IntentUtils {

    private const val MEDIA = "media";
    private const val OPTION = "option_media";
    private const val ACTION = "action";
    private const val MEDIARS = "mediaRs";

    fun Intent.getActionMedia() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(ACTION, MediaAction::class.java)
    } else {
        getSerializableExtra(ACTION) as MediaAction
    }

    fun Intent.getMediaResults() =
        Gson().fromJson(getStringExtra(MEDIARS), MediaInfoResults::class.java)!!

    fun Intent.getOptionMedia() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(OPTION, OptionMedia::class.java)
    } else {
        getSerializableExtra(OPTION) as OptionMedia
    }

    fun Intent.passActionMedia(action: MediaAction) = putExtra(ACTION, action)

    fun Intent.passMediaResults(mediaInfos: MediaInfoResults) =
        putExtra(MEDIARS, Gson().toJson(mediaInfos))

    fun Intent.passOptionMedia(
        optionMedia: OptionMedia
    ) = putExtra(OPTION, optionMedia)
}
