package com.example.test1internalrepresentation.feature.archiver.ui

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test1internalrepresentation.feature.archiver.domain.CompressHuffmanUseCase
import com.example.test1internalrepresentation.feature.archiver.domain.CompressionReport
import com.example.test1internalrepresentation.feature.archiver.domain.DecompressHuffmanUseCase
import com.example.test1internalrepresentation.feature.archiver.domain.DecompressionReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets
import kotlin.random.Random

data class Lab2UiState(
    val selectedTabIndex: Int = 0,
    val activePresetType: Int = 1,
    val compressionReport: CompressionReport? = null,
    val decompressionReport: DecompressionReport? = null,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class Lab2ViewModel(
    private val compressUseCase: CompressHuffmanUseCase = CompressHuffmanUseCase(),
    private val decompressUseCase: DecompressHuffmanUseCase = DecompressHuffmanUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(Lab2UiState())
    val uiState: StateFlow<Lab2UiState> = _uiState.asStateFlow()

    init {
        applyPreset(1)
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTabIndex = tabIndex, errorMessage = null, successMessage = null) }
    }

    fun applyPreset(presetType: Int) {
        val fileName: String
        val bytes: ByteArray

        when (presetType) {
            1 -> {
                fileName = "text_document.txt"
                val paragraph = "Теория информации и кодирования является фундаментальной дисциплиной. " +
                        "Оптимальные префиксные коды Хаффмана обеспечивают максимальную степень статистического сжатия данных без потерь. " +
                        "При передаче сообщений по цифровым каналам связи сжатие без потерь позволяет значительно сократить объем передаваемой информации.\n"
                bytes = paragraph.repeat(8).toByteArray(StandardCharsets.UTF_8)
            }
            2 -> {
                fileName = "single_byte.bin"
                bytes = byteArrayOf(0x41)
            }
            3 -> {
                fileName = "random_data.bin"
                bytes = Random.nextBytes(1024)
            }
            else -> {
                fileName = "sample.txt"
                bytes = "Тестовая последовательность данных".toByteArray(StandardCharsets.UTF_8)
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(activePresetType = presetType, errorMessage = null, successMessage = null) }
            try {
                val report = withContext(Dispatchers.Default) {
                    compressUseCase(bytes, fileName)
                }
                _uiState.update {
                    it.copy(
                        compressionReport = report,
                        isProcessing = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        errorMessage = "Ошибка архивации: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun onCompressFileSelected(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, errorMessage = null, successMessage = null) }
            try {
                val fileName = getFileName(context, uri)
                val bytes = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                } ?: throw IllegalArgumentException("Не удалось прочитать содержимое файла")

                val report = withContext(Dispatchers.Default) {
                    compressUseCase(bytes, fileName)
                }

                _uiState.update {
                    it.copy(
                        compressionReport = report,
                        isProcessing = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        errorMessage = "Ошибка сжатия файла: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun onDecompressFileSelected(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, errorMessage = null, successMessage = null) }
            try {
                val archiveName = getFileName(context, uri)
                val compressedBytes = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                } ?: throw IllegalArgumentException("Не удалось прочитать сжатый файл")

                val report = withContext(Dispatchers.Default) {
                    decompressUseCase(compressedBytes, archiveName)
                }

                _uiState.update {
                    it.copy(
                        decompressionReport = report,
                        isProcessing = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        errorMessage = "Ошибка распаковки архива: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun exportCompressedFile(context: Context, uri: Uri) {
        val report = _uiState.value.compressionReport ?: return
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(report.compressedData)
                    }
                }
                _uiState.update { it.copy(successMessage = "Архив успешно сохранен в память устройства") }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Ошибка сохранения архива: ${e.localizedMessage}") }
            }
        }
    }

    fun exportDecompressedFile(context: Context, uri: Uri) {
        val report = _uiState.value.decompressionReport ?: return
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(report.restoredData)
                    }
                }
                _uiState.update { it.copy(successMessage = "Распакованный файл успешно сохранен") }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Ошибка сохранения файла: ${e.localizedMessage}") }
            }
        }
    }

    private fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = cursor.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "file.bin"
    }
}
