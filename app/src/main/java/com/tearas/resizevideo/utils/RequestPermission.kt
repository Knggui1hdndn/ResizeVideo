package com.tearas.resizevideo.utils

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData

class RequestPermission(context: FragmentActivity) : LiveData<Boolean>() {

    private val rqPermission =
        context.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            postValue(it)
        }

    private val rqPermissions =
        context.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            postValue(it.all { it.value })
        }

    fun launch(permission: String) = rqPermission.launch(permission)
    fun launch(permissions: Array<String>) = rqPermissions.launch(permissions)
}