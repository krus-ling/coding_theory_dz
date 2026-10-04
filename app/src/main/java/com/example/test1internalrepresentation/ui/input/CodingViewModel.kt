package com.example.test1internalrepresentation.ui.input

import androidx.lifecycle.ViewModel
import com.example.test1internalrepresentation.data.VariantsRepository
import com.example.test1internalrepresentation.domain.model.CodingAnalysis
import com.example.test1internalrepresentation.domain.model.CodingMethodResult
import com.example.test1internalrepresentation.domain.model.SymbolProbability
import com.example.test1internalrepresentation.domain.usecase.AnalyzeTextFrequencyUseCase
import com.example.test1internalrepresentation.domain.usecase.BuildHuffmanUseCase
import com.example.test1internalrepresentation.domain.usecase.BuildShannonFanoUseCase
import com.example.test1internalrepresentation.domain.usecase.CalculateMetricsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.abs

enum class InputMode {
    VARIANT_7_UPPER,
    VARIANT_7_LOWER,
    LARGE_ALPHABET_120,
    TEXT_ANALYSIS,
    CUSTOM_PROBABILITIES
}

data class UiState(
    val currentMode: InputMode = InputMode.VARIANT_7_UPPER,
    val inputText: String = "",
    val customProbabilitiesText: String = "0.25, 0.25, 0.25, 0.25",
    val symbols: List<SymbolProbability> = emptyList(),
    val analysisResult: CodingAnalysis? = null,
    val errorMessage: String? = null
) {
    val uniqueSymbolCount: Int
        get() = if (currentMode == InputMode.TEXT_ANALYSIS) {
            inputText.toSet().size
        } else {
            symbols.size
        }
}

class CodingViewModel(
    private val repository: VariantsRepository = VariantsRepository(),
    private val shannonFanoUseCase: BuildShannonFanoUseCase = BuildShannonFanoUseCase(),
    private val huffmanUseCase: BuildHuffmanUseCase = BuildHuffmanUseCase(),
    private val metricsUseCase: CalculateMetricsUseCase = CalculateMetricsUseCase(),
    private val textAnalysisUseCase: AnalyzeTextFrequencyUseCase = AnalyzeTextFrequencyUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        selectMode(InputMode.VARIANT_7_UPPER)
    }

    fun selectMode(mode: InputMode) {
        _uiState.update { state ->
            val updatedSymbols = when (mode) {
                InputMode.VARIANT_7_UPPER -> repository.getVariantSymbols(7, isUpper = true)
                InputMode.VARIANT_7_LOWER -> repository.getVariantSymbols(7, isUpper = false)
                InputMode.LARGE_ALPHABET_120 -> repository.getZipf120Symbols()
                InputMode.TEXT_ANALYSIS -> {
                    val defaultText = if (state.inputText.isEmpty()) repository.getSampleLongText() else state.inputText
                    textAnalysisUseCase.execute(defaultText)
                }
                InputMode.CUSTOM_PROBABILITIES -> parseCustomProbabilities(state.customProbabilitiesText)
            }
            state.copy(
                currentMode = mode,
                inputText = if (mode == InputMode.TEXT_ANALYSIS && state.inputText.isEmpty()) repository.getSampleLongText() else state.inputText,
                symbols = updatedSymbols,
                analysisResult = null,
                errorMessage = null
            )
        }
    }

    fun onTextChanged(newText: String) {
        val symbols = textAnalysisUseCase.execute(newText)
        _uiState.update { it.copy(inputText = newText, symbols = symbols) }
    }

    fun onInsertRichTextPreset() {
        val richText = repository.getRich100PlusUniqueSymbolsText()
        val symbols = textAnalysisUseCase.execute(richText)
        _uiState.update {
            it.copy(
                currentMode = InputMode.TEXT_ANALYSIS,
                inputText = richText,
                symbols = symbols,
                analysisResult = null,
                errorMessage = null
            )
        }
    }

    fun onCustomProbabilitiesChanged(raw: String) {
        val symbols = parseCustomProbabilities(raw)
        _uiState.update { it.copy(customProbabilitiesText = raw, symbols = symbols) }
    }

    fun calculate() {
        val symbols = _uiState.value.symbols
        if (symbols.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Список символов пуст") }
            return
        }

        val probs = symbols.map { it.probability }
        val sum = probs.sum()
        if (abs(sum - 1.0) > 0.05) {
            _uiState.update { it.copy(errorMessage = "Сумма вероятностей равна ${String.format("%.3f", sum)}, а должна быть 1.0") }
            return
        }

        val h = metricsUseCase.calculateEntropy(probs)
        val hMax = metricsUseCase.calculateMaxEntropy(symbols.size)

        val sfCodes = shannonFanoUseCase.execute(symbols)
        val sfAvgLen = metricsUseCase.calculateAverageLength(sfCodes)
        val sfResult = CodingMethodResult(
            methodName = "Шеннон-Фано",
            codes = sfCodes,
            averageLength = sfAvgLen,
            compressionRatio = if (sfAvgLen > 0) hMax / sfAvgLen else 0.0,
            relativeEfficiency = if (sfAvgLen > 0) h / sfAvgLen else 0.0
        )

        val hufCodes = huffmanUseCase.execute(symbols)
        val hufAvgLen = metricsUseCase.calculateAverageLength(hufCodes)
        val hufResult = CodingMethodResult(
            methodName = "Хаффман",
            codes = hufCodes,
            averageLength = hufAvgLen,
            compressionRatio = if (hufAvgLen > 0) hMax / hufAvgLen else 0.0,
            relativeEfficiency = if (hufAvgLen > 0) h / hufAvgLen else 0.0
        )

        val analysis = CodingAnalysis(
            entropy = h,
            maxEntropy = hMax,
            shannonFano = sfResult,
            huffman = hufResult
        )

        _uiState.update { it.copy(analysisResult = analysis, errorMessage = null) }
    }

    private fun parseCustomProbabilities(raw: String): List<SymbolProbability> {
        val parts = raw.split(',', ' ', ';').filter { it.isNotBlank() }
        return parts.mapIndexedNotNull { index, s ->
            s.toDoubleOrNull()?.let { prob ->
                SymbolProbability(symbol = "x${index + 1}", probability = prob)
            }
        }
    }
}
