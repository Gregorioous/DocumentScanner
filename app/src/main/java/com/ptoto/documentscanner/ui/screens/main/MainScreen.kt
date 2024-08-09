package com.ptoto.documentscanner.ui.screens.main

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.ptoto.documentscanner.R
import com.ptoto.documentscanner.data.models.PdfEntity
import com.ptoto.documentscanner.ui.screens.common.ErrorScreen
import com.ptoto.documentscanner.ui.screens.common.LoadingScreen
import com.ptoto.documentscanner.ui.screens.main.components.PdfLayout
import com.ptoto.documentscanner.ui.screens.main.components.RenameDeleteDialog
import com.ptoto.documentscanner.ui.screens.main.components.ThemeSwitcher
import com.ptoto.documentscanner.ui.viewmodels.PdfViewModel
import com.ptoto.documentscanner.utills.Resource
import com.ptoto.documentscanner.utills.copyPdfFileToAppDirectory
import com.ptoto.documentscanner.utills.getFileSize
import com.ptoto.documentscanner.utills.showToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(pdfViewModel: PdfViewModel, darkTheme: Boolean, onThemeUpdated: () -> Unit) {

    LoadingScreen(pdfViewModel = pdfViewModel)
    RenameDeleteDialog(pdfViewModel = pdfViewModel)
    val activity: Activity = LocalContext.current as Activity

    val context = LocalContext.current

    val pdfState by pdfViewModel.pdfStateFlow.collectAsState()

    val message = pdfViewModel.message

    LaunchedEffect(Unit) {
        message.collect {
            when (it) {
                is Resource.Success -> {
                    context.showToast(it.data)
                }

                is Resource.Error -> {
                    context.showToast(it.message)
                }

                Resource.Idle -> {}
                Resource.Loading -> {}

            }
        }
    }


    val scannerLauncher =
        rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts
                .StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val scanningResult: GmsDocumentScanningResult? =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                scanningResult?.pdf?.let { pdf: GmsDocumentScanningResult.Pdf ->
                    Log.d("pdfName", pdf.uri.lastPathSegment.toString())

                    val date = Date()
                    val filename = SimpleDateFormat(
                        "dd-MMM-YYYY- HH:mm:ss",
                        Locale.getDefault()
                    ).format(date) + ".pdf"

                    context.copyPdfFileToAppDirectory(
                        pdf.uri,
                        filename
                    )

                    val pdfEntity = PdfEntity(
                        UUID.randomUUID().toString(),
                        filename,
                        context.getFileSize(filename),
                        date
                    )
                    pdfViewModel.insertPdf(
                        pdfEntity
                    )
                }
            }
        }

    val scanner: GmsDocumentScanner = remember {
        GmsDocumentScanning.getClient(
            GmsDocumentScannerOptions.Builder().setGalleryImportAllowed(true)
                .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL).build()
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.app_name))
            }, actions = {
                ThemeSwitcher(
                    darkTheme = darkTheme,
                    onClick = onThemeUpdated
                )
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scanner.getStartScanIntent(activity).addOnSuccessListener {
                        scannerLauncher.launch(
                            IntentSenderRequest.Builder(it).build()
                        )
                    }.addOnFailureListener {
                        it.printStackTrace()
                        context.showToast(it.message.toString())
                    }
                },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                        contentDescription = "camera"
                    )
                }
            )
        }
    ) { paddingValues ->
        pdfState.DisplayResult(
            onLoading = {
                /* Box(
                     modifier = Modifier
                         .fillMaxSize(),
                     contentAlignment = Alignment.Center
                 ) {
                     CircularProgressIndicator()
                 }*/
            },
            onSuccess = { pdfList ->
                if (pdfList.isEmpty()) {
                    ErrorScreen(message = "No Pdf")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {

                        items(items = pdfList, key = { pdfEntity: PdfEntity ->
                            pdfEntity.id
                        }) { pdfEntity ->
                            PdfLayout(pdfEntity = pdfEntity, pdfViewModel = pdfViewModel)
                        }
                    }
                }
            },
            onError = {
                ErrorScreen(message = it)
            })
    }
}