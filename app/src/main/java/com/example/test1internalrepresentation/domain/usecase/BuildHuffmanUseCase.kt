package com.example.test1internalrepresentation.domain.usecase

import com.example.test1internalrepresentation.domain.model.CodeWord
import com.example.test1internalrepresentation.domain.model.SymbolProbability
import java.util.PriorityQueue

class BuildHuffmanUseCase {

    private sealed class Node(val weight: Double) : Comparable<Node> {
        override fun compareTo(other: Node): Int = this.weight.compareTo(other.weight)

        class Leaf(val symbol: String, weight: Double) : Node(weight)
        class Internal(val left: Node, val right: Node) : Node(left.weight + right.weight)
    }

    fun execute(symbols: List<SymbolProbability>): List<CodeWord> {
        if (symbols.isEmpty()) return emptyList()
        if (symbols.size == 1) {
            return listOf(CodeWord(
                symbols[0].symbol,
                symbols[0].probability,
                "0"
            ))
        }

        val queue = PriorityQueue<Node>()
        symbols.forEach { queue.add(Node.Leaf(it.symbol, it.probability)) }

        while (queue.size > 1) {
            val left = queue.poll()
            val right = queue.poll()
            queue.add(Node.Internal(left, right))
        }

        val root = queue.poll()
        val codeMap = mutableMapOf<String, String>()

        fun traverse(node: Node, prefix: String) {
            when (node) {
                is Node.Leaf -> codeMap[node.symbol] = prefix
                is Node.Internal -> {
                    traverse(node.left, prefix = "0")
                    traverse(node.right, prefix = "1")
                }
            }
        }

        traverse(root, "")

        return symbols.sortedByDescending { it.probability }.map {
            CodeWord(
                symbol = it.symbol,
                probability = it.probability,
                code = codeMap[it.symbol].orEmpty()
            )
        }
    }
}