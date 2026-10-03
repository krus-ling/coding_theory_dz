package com.example.test1internalrepresentation.domain.model

data class CodingMethodResult(
    val methodName: String,
    val codes: List<CodeWord>,
    val averageLength: Double,
    val compressionRatio: Double,   // Kcc
    val relativeEfficiency: Double  // Koe
)