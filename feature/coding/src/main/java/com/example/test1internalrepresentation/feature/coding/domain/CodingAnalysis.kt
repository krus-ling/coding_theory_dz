package com.example.test1internalrepresentation.feature.coding.domain

data class CodingAnalysis(
    val entropy: Double,
    val maxEntropy: Double,
    val shannonFano: CodingMethodResult,
    val huffman: CodingMethodResult
)
