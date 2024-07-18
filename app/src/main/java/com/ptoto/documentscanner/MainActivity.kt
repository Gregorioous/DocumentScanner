package com.ptoto.documentscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ptoto.documentscanner.screens.MainScreen
import com.ptoto.documentscanner.ui.theme.DocumentScannerTheme
import com.ptoto.documentscanner.viewmodels.PdfViewModel

class MainActivity : ComponentActivity() {

    private val pdfViewModel:PdfViewModel by viewModels<PdfViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            splashScreen.setKeepOnScreenCondition { pdfViewModel.isSplashScreen }
            DocumentScannerTheme {
                MainScreen()
            }
        }
    }
}
