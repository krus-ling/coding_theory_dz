package com.example.test1internalrepresentation.feature.archiver.domain

import com.example.test1internalrepresentation.core.domain.util.BitOutputStream
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.nio.charset.StandardCharsets
import java.util.PriorityQueue

/**
 * UseCase алгоритма побитового сжатия произвольного файла методом Хаффмана.
 * Записывает компактный служебный заголовок с сигнатурой 'HF', исходным именем файла,
 * объемом данных V₁ и байтовой таблицей частот.
 */
class CompressHuffmanUseCase {

    private data class Node(
        val byteVal: Byte? = null,
        val freq: Int,
        val left: Node? = null,
        val right: Node? = null
    ) : Comparable<Node> {
        val isLeaf: Boolean get() = left == null && right == null
        override fun compareTo(other: Node): Int = this.freq.compareTo(other.freq)
    }

    /**
     * Сжимает входной массив байт и возвращает отчет [CompressionReport].
     *
     * @param inputBytes Массив исходных байт.
     * @param fileName Исходное имя файла с расширением.
     */
    operator fun invoke(inputBytes: ByteArray, fileName: String = "data.bin"): CompressionReport {
        if (inputBytes.isEmpty()) {
            return CompressionReport(
                originalFileName = fileName,
                originalSizeBytes = 0,
                compressedSizeBytes = 0,
                headerSizeBytes = 0,
                compressionRatio = 1.0,
                spaceSavingPercent = 0.0,
                compressedData = ByteArray(0),
                sampleHex = "",
                codesMap = emptyMap()
            )
        }

        val frequencies = HashMap<Byte, Int>()
        for (b in inputBytes) {
            frequencies[b] = (frequencies[b] ?: 0) + 1
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
            val parent = Node(
                freq = left.freq + right.freq,
                left = left,
                right = right
            )
            pq.add(parent)
        }

        val root = pq.peek()
        val codes = HashMap<Byte, String>()
        buildCodeTable(root, "", codes)

        val byteStream = ByteArrayOutputStream()
        val dataOut = DataOutputStream(byteStream)

        dataOut.writeByte('H'.code)
        dataOut.writeByte('F'.code)

        val nameBytes = fileName.toByteArray(StandardCharsets.UTF_8)
        dataOut.writeShort(nameBytes.size)
        dataOut.write(nameBytes)

        dataOut.writeInt(inputBytes.size)
        dataOut.writeInt(frequencies.size)

        for ((byteVal, freq) in frequencies) {
            dataOut.writeByte(byteVal.toInt())
            when {
                freq <= 255 -> {
                    dataOut.writeByte(0)
                    dataOut.writeByte(freq)
                }
                freq <= 65535 -> {
                    dataOut.writeByte(1)
                    dataOut.writeShort(freq)
                }
                else -> {
                    dataOut.writeByte(2)
                    dataOut.writeInt(freq)
                }
            }
        }
        dataOut.flush()
        val headerSize = byteStream.size()

        val bitOut = BitOutputStream(byteStream)
        for (b in inputBytes) {
            val code = codes[b] ?: error("Код Хаффмана для байта $b не найден")
            bitOut.writeBits(code)
        }
        bitOut.flushBits()

        val compressedData = byteStream.toByteArray()
        val origSize = inputBytes.size.toLong()
        val compSize = compressedData.size.toLong()

        val ratio = if (compSize > 0) origSize.toDouble() / compSize.toDouble() else 1.0
        val saving = if (origSize > 0) ((origSize - compSize).toDouble() / origSize.toDouble()) * 100.0 else 0.0

        val sampleHex = compressedData.take(32).joinToString(" ") { "%02X".format(it) }

        return CompressionReport(
            originalFileName = fileName,
            originalSizeBytes = origSize,
            compressedSizeBytes = compSize,
            headerSizeBytes = headerSize,
            compressionRatio = ratio,
            spaceSavingPercent = saving,
            compressedData = compressedData,
            sampleHex = sampleHex,
            codesMap = codes
        )
    }

    private fun buildCodeTable(node: Node?, currentCode: String, outMap: HashMap<Byte, String>) {
        if (node == null) return
        if (node.isLeaf && node.byteVal != null) {
            outMap[node.byteVal] = if (currentCode.isEmpty()) "0" else currentCode
            return
        }
        buildCodeTable(node.left, currentCode + "0", outMap)
        buildCodeTable(node.right, currentCode + "1", outMap)
    }
}
