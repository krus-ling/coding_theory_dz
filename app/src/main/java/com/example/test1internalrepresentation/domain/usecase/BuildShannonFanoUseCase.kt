package com.example.test1internalrepresentation.domain.usecase

import com.example.test1internalrepresentation.domain.model.CodeWord
import com.example.test1internalrepresentation.domain.model.SymbolProbability
import kotlin.math.abs

class BuildShannonFanoUseCase {

    fun execute(symbols: List<SymbolProbability>): List<CodeWord> {
        val sorted = symbols.sortedByDescending { it.probability }
        val codeMap = mutableMapOf<String, StringBuilder>()
        sorted.forEach { codeMap[it.symbol] = StringBuilder() }

        fun divide(subList: List<SymbolProbability>) {
            if (subList.size <= 1) return

            val total = subList.sumOf { it.probability }
            var running = 0.0
            var bestSplitIndex = 0
            var minDiff = Double.MAX_VALUE

            // Ищем границу, где сумма левой части максимально близка к половине
            for (i in 0 until subList.size - 1) {
                running += subList[i].probability
                val diff = abs(running - (total - running))
                if (diff < minDiff) {
                    minDiff = diff
                    bestSplitIndex = i
                }
            }

            val group0 = subList.subList(0, bestSplitIndex + 1)
            val group1 = subList.subList(bestSplitIndex + 1, subList.size)

            group0.forEach { codeMap[it.symbol]?.append("0") }
            group1.forEach { codeMap[it.symbol]?.append("1") }

            divide(group0)
            divide(group1)
        }

        divide(sorted)

        return sorted.map {
            CodeWord(
                symbol = it.symbol,
                probability = it.probability,
                code = codeMap[it.symbol]?.toString().orEmpty()
            )
        }
    }
}