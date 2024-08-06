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

fun copyPdfFileToAppDirectory(context: Context, pdfUri: Uri, destinationFileName: String) {
    val inputStream = context.contentResolver.openInputStream(pdfUri)
    val outputFile = File(context.filesDir, destinationFileName)
    FileOutputStream(outputFile).use {
        inputStream?.copyTo(it)
    }
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

fun getFileUri(context: Context, fileName: String): Uri {
    val file = File(context.filesDir, fileName)
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}