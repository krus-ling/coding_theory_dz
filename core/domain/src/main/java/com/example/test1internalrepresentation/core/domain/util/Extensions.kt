package com.example.test1internalrepresentation.core.domain.util

import java.util.Locale

fun Double.formatDecimal(decimals: Int = 4): String {
    return String.format(Locale.US, "%.${decimals}f", this)
}
