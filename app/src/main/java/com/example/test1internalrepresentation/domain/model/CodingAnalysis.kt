package com.example.test1internalrepresentation.domain.model

data class CodingAnalysis(
    val entropy: Double,
    val maxEntropy: Double,
    val shannonFano: CodingMethodResult,
    val huffman: CodingMethodResult
)