package com.ptoto.documentscanner.ui.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ptoto.documentscanner.ui.viewmodels.PdfViewModel

@Composable
fun LoadingScreen(pdfViewModel: PdfViewModel) {
    if (pdfViewModel.loadingDialog) {
        Dialog(
            onDismissRequest = { pdfViewModel.loadingDialog = false },
            properties = DialogProperties(false, false)
        ) {

            Box(
                modifier = Modifier
                    .size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}