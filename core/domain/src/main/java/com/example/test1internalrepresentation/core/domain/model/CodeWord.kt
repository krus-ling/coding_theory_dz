package com.example.test1internalrepresentation.core.domain.model

/**
 * Модель двоичного кодового слова, построенного для конкретного символа алфавита.
 *
 * @property symbol Обозначение символа сообщений.
 * @property probability Вероятность появления данного символа.
 * @property code Сформированная двоичная последовательность бит (например, "0101").
 * @property length Разрядность (длина) двоичного кодового слова в битах.
 */
data class CodeWord(
    val symbol: String,
    val probability: Double,
    val code: String,
    val length: Int = code.length
)
