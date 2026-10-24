package com.example.test1internalrepresentation.feature.representation.domain

data class Task1Result(
    val decimalResult: Int,
    val binaryResult: String,
    val steps: List<String>,
    val verification: List<String>
)
