package com.example.test1internalrepresentation.domain.usecase

import com.example.test1internalrepresentation.domain.model.Task1Result
import kotlin.math.abs

/**
 * UseCase для расчета внутреннего представления чисел в двоичной системе счисления
 * (прямой, обратный и дополнительный код, сложение с пошаговой проверкой).
 */
class CalculateNumberRepresentationUseCase {

    private val superscripts = listOf("⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸")

    fun execute(a: Int, b: Int): Task1Result {
        val sum = a + b
        val maxValue = maxOf(abs(a), abs(b), abs(sum))
        val minBitsNeeded = maxValue.toString(2).length + 1

        val targetBits = when {
            minBitsNeeded <= 2 -> 2
            minBitsNeeded <= 4 -> 4
            minBitsNeeded <= 8 -> 8
            minBitsNeeded <= 16 -> 16
            else -> 32
        }

        val paddedA = padToBitsCount(toBinaryMagnitude(a), targetBits)
        val binA = if (a < 0) {
            val inverted = invertBits(paddedA)
            addOneInBinary(inverted, targetBits)
        } else {
            paddedA
        }

        val paddedB = padToBitsCount(toBinaryMagnitude(b), targetBits)
        val binB = if (b < 0) {
            val inverted = invertBits(paddedB)
            addOneInBinary(inverted, targetBits)
        } else {
            paddedB
        }

        val resultBin = sumInBinary(binA, binB, targetBits)

        val stepsList = buildList {
            add("Задание 1: $a + $b")
            add("В десятичной: $a₁₀ + $b₁₀ = $sum₁₀")
            add("Выбранная разрядная сетка: $targetBits бит\n")

            // Обработка первого операнда
            add("1. Переводим |$a| в двоичную систему: ${toBinaryMagnitude(a)}₂")
            add("2. Представляем в $targetBits-битной сетке: $paddedA")
            if (a < 0) {
                val invertedA = invertBits(paddedA)
                add("3. Обратный ход и +1: $invertedA + 1 = $binA₂")
            }

            // Обработка второго операнда
            add("\n4. Переводим |$b| в двоичную систему: ${toBinaryMagnitude(b)}₂")
            add("5. Представляем в $targetBits-битной сетке: $paddedB")
            if (b < 0) {
                val invertedB = invertBits(paddedB)
                add("Обратный ход и +1: $invertedB + 1 = $binB₂")
            }

            // Сложение
            add("\n6. Суммируем $a₁₀ и $b₁₀:")
            add("  $binA ($a)")
            add("+ $binB ($b)")
            add("  ${"-".repeat(targetBits)}")
            add("  $resultBin₂ ($sum)")
        }

        val verificationList = generateVerificationStep(resultBin, sum)

        return Task1Result(
            decimalResult = sum,
            binaryResult = resultBin,
            steps = stepsList,
            verification = verificationList
        )
    }

    private fun generateVerificationStep(resultBin: String, expectedDecimal: Int): List<String> = buildList {
        val isNegative = resultBin.startsWith("1")
        val targetBits = resultBin.length

        if (!isNegative) {
            add("1. Проверка: старший бит 0, число положительное.")
            add("Результат: ${resultBin.toInt(2)}₁₀")
            return@buildList
        }

        val inverted = invertBits(resultBin)
        val magnitudeBin = addOneInBinary(inverted, targetBits)

        add("1. Определение знака и нахождение модуля:")
        add("Старший бит равен 1 — результат отрицательный (в дополнительном коде).")
        add("• Инвертируем биты: $resultBin = $inverted₂")
        add("• Прибавляем 1: $inverted₂ + 1 = $magnitudeBin₂")

        // Разложение по степеням двойки
        val powersExpansion = magnitudeBin.mapIndexed { index, bit ->
            val power = targetBits - 1 - index
            val powerStr = if (power < superscripts.size) superscripts[power] else "^$power"
            "$bit⋅2$powerStr"
        }.joinToString(" + ")

        val nonZeroTerms = magnitudeBin.mapIndexedNotNull { index, bit ->
            if (bit == '1') {
                1 shl (targetBits - 1 - index)
            } else null
        }

        val calculatedMagnitude = magnitudeBin.toInt(2)

        add("\n2. Перевод модуля в десятичную систему:")
        add("$magnitudeBin₂ = $powersExpansion")
        add("= ${nonZeroTerms.joinToString(" + ")} = ${calculatedMagnitude}₁₀")

        add("\n3. Итог проверки:")
        add("С учетом знака результат равен -$calculatedMagnitude₁₀.")
        add("Ожидалось: $expectedDecimal₁₀, получено: -$calculatedMagnitude₁₀.")
        add(if (-calculatedMagnitude == expectedDecimal) "Все сходится ✔" else "Ошибка в расчетах ❌")
    }

    private fun toBinaryMagnitude(a: Int): String {
        return abs(a).toString(2)
    }

    private fun padToBitsCount(digit: String, targetBits: Int): String {
        return digit.padStart(targetBits, '0')
    }

    private fun invertBits(digit: String): String {
        return digit.map { char ->
            if (char == '1') '0' else '1'
        }.joinToString("")
    }

    private fun addOneInBinary(digit: String, targetBits: Int): String {
        return (digit.toInt(2) + 1)
            .toString(2)
            .padStart(targetBits, '0')
    }

    private fun sumInBinary(binA: String, binB: String, targetBits: Int): String {
        val sumLong = binA.toLong(2) + binB.toLong(2)
        return sumLong
            .toString(2)
            .takeLast(targetBits)
            .padStart(targetBits, '0')
    }
}
