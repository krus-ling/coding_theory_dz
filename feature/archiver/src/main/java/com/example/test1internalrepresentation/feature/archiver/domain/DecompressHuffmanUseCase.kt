package com.example.test1internalrepresentation.feature.archiver.domain

import com.example.test1internalrepresentation.core.domain.util.BitInputStream
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.nio.charset.StandardCharsets
import java.util.PriorityQueue

class DecompressHuffmanUseCase {

    private data class Node(
        val byteVal: Byte? = null,
        val freq: Int,
        val left: Node? = null,
        val right: Node? = null
    ) : Comparable<Node> {
        val isLeaf: Boolean get() = left == null && right == null
        override fun compareTo(other: Node): Int = this.freq.compareTo(other.freq)
    }

    operator fun invoke(compressedBytes: ByteArray, archiveFileName: String = "archive.huff"): DecompressionReport {
        if (compressedBytes.isEmpty()) {
            return DecompressionReport(
                archiveFileName = archiveFileName,
                restoredFileName = "restored_file.bin",
                archiveSizeBytes = 0,
                restoredSizeBytes = 0,
                alphabetSize = 0,
                restoredData = ByteArray(0),
                sampleHex = ""
            )
        }

        val byteInput = ByteArrayInputStream(compressedBytes)
        val dataIn = DataInputStream(byteInput)

        val sig1 = dataIn.readByte().toInt().toChar()
        val sig2 = dataIn.readByte().toInt().toChar()
        require(sig1 == 'H' && sig2 == 'F') { "Некорректный формат архива: неверная сигнатура 'HF'" }

        val nameLength = dataIn.readUnsignedShort()
        val nameBytes = ByteArray(nameLength)
        dataIn.readFully(nameBytes)
        val restoredFileName = String(nameBytes, StandardCharsets.UTF_8).ifEmpty { "restored_file.bin" }

        val originalSize = dataIn.readInt()
        if (originalSize == 0) {
            return DecompressionReport(
                archiveFileName = archiveFileName,
                restoredFileName = restoredFileName,
                archiveSizeBytes = compressedBytes.size.toLong(),
                restoredSizeBytes = 0,
                alphabetSize = 0,
                restoredData = ByteArray(0),
                sampleHex = ""
            )
        }

        val alphabetSize = dataIn.readInt()
        val frequencies = HashMap<Byte, Int>()
        for (i in 0 until alphabetSize) {
            val byteVal = dataIn.readByte()
            val flag = dataIn.readByte().toInt()
            val freq = when (flag) {
                0 -> dataIn.readByte().toInt() and 0xFF
                1 -> dataIn.readUnsignedShort()
                2 -> dataIn.readInt()
                else -> dataIn.readInt()
            }
            frequencies[byteVal] = freq
        }

        val pq = PriorityQueue<Node>()
        for ((byteVal, freq) in frequencies) {
            pq.add(Node(byteVal = byteVal, freq = freq))
        }

        if (pq.size == 1) {
            val single = pq.poll()!!
            pq.add(Node(freq = single.freq, left = single, right = null))
        }

        while (pq.size > 1) {
            val left = pq.poll()!!
            val right = pq.poll()!!
            pq.add(Node(freq = left.freq + right.freq, left = left, right = right))
        }

        val root = pq.peek()!!
        val restoredData = ByteArray(originalSize)
        val bitInput = BitInputStream(byteInput)

        var decodedCount = 0
        while (decodedCount < originalSize) {
            var curr: Node? = root
            while (curr != null && !curr.isLeaf) {
                val bit = bitInput.readBit()
                if (bit == -1) break
                curr = if (bit == 0) curr.left else curr.right
            }

            if (curr != null && curr.isLeaf && curr.byteVal != null) {
                restoredData[decodedCount] = curr.byteVal
                decodedCount++
            } else {
                break
            }
        }

        val sampleHex = restoredData.take(32).joinToString(" ") { "%02X".format(it) }

        return DecompressionReport(
            archiveFileName = archiveFileName,
            restoredFileName = restoredFileName,
            archiveSizeBytes = compressedBytes.size.toLong(),
            restoredSizeBytes = restoredData.size.toLong(),
            alphabetSize = alphabetSize,
            restoredData = restoredData,
            sampleHex = sampleHex
        )
    }
}
