package com.example.test1internalrepresentation.feature.coding.domain

import com.example.test1internalrepresentation.core.domain.model.CodeWord
import kotlin.math.log2

/**
 * UseCase расчета теоретико-информационных метрик сжатия (энтропии H, H_max, L_ср).
 */
class CalculateMetricsUseCase {

    /**
     * Вычисляет энтропию источника сообщений Шеннона H = -∑ P_i log2(P_i).
     */
    fun calculateEntropy(probabilities: List<Double>): Double = -probabilities
        .filter { it > 0.0 }
        .sumOf { p -> p * log2(p) }

    /**
     * Вычисляет максимальную энтропию равновероятного источника H_max = log2(N).
     */
    fun calculateMaxEntropy(alphabetSize: Int): Double {
        return if (alphabetSize > 0) {
            log2(alphabetSize.toDouble())
        } else {
            0.0
        }
    }

    /**
     * Вычисляет среднюю длину кодового слова L_ср = ∑ P_i · l_i.
     */
    fun calculateAverageLength(codes: List<CodeWord>): Double = codes
        .sumOf { it.probability * it.length }
}
