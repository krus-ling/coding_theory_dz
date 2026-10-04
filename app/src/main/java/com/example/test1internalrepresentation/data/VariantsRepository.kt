package com.example.test1internalrepresentation.data

import com.example.test1internalrepresentation.domain.model.SymbolProbability

data class VariantData(
    val number: Int,
    val upperRow: List<Double>,
    val lowerRow: List<Double>
)

class VariantsRepository {

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

    /**
     * Генератор распределения вероятностей для алфавита из 120 уникальных символов (x₁..x₁₂₀).
     * Вероятности строго отсортированы по убыванию, а их сумма строго равна 1.0.
     */
    fun getZipf120Symbols(): List<SymbolProbability> {
        val count = 120
        val weights = (1..count).map { 1.0 / it }
        val sumWeights = weights.sum()
        val probs = weights.map { it / sumWeights }

        val sumExceptLast = probs.dropLast(1).sum()
        val adjustedLast = 1.0 - sumExceptLast

        return probs.mapIndexed { index, prob ->
            val finalProb = if (index == count - 1) adjustedLast else prob
            SymbolProbability(symbol = "x${index + 1}", probability = finalProb)
        }
    }

    fun getSampleLongText(): String {
        return "Теория информации и кодирования изучает математические методы сжатия, передачи и защиты данных. " +
                "Алгоритмы Хаффмана и Шеннона-Фано устраняют избыточность сообщений в каналах без помех."
    }

    /**
     * Готовый пресет богатого текста с более чем 100 УНИКАЛЬНЫМИ символами (кириллица, латиница, цифры, спецсимволы).
     */
    fun getRich100PlusUniqueSymbolsText(): String {
        return "Теория информации & алгоритмы сжатия данных (Lab №1): " +
                "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ " +
                "абвгдеёжзийклмнопрстуфхцчшщъыьэюя " +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ " +
                "abcdefghijklmnopqrstuvwxyz " +
                "0123456789 " +
                "!@#$%^&*()_+-=[]{}|;:'\",.<>/?~«»—±°×÷"
    }
}
