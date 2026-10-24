package com.example.test1internalrepresentation.core.domain.util

import java.io.InputStream
import java.io.OutputStream

/**
 * Класс для побитовой записи данных в выходной поток байт.
 *
 * @property output Выходной поток [OutputStream].
 */
class BitOutputStream(private val output: OutputStream) : AutoCloseable {
    private var currentByte = 0
    private var numBits = 0

    /**
     * Записывает один бит (0 или 1) в буфер.
     *
     * @param bit Значение бита (0 или 1).
     */
    fun writeBit(bit: Int) {
        currentByte = (currentByte shl 1) or (bit and 1)
        numBits++
        if (numBits == 8) {
            output.write(currentByte)
            currentByte = 0
            numBits = 0
        }
    }

    /**
     * Записывает последовательность бит, заданную строкой из символов '0' и '1'.
     *
     * @param bitString Двоичная строка.
     */
    fun writeBits(bitString: String) {
        for (ch in bitString) {
            when (ch) {
                '0' -> writeBit(0)
                '1' -> writeBit(1)
            }
        }
    }

    /**
     * Сбрасывает оставшиеся накопленные биты в поток с дополнением нулями до полного байта.
     */
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

/**
 * Класс для побитового чтения данных из входного потока байт.
 *
 * @property input Входной поток [InputStream].
 */
class BitInputStream(private val input: InputStream) : AutoCloseable {
    private var currentByte = 0
    private var numBits = 0

    /**
     * Считывает один бит из потока.
     *
     * @return Значение бита (0 или 1) либо -1 при достижении конца потока.
     */
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
