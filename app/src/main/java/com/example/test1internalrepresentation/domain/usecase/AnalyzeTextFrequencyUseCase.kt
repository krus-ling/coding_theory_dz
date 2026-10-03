package com.example.test1internalrepresentation.domain.usecase

import com.example.test1internalrepresentation.domain.model.SymbolProbability

class AnalyzeTextFrequencyUseCase {
    fun execute(text: String): List<SymbolProbability> {
        if (text.isEmpty()) {
            return emptyList()
        }

        val counts = text.groupingBy { it.toString() }.eachCount()
        val total = text.length.toDouble()

        return counts
            .map { (char, count) ->
                SymbolProbability(
                    symbol = if (char == " ") "Space" else char,
                    probability = count / total
                )
            }
            .sortedByDescending { it.probability }
    }
}