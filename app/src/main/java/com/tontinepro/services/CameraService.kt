package com.tontinepro.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CameraService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun createImageFile(): File {
        val timeStamp = System.currentTimeMillis().toString()
        val storageDir = File(context.cacheDir, "images")

        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File(storageDir, "IMG_${timeStamp}.jpg")
    }

    fun getUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun compressImage(imagePath: String, maxWidth: Int = 800, maxHeight: Int = 600): String? {
        return try {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val scaledBitmap = scaleBitmap(bitmap, maxWidth, maxHeight)

            val compressedFile = createImageFile()
            val outputStream = FileOutputStream(compressedFile)

            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.close()

            compressedFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val scaleX = maxWidth.toFloat() / width
        val scaleY = maxHeight.toFloat() / height
        val scale = minOf(scaleX, scaleY)

        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}