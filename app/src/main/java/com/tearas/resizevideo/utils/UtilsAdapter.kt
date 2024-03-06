package com.tearas.resizevideo.utils

import android.graphics.Color
import android.text.format.Formatter
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tearas.resizevideo.R
import com.tearas.resizevideo.model.Resolution
import com.tearas.resizevideo.utils.Utils.isDarkMode

@BindingAdapter("imageUrl")
fun ImageView.loadImage(path: String) {
    Glide.with(this)
        .load("file:///$path")
        .into(this)
}

@BindingAdapter("formatSize")
fun AppCompatTextView.formatSize(size: Long) {
    this.text = Formatter.formatFileSize(this.context, size)
}

@BindingAdapter("isSelected")
fun AppCompatTextView.setTextColor(isSelected: Boolean) {
    val context = context ?: return
    val textColor = if (isSelected) {
        context.getColor(R.color.maintream)
    } else {
        if (!context.isDarkMode()) {
            Color.BLACK
        } else {
            Color.WHITE
        }
    }

    setTextColor(textColor)
}



