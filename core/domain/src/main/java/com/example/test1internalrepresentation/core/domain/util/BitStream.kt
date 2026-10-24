package com.example.test1internalrepresentation.core.domain.util

import java.io.InputStream
import java.io.OutputStream

class BitOutputStream(private val output: OutputStream) : AutoCloseable {
    private var currentByte = 0
    private var numBits = 0

    fun writeBit(bit: Int) {
        currentByte = (currentByte shl 1) or (bit and 1)
        numBits++
        if (numBits == 8) {
            output.write(currentByte)
            currentByte = 0
            numBits = 0
        }
    }

    fun writeBits(bitString: String) {
        for (ch in bitString) {
            when (ch) {
                '0' -> writeBit(0)
                '1' -> writeBit(1)
            }
        }
    }

    fun flushBits() {
        if (numBits > 0) {
            currentByte = currentByte shl (8 - numBits)
            output.write(currentByte)
            currentByte = 0
            numBits = 0
        }
    }

    override fun close() {
        flushBits()
        output.close()
    }
}

class BitInputStream(private val input: InputStream) : AutoCloseable {
    private var currentByte = 0
    private var numBits = 0

    fun readBit(): Int {
        if (numBits == 0) {
            currentByte = input.read()
            if (currentByte == -1) return -1
            numBits = 8
        }
        numBits--
        return (currentByte ushr numBits) and 1
    }

    override fun close() {
        input.close()
    }
}
