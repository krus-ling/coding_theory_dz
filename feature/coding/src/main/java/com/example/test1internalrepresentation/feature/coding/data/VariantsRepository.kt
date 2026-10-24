package com.example.test1internalrepresentation.feature.coding.data

import com.example.test1internalrepresentation.core.domain.model.SymbolProbability

/**
 * Модель данных варианта из методического пособия (Таблица 1.1).
 *
 * @property number Номер варианта (например, 7).
 * @property upperRow Верхний ряд вероятностей.
 * @property lowerRow Нижний ряд вероятностей.
 */
data class VariantData(
    val number: Int,
    val upperRow: List<Double>,
    val lowerRow: List<Double>
)

/**
 * Репозиторий источника вариантов из методички, распределения Ципфа и генерации текстов.
 */
class VariantsRepository {

    private val variants = listOf(
        VariantData(
            number = 7,
            upperRow = listOf(0.190, 0.165, 0.137, 0.122, 0.112, 0.107, 0.091, 0.076),
            lowerRow = listOf(0.199, 0.157, 0.143, 0.140, 0.139, 0.122, 0.063, 0.037)
        )
    )

    /**
     * Возвращает список вероятностей символов для указанного варианта.
     *
     * @param variantNumber Номер варианта (7).
     * @param isUpper Флаг выбора верхнего ряда (true) или нижнего ряда (false).
     */
    fun getVariantSymbols(variantNumber: Int, isUpper: Boolean): List<SymbolProbability> {
        val variant = variants.find { it.number == variantNumber } ?: variants.first()
        val row = if (isUpper) variant.upperRow else variant.lowerRow
        return row.mapIndexed { index, prob ->
            SymbolProbability(symbol = "a${index + 1}", probability = prob)
        }
    }

    /**
     * Генерирует ансамбль сообщений из N = 120 символов по закону распределения Ципфа.
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

    /**
     * Возвращает образцовый текст средней длины для тестирования частотного анализа.
     */
    fun getSampleLongText(): String {
        return "Теория информации и кодирования изучает математические методы сжатия, передачи и защиты данных. " +
                "Алгоритмы Хаффмана и Шеннона-Фано устраняют избыточность сообщений в каналах без помех."
    }

    /**
     * Возвращает насыщенный текст, содержащий более 100 уникальных символов алфавита.
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
