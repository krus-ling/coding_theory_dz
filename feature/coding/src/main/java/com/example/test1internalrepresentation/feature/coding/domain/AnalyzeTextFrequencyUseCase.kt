package com.example.test1internalrepresentation.feature.coding.domain

import com.example.test1internalrepresentation.core.domain.model.SymbolProbability

/**
 * UseCase частотного анализа текста и вычисления относительных вероятностей символов.
 */
class AnalyzeTextFrequencyUseCase {

    /**
     * Выполняет частотный анализ символов входной строки.
     *
     * @param text Исходный текст.
     * @return Список [SymbolProbability], отсортированный по убыванию вероятности.
     */
    fun execute(text: String): List<SymbolProbability> {
        if (text.isEmpty()) return emptyList()

        val totalChars = text.length.toDouble()
        val frequencyMap = HashMap<Char, Int>()

        for (ch in text) {
            frequencyMap[ch] = (frequencyMap[ch] ?: 0) + 1
        }

        return frequencyMap.map { (ch, count) ->
            val displaySymbol = when (ch) {
                ' ' -> "Space"
                '\n' -> "\\n"
                '\t' -> "\\t"
                '\r' -> "\\r"
                else -> ch.toString()
            }
            SymbolProbability(
                symbol = displaySymbol,
                probability = count / totalChars
            )
        }.sortedByDescending { it.probability }
    }
}
