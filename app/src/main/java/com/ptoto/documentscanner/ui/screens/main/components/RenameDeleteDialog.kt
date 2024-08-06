package com.ptoto.documentscanner.ui.screens.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ptoto.documentscanner.R
import com.ptoto.documentscanner.ui.viewmodels.PdfViewModel
import com.ptoto.documentscanner.utills.deleteFele
import com.ptoto.documentscanner.utills.renameFile
import com.ptoto.documentscanner.utills.showToast
import java.util.Date

@Composable
fun RenameDeleteDialog(pdfViewModel: PdfViewModel) {

    var newNameText by remember(pdfViewModel.currentPdfEntity) {
        mutableStateOf(pdfViewModel.currentPdfEntity?.name ?: "")
    }
    val context = LocalContext.current
    if (pdfViewModel.showRenameDialog) {

        Dialog(onDismissRequest = {
            pdfViewModel.showRenameDialog = false
        }) {
            Surface(
                shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Rename",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newNameText,
                        onValueChange = { newText -> newNameText = newText },
                        label = { Text(text = "PDF Name") })
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        IconButton(onClick = {
                            pdfViewModel.currentPdfEntity?.let {
                                pdfViewModel.showRenameDialog = false
                                if (context.deleteFele(it.name)) {
                                    pdfViewModel.deletePdf(it)
                                } else {
                                    context.showToast("Error")
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = null,
                                tint = Color.Blue
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            pdfViewModel.showRenameDialog = false
                        }) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            pdfViewModel.currentPdfEntity?.let { pdf ->
                                if (!pdf.name.equals(newNameText, true)) {
                                    pdfViewModel.showRenameDialog = false
                                    context.renameFile(
                                        pdf.name,
                                        newNameText
                                    )
                                    val updatePdf = pdf.copy(
                                        name = newNameText,
                                        lastModifiedTime = Date()
                                    )
                                    pdfViewModel.updatePdf(updatePdf)
                                } else {
                                    pdfViewModel.showRenameDialog = false
                                }
                            }
                        }) {
                            Text(text = "Update")
                        }
                    }
                }
            }
        }
    }
}
