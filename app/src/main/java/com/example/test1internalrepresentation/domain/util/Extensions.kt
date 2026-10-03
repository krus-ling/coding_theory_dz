package com.example.test1internalrepresentation.domain.util

import java.util.Locale

/**
 * Расширение для форматирования чисел с плавающей точкой в строку
 * с заданным количеством знаков после запятой без нагромождения String.format в UI.
 */
fun Double.formatDecimal(decimals: Int = 4): String {
    return String.format(Locale.US, "%.${decimals}f", this)
}
