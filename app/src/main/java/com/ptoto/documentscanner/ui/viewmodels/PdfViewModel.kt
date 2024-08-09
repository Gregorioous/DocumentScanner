package com.ptoto.documentscanner.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ptoto.documentscanner.data.models.PdfEntity
import com.ptoto.documentscanner.data.repository.PdfRepository
import com.ptoto.documentscanner.utills.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PdfViewModel(application: Application) : ViewModel() {

    var isSplashScreen by mutableStateOf(false)
    var showRenameDialog by mutableStateOf(false)
    var loadingDialog by mutableStateOf(false)
    var isDarkMode by mutableStateOf(false)

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }
    private val pdfRepository = PdfRepository(application)

    private val _pdfStateFlow =
        MutableStateFlow<Resource<List<PdfEntity>>>(Resource.Idle)

    val pdfStateFlow: StateFlow<Resource<List<PdfEntity>>>
        get() = _pdfStateFlow

    var currentPdfEntity: PdfEntity? by mutableStateOf(null)

    private val _message: Channel<Resource<String>> = Channel()
    val message = _message.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            isSplashScreen = false
        }

        viewModelScope.launch {
            pdfStateFlow.collect {
                when (it) {
                    is Resource.Error -> {
                        loadingDialog = false
                    }

                    Resource.Idle -> {

                    }

                    Resource.Loading -> {
                        loadingDialog = true
                    }

                    is Resource.Success -> {
                        loadingDialog = false
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _pdfStateFlow.emit(Resource.Loading)
            delay(5000)
            pdfRepository.getPdfList().catch {
                _pdfStateFlow.emit(Resource.Error(it.message ?: "Something went wrong"))
                it.printStackTrace()
            }.collect {
                _pdfStateFlow.emit(Resource.Success(it))
            }
        }
    }

    fun insertPdf(pdfEntity: PdfEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                /*_pdfStateFlow.emit(Resource.Loading)*/
                loadingDialog = true
                delay(2000)
                val result = pdfRepository.insertPdf(pdfEntity)
                if (result.toInt() != -1) {
                    _message.send(Resource.Success("Inserted Pdf Successfully"))
                } else {
                    _message.send(Resource.Error("Something went wrong"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _message.send(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }

    fun deletePdf(pdfEntity: PdfEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                /*  _pdfStateFlow.emit(Resource.Loading)*/
                loadingDialog = true
                delay(2000)
                pdfRepository.deletePdf(pdfEntity)
                _message.send(Resource.Success("Deleted Pdf Successfully"))
            } catch (e: Exception) {
                e.printStackTrace()
                _message.send(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }

    fun updatePdf(pdfEntity: PdfEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                /*  _pdfStateFlow.emit(Resource.Loading)*/
                loadingDialog = true
                delay(2000)
                pdfRepository.updatePdf(pdfEntity)
                _message.send(Resource.Success("Updated Pdf Successfully"))
            } catch (e: Exception) {
                e.printStackTrace()
                _message.send(Resource.Error(e.message ?: "Something went wrong"))
            }
        }
    }
}