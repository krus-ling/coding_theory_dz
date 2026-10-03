package com.example.test1internalrepresentation.domain.usecase

import com.example.test1internalrepresentation.domain.model.CodeWord
import kotlin.math.log2

class CalculateMetricsUseCase {

    fun calculateEntropy(probabilities: List<Double>): Double = -probabilities
        .filter { it > 0.0 }
        .sumOf { p -> p * log2(p) }

    fun calculateMaxEntropy(alphabetSize: Int): Double {
        return if (alphabetSize > 0) {
            log2(alphabetSize.toDouble())
        } else {
            0.0
        }
    }

    fun calculateAverageLength(codes: List<CodeWord>): Double = codes
        .sumOf { it.probability * it.length }
}