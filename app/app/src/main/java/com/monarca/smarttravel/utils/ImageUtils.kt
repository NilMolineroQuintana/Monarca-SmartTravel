package com.monarca.smarttravel.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val fileName = "trip_img_${UUID.randomUUID()}.jpg"
        val file = File(context.filesDir, fileName)
        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        file.absolutePath
    } catch (e: Exception) {
        null
    }
}