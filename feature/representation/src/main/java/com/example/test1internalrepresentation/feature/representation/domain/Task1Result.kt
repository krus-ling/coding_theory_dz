package com.example.test1internalrepresentation.feature.representation.domain

/**
 * Модель результата выполнения математических операций перевода и сложения чисел в разрядной сетке.
 *
 * @property decimalResult Итоговое десятичное значение суммы.
 * @property binaryResult Двоичное представление итоговой суммы в дополнительном коде.
 * @property steps Хронологический список шагов пошагового решения.
 * @property verification Пункты детальной обратной математической проверки результата.
 */
data class Task1Result(
    val decimalResult: Int,
    val binaryResult: String,
    val steps: List<String>,
    val verification: List<String>
)
