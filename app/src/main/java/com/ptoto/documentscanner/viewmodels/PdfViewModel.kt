package com.ptoto.documentscanner.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PdfViewModel: ViewModel() {

    var isSplashScreen by mutableStateOf(false)


    init {
        viewModelScope.launch {
            delay(3000)
            isSplashScreen = false
        }
    }
}