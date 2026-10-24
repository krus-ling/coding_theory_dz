package com.example.test1internalrepresentation.feature.coding.domain

import com.example.test1internalrepresentation.core.domain.model.CodeWord

data class CodingMethodResult(
    val methodName: String,
    val codes: List<CodeWord>,
    val averageLength: Double,
    val compressionRatio: Double,
    val relativeEfficiency: Double
)
