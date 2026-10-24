package com.example.test1internalrepresentation.core.domain.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Утилиты для форматирования размеров файлов, байтовых значений и символьных меток.
 */
object FormatUtils {
    private val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
    }
    private val byteFormatter = DecimalFormat("#,###", symbols)

    /**
     * Форматирует размер файла в байтах в человекочитаемый вид (байты, КБ, МБ).
     *
     * @param bytes Размер файла в байтах.
     * @return Текстовое представление размера.
     */
    fun formatFileSize(bytes: Long): String {
        val formattedBytes = byteFormatter.format(bytes)
        return when {
            bytes < 1024 -> "$bytes Б"
            bytes < 1024 * 1024 -> {
                val kb = bytes / 1024.0
                "%.2f КБ (%s байт)".format(kb, formattedBytes)
            }
            else -> {
                val mb = bytes / (1024.0 * 1024.0)
                "%.2f МБ (%s байт)".format(mb, formattedBytes)
            }
        }
    }

    /**
     * Форматирует краткое текстовое значение размера файла.
     *
     * @param bytes Объем в байтах.
     * @return Краткая строка вида "1.8 КБ" или "450 Б".
     */
    fun formatShortSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes Б"
            bytes < 1024 * 1024 -> "%.1f КБ".format(bytes / 1024.0)
            else -> "%.2f МБ".format(bytes / (1024.0 * 1024.0))
        }
    }

    /**
     * Форматирует отображение отдельного байта в виде понятного символа или служебной метки.
     *
     * @param byteVal Значение байта.
     * @return Текстовая метка (например, "' ' (Пробел)", "'\n' (Перенос)" или "0x41").
     */
    fun formatByteLabel(byteVal: Byte): String {
        val intVal = byteVal.toInt() and 0xFF
        return when (intVal) {
            32 -> "' ' (Пробел)"
            10 -> "'\\n' (Перенос)"
            13 -> "'\\r' (Возврат)"
            9 -> "'\\t' (Табуляция)"
            in 33..126 -> "'${intVal.toChar()}'"
            else -> "0x%02X".format(byteVal)
        }
    }
}
