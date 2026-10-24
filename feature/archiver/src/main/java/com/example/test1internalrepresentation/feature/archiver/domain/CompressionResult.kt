package com.example.test1internalrepresentation.feature.archiver.domain

/**
 * Отчет с результатами сжатия файла алгоритмом Хаффмана.
 *
 * @property originalFileName Исходное имя и расширение файла.
 * @property originalSizeBytes Объем исходных данных V₁ в байтах.
 * @property compressedSizeBytes Объем упакованного архива V₂ в байтах (заголовок + тело).
 * @property headerSizeBytes Размер служебного заголовка в байтах.
 * @property compressionRatio Коэффициент сжатия K_сж = V₁ / V₂.
 * @property spaceSavingPercent Процент сэкономленного пространства.
 * @property compressedData Упакованный массив байт.
 * @property sampleHex HEX-дамп первых байт архива.
 * @property codesMap Карта построения байтовых кодов Хаффмана.
 */
data class CompressionReport(
    val originalFileName: String,
    val originalSizeBytes: Long,
    val compressedSizeBytes: Long,
    val headerSizeBytes: Int,
    val compressionRatio: Double,
    val spaceSavingPercent: Double,
    val compressedData: ByteArray,
    val sampleHex: String,
    val codesMap: Map<Byte, String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompressionReport

        if (originalSizeBytes != other.originalSizeBytes) return false
        if (compressedSizeBytes != other.compressedSizeBytes) return false
        if (headerSizeBytes != other.headerSizeBytes) return false
        if (compressionRatio != other.compressionRatio) return false
        if (spaceSavingPercent != other.spaceSavingPercent) return false
        if (originalFileName != other.originalFileName) return false
        if (!compressedData.contentEquals(other.compressedData)) return false
        if (sampleHex != other.sampleHex) return false
        if (codesMap != other.codesMap) return false

        return true
    }

    override fun hashCode(): Int {
        var result = originalSizeBytes.hashCode()
        result = 31 * result + compressedSizeBytes.hashCode()
        result = 31 * result + headerSizeBytes
        result = 31 * result + compressionRatio.hashCode()
        result = 31 * result + spaceSavingPercent.hashCode()
        result = 31 * result + originalFileName.hashCode()
        result = 31 * result + compressedData.contentHashCode()
        result = 31 * result + sampleHex.hashCode()
        result = 31 * result + codesMap.hashCode()
        return result
    }
}

/**
 * Отчет с результатами деархивации и распаковки файла `.huff`.
 *
 * @property archiveFileName Имя файла архива.
 * @property restoredFileName Исходное восстановленное имя файла с родным расширением.
 * @property archiveSizeBytes Объем сжатого архива V₂ в байтах.
 * @property restoredSizeBytes Восстановленный объем V₁ в байтах.
 * @property alphabetSize Мощность байтового алфавита N.
 * @property restoredData Восстановленный массив байт.
 * @property sampleHex HEX-дамп первых восстановленных байт.
 */
data class DecompressionReport(
    val archiveFileName: String,
    val restoredFileName: String,
    val archiveSizeBytes: Long,
    val restoredSizeBytes: Long,
    val alphabetSize: Int,
    val restoredData: ByteArray,
    val sampleHex: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DecompressionReport

        if (archiveSizeBytes != other.archiveSizeBytes) return false
        if (restoredSizeBytes != other.restoredSizeBytes) return false
        if (alphabetSize != other.alphabetSize) return false
        if (archiveFileName != other.archiveFileName) return false
        if (restoredFileName != other.restoredFileName) return false
        if (!restoredData.contentEquals(other.restoredData)) return false
        if (sampleHex != other.sampleHex) return false

        return true
    }

    override fun hashCode(): Int {
        var result = archiveSizeBytes.hashCode()
        result = 31 * result + restoredSizeBytes.hashCode()
        result = 31 * result + alphabetSize
        result = 31 * result + archiveFileName.hashCode()
        result = 31 * result + restoredFileName.hashCode()
        result = 31 * result + restoredData.contentHashCode()
        result = 31 * result + sampleHex.hashCode()
        return result
    }
}
