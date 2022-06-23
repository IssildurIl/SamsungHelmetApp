package com.learning.samsunghelmetapp.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
const val CAMERA = Manifest.permission.CAMERA
const val REQUEST_ID_MULTIPLE_PERMISSIONS = 7
const val PERMISSION_REQUEST = 200

private fun checkPermission(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= 23
        && ContextCompat.checkSelfPermission(
            APP_ACTIVITY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), PERMISSION_REQUEST)
        false
    } else true
}
