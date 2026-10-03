package com.example.test1internalrepresentation

import kotlin.math.abs

fun solveTask1WithSteps(a: Int, b: Int): Task1Result {

    val sum = a + b
    val maxValue = maxOf(abs(a), abs(b), abs(a + b))
    val minBitsNeeded = maxValue.toString(2).length + 1
    val targetBits = when {
        minBitsNeeded <= 2 -> 2
        minBitsNeeded <= 4 -> 4
        minBitsNeeded <= 8 -> 8
        minBitsNeeded <= 16 -> 16
        else -> 32
    }

    val stepsList = buildList {
        val sum = a + b
        add("Задание 1: $a + $b")
        add("В десятичной: $a₁₀ + $b₁₀ = $sum₁₀")



        add("Выбранная разрядная сетка: $targetBits бит\n")

        // Обработка первого операнда
        add("1. Переводим |$a| в двоичную систему: ${conversionToBinary(a)}₂")
        val paddedA = fillInTheMissingDigits(conversionToBinary(a), targetBits)
        add("2. Представляем в $targetBits-битной сетке: $paddedA")
        val binA = if (a < 0) {
            val inverted = reverseStroke(paddedA)
            val complement = addOneInBinary(inverted, targetBits)
            add("3. Обратный ход и +1: $inverted + 1 = $complement₂")
            complement
        } else {
            paddedA
        }

        // Обработка второго операнда
        add("\n4. Переводим |$b| в двоичную систему: ${conversionToBinary(b)}₂")
        val paddedB = fillInTheMissingDigits(conversionToBinary(b), targetBits)
        add("5. Представляем в $targetBits-битной сетке: $paddedB")
        val binB = if (b < 0) {
            val inverted = reverseStroke(paddedB)
            val complement = addOneInBinary(inverted, targetBits)
            add("Обратный ход и +1: $inverted + 1 = $complement₂")
            complement
        } else {
            paddedB
        }

        // Сложение
        val resultBin = sumInBinary(a, b, targetBits)
        add("\n6. Суммируем $a₁₀ и $b₁₀:")
        add("  $binA ($a)")
        add("+ $binB ($b)")
        add("  ${"-".repeat(targetBits)}")
        add("  $resultBin₂ ($sum)")
    }

    val resultBin = sumInBinary(a, b, targetBits)
    val verificationList = generateVerificationStep(resultBin, sum)

    return Task1Result(
        decimalResult = sum,
        binaryResult = resultBin,
        steps = stepsList,
        verification = verificationList
    )
}

private val superscripts = listOf("⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸")

fun generateVerificationStep(resultBin: String, expectedDecimal: Int): List<String> = buildList {
    val isNegative = resultBin.startsWith("1")
    val targetBits = resultBin.length

    if (!isNegative) {
        add("1. Проверка: старший бит 0, число положительное.")
        add("Результат: ${resultBin.toInt(2)}₁₀")
        return@buildList
    }

    val inverted = reverseStroke(resultBin)
    val magnitudeBin = addOneInBinary(inverted, targetBits)

    add("1. Определение знака и нахождение модуля:")
    add("Старший бит равен 1 — результат отрицательный (в дополнительном коде).")
    add("• Инвертируем биты: $resultBin = $inverted₂")
    add("• Прибавляем 1: $inverted₂ + 1 = $magnitudeBin₂")

    // Формируем разложение по степеням двойки: "0⋅2⁷ + 0⋅2⁶ + 1⋅2⁵..."
    val powersExpansion = magnitudeBin.mapIndexed { index, bit ->
        val power = targetBits - 1 - index
        val powerStr = if (power < superscripts.size) superscripts[power] else "^$power"
        "$bit⋅2$powerStr"
    }.joinToString(" + ")

    // Формируем слагаемые только ненулевых степеней: "32 + 16 + 8 + 4 + 2"
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

private fun conversionToBinary(a: Int): String {
    return abs(a).toString(2)
}

private fun fillInTheMissingDigits(digit: String, targetBits: Int): String {
    return digit.padStart(targetBits, '0')
}

private fun reverseStroke(digit: String): String {
    return digit
        .map { char ->
            if (char == '1') '0' else '1'
        }.joinToString("")
}

private fun addOneInBinary(digit: String, targetBits: Int): String {
    return (digit.toInt(2) + 1)
        .toString(2)
        .padStart(targetBits, '0')
}

private fun toTwosComplement(value: Int, targetBits: Int): String {

    return if (value < 0) {
        addOneInBinary(
            digit = reverseStroke(
                fillInTheMissingDigits(
                    digit = conversionToBinary(value),
                    targetBits = targetBits
                )
            ),
            targetBits = targetBits
        )
    } else {
        fillInTheMissingDigits(
            digit = conversionToBinary(value),
            targetBits = targetBits
        )
    }
}

private fun sumInBinary(digit1: Int, digit2: Int, targetBits: Int): String {
    return (
            toTwosComplement(digit1, targetBits).toLong(2) +
            toTwosComplement(digit2, targetBits).toLong(2)
            )
        .toString(2)
        .takeLast(targetBits)
        .padStart(targetBits, '0')
}