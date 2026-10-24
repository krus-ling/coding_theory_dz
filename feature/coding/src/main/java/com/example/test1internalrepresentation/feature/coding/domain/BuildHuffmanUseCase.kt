package com.example.test1internalrepresentation.feature.coding.domain

import com.example.test1internalrepresentation.core.domain.model.CodeWord
import com.example.test1internalrepresentation.core.domain.model.SymbolProbability
import java.util.PriorityQueue

class BuildHuffmanUseCase {

    private data class Node(
        val symbol: String? = null,
        val probability: Double,
        val left: Node? = null,
        val right: Node? = null
    ) : Comparable<Node> {
        val isLeaf: Boolean get() = left == null && right == null
        override fun compareTo(other: Node): Int = this.probability.compareTo(other.probability)
    }

    fun execute(symbols: List<SymbolProbability>): List<CodeWord> {
        if (symbols.isEmpty()) return emptyList()

        val sorted = symbols.sortedByDescending { it.probability }

        val pq = PriorityQueue<Node>()
        for (sp in sorted) {
            pq.add(Node(symbol = sp.symbol, probability = sp.probability))
        }

        if (pq.size == 1) {
            val single = pq.poll()!!
            pq.add(Node(probability = single.probability, left = single, right = null))
        }

        while (pq.size > 1) {
            val left = pq.poll()!!
            val right = pq.poll()!!
            val parent = Node(
                probability = left.probability + right.probability,
                left = left,
                right = right
            )
            pq.add(parent)
        }

        val root = pq.peek()
        val codeMap = HashMap<String, String>()
        buildCodes(root, "", codeMap)

        return sorted.map { sp ->
            CodeWord(
                symbol = sp.symbol,
                probability = sp.probability,
                code = codeMap[sp.symbol] ?: ""
            )
        }
    }

    private fun buildCodes(node: Node?, prefix: String, outMap: MutableMap<String, String>) {
        if (node == null) return
        if (node.isLeaf && node.symbol != null) {
            outMap[node.symbol] = if (prefix.isEmpty()) "0" else prefix
            return
        }
        buildCodes(node.left, prefix + "0", outMap)
        buildCodes(node.right, prefix + "1", outMap)
    }
}
