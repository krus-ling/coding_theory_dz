package com.example.test1internalrepresentation.core.domain.util

import java.util.Locale

/**
 * Функция-расширение для форматирования вещественных чисел типа Double с заданным количеством знаков после запятой.
 *
 * @param decimals Количество знаков после десятичной точки (по умолчанию 4).
 * @return Отформатированная строка с фиксированной точностью.
 */
fun Double.formatDecimal(decimals: Int = 4): String {
    return String.format(Locale.US, "%.${decimals}f", this)
}
