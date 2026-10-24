package com.example.test1internalrepresentation.feature.coding.domain

/**
 * Итоговая модель комплексного сравнительного анализа результатов эффективного кодирования.
 *
 * @property entropy Энтропия источника H.
 * @property maxEntropy Максимальная энтропия H_max.
 * @property shannonFano Результат алгоритма Шеннона — Фано.
 * @property huffman Результат алгоритма Хаффмана.
 */
data class CodingAnalysis(
    val entropy: Double,
    val maxEntropy: Double,
    val shannonFano: CodingMethodResult,
    val huffman: CodingMethodResult
)
