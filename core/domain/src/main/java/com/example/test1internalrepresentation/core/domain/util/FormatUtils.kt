package com.example.test1internalrepresentation.core.domain.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object FormatUtils {
    private val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
    }
    private val byteFormatter = DecimalFormat("#,###", symbols)

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

    fun formatShortSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes Б"
            bytes < 1024 * 1024 -> "%.1f КБ".format(bytes / 1024.0)
            else -> "%.2f МБ".format(bytes / (1024.0 * 1024.0))
        }
    }

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
