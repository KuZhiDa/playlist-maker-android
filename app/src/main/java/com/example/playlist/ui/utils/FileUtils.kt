package com.example.playlist.ui.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun saveCoverToInternalStorage(context: Context, sourceUri: Uri): String? {
    val directory = File(context.filesDir, "playlist_covers").apply { mkdirs() }
    val file = File(directory, "playlist_cover_${System.currentTimeMillis()}.jpg")
    return try {
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (_: Exception) {
        file.delete()
        null
    }
}
