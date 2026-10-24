package com.example.test1internalrepresentation.feature.coding.domain

import com.example.test1internalrepresentation.core.domain.model.CodeWord
import com.example.test1internalrepresentation.core.domain.model.SymbolProbability
import kotlin.math.abs

/**
 * UseCase построения двоичного кода алгоритмом Шеннона — Фано путем рекурсивного деления упорядоченного списка на две группы.
 */
class BuildShannonFanoUseCase {

    /**
     * Строит кодовые слова методом Шеннона — Фано.
     *
     * @param symbols Исходный ансамбль символов с вероятностями.
     * @return Список построенных кодовых слов [CodeWord].
     */
    fun execute(symbols: List<SymbolProbability>): List<CodeWord> {
        if (symbols.isEmpty()) return emptyList()

        val sorted = symbols.sortedByDescending { it.probability }
        val codeMap = HashMap<String, String>()

        buildCodes(sorted, "", codeMap)

        return sorted.map { sp ->
            CodeWord(
                symbol = sp.symbol,
                probability = sp.probability,
                code = codeMap[sp.symbol] ?: ""
            )
        }
    }

    private fun buildCodes(
        list: List<SymbolProbability>,
        prefix: String,
        outMap: MutableMap<String, String>
    ) {
        if (list.size == 1) {
            val item = list.first()
            outMap[item.symbol] = if (prefix.isEmpty()) "0" else prefix
            return
        }

        var total = 0.0
        for (item in list) {
            total += item.probability
        }

        var halfSum = 0.0
        var splitIndex = 0
        var minDiff = Double.MAX_VALUE

        for (i in list.indices) {
            halfSum += list[i].probability
            val rest = total - halfSum
            val diff = abs(halfSum - rest)
            if (diff < minDiff) {
                minDiff = diff
                splitIndex = i
            }
        }

        val group0 = list.subList(0, splitIndex + 1)
        val group1 = list.subList(splitIndex + 1, list.size)

        if (group0.isNotEmpty()) buildCodes(group0, prefix + "0", outMap)
        if (group1.isNotEmpty()) buildCodes(group1, prefix + "1", outMap)
    }
}
