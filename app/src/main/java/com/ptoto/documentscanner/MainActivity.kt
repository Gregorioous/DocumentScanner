package com.ptoto.documentscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ptoto.documentscanner.ui.screens.main.MainScreen
import com.ptoto.documentscanner.ui.theme.DocumentScannerTheme
import com.ptoto.documentscanner.ui.viewmodels.PdfViewModel

class MainActivity : ComponentActivity() {

    private val pdfViewModel: PdfViewModel by viewModels<PdfViewModel> {
        viewModelFactory {
            addInitializer(PdfViewModel::class) {
                PdfViewModel(application)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            splashScreen.setKeepOnScreenCondition { pdfViewModel.isSplashScreen }
            DocumentScannerTheme {
                MainScreen(pdfViewModel)
            }
        }
    }
}
