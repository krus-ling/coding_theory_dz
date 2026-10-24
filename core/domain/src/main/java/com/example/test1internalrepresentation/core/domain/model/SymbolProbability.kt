package com.example.test1internalrepresentation.core.domain.model

/**
 * Модель данных символа алфавита и вероятности его появления в источнике сообщений.
 *
 * @property symbol Графическое или текстовое обозначение символа (например, "a₁", "x₅", "'A'").
 * @property probability Вероятность появления символа P(a_i) в диапазоне [0.0, 1.0].
 */
data class SymbolProbability(
    val symbol: String,
    val probability: Double
)
