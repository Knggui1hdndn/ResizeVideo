package com.tearas.resizevideo.model

data class  DialogModel(
    val title: String,
    val message: String,
    val textNegative: String,
    val textPositive: String,
)

sealed class DialogAction {
    data object DELETE : DialogAction()
    data object REPLACE : DialogAction()
    data object ANSWER : DialogAction()
    data object BACK : DialogAction()
}
