package com.example.test1internalrepresentation.data

import com.example.test1internalrepresentation.domain.model.SymbolProbability

data class VariantData(
    val number: Int,
    val upperRow: List<Double>,
    val lowerRow: List<Double>
)

class VariantsRepository {

    // Таблица 1.1 из методички (основной упор на вариант 7)
    private val variants = listOf(
        VariantData(
            number = 7,
            upperRow = listOf(0.190, 0.165, 0.137, 0.122, 0.112, 0.107, 0.091, 0.076),
            lowerRow = listOf(0.199, 0.157, 0.143, 0.140, 0.139, 0.122, 0.063, 0.037)
        )
    )

    fun getVariantSymbols(variantNumber: Int, isUpper: Boolean): List<SymbolProbability> {
        val variant = variants.find { it.number == variantNumber } ?: variants.first()
        val row = if (isUpper) variant.upperRow else variant.lowerRow
        return row.mapIndexed { index, prob ->
            SymbolProbability(symbol = "a${index + 1}", probability = prob)
        }
    }

    fun getSampleLongText(): String {
        return "Теория информации и кодирования изучает математические методы сжатия, передачи и защиты данных. " +
                "Алгоритмы Хаффмана и Шеннона-Фано устраняют избыточность сообщений в каналах без помех."
    }
}