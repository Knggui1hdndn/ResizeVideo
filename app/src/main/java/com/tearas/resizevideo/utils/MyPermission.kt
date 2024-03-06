package com.tearas.resizevideo.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi


 const val READ_MEDIA_VIDEO = Manifest.permission.READ_MEDIA_VIDEO
const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
fun Context.checkPermission(permission: Array<String>): Boolean {
    return permission.all { permission.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED } }
}

fun Context.checkPermission(permission: String): Boolean {
    return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}
