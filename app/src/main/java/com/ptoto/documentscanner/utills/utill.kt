package com.ptoto.documentscanner.utills

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.copyPdfFileToAppDirectory(pdfUri: Uri, destinationFileName: String) {
    val outputFile = File(filesDir, destinationFileName)
    contentResolver.openInputStream(pdfUri)?.use { inputStream ->
        FileOutputStream(outputFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
}

fun Context.getFileSize(fileName: String): String {
    val file = File(filesDir, fileName)
    val fileSizeBytes = file.length()
    return formatFileSize(fileSizeBytes)
}

fun Context.renameFile(oldFileName: String, newFileName: String) {
    val oldFile = File(filesDir, oldFileName)
    val newFile = File(filesDir, newFileName)
    oldFile.renameTo(newFile)
}

fun Context.deleteFele(fileName: String): Boolean {
    val file = File(filesDir, fileName)
    return file.deleteRecursively()
}

fun Context.getFileUri(fileName: String): Uri {
    val file = File(filesDir, fileName)
    return FileProvider.getUriForFile(this, "${packageName}.provider", file)
}


private fun formatFileSize(sizeInBytes: Long): String {
    val kiloBytes = sizeInBytes / 1024
    val megaBytes = kiloBytes / 1024
    val gigaBytes = megaBytes / 1024

    return when {
        gigaBytes > 0 -> "$gigaBytes GB"
        megaBytes > 0 -> "$megaBytes MB"
        kiloBytes > 0 -> "$kiloBytes KB"
        else -> "$sizeInBytes B"
    }
}