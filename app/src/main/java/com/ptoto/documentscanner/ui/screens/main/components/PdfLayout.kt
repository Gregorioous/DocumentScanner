package com.ptoto.documentscanner.ui.screens.main.components

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ptoto.documentscanner.R
import com.ptoto.documentscanner.data.models.PdfEntity
import com.ptoto.documentscanner.ui.viewmodels.PdfViewModel
import com.ptoto.documentscanner.utills.getFileUri
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfLayout(pdfEntity: PdfEntity, pdfViewModel: PdfViewModel) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(10.dp),
        onClick = {
            val getFileUri = context.getFileUri(pdfEntity.name)
            val browserIntent = Intent(Intent.ACTION_VIEW, getFileUri)
            browserIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            activity.startActivity(browserIntent)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.baseline_picture_as_pdf_24
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Title: ${pdfEntity.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Size: ${pdfEntity.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Date: ${
                        SimpleDateFormat(
                            "dd-MMM-YYYY- HH:mm:ss ",
                            java.util.Locale.getDefault()
                        ).format(pdfEntity.lastModifiedTime)
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = {
                pdfViewModel.currentPdfEntity = pdfEntity
                pdfViewModel.showRenameDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "more"
                )
            }
        }
    }
}