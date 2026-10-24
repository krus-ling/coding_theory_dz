package com.example.test1internalrepresentation.feature.coding.domain

import com.example.test1internalrepresentation.core.domain.model.CodeWord

/**
 * Результат расчета отдельного метода сжатия.
 *
 * @property methodName Наименование метода.
 * @property codes Список сгенерированных кодовых слов.
 * @property averageLength Средняя длина кодового слова L_ср.
 * @property compressionRatio Коэффициент статистического сжатия K_сс = H_max / L_ср.
 * @property relativeEfficiency Коэффициент относительной эффективности K_оэ = H / L_ср.
 */
data class CodingMethodResult(
    val methodName: String,
    val codes: List<CodeWord>,
    val averageLength: Double,
    val compressionRatio: Double,
    val relativeEfficiency: Double
)
