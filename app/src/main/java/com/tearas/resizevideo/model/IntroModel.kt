package com.tearas.resizevideo.model

import androidx.annotation.DrawableRes

data class IntroModel(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,
) {
}