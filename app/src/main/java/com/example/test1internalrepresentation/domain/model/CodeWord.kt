package com.example.test1internalrepresentation.domain.model

data class CodeWord(
    val symbol: String,
    val probability: Double,
    val code: String,
    val length: Int = code.length
)