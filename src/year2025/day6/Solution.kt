package year2025.day6

import util.getInput

/**
 * Solution for https://adventofcode.com/2025/day/6
 */
fun main() {
    val input = getInput(6, 2025)
//    val input = example

    solution1(input)
    solution2(input)
}

private fun solution1(input: List<String>) {
    val operations = input.last().split(" ").filterNot { it.isBlank() }.map { Operation.fromSymbol(it) }
    val values = input.dropLast(1).map { list ->
        list.split(" ").filterNot { it.isBlank() }.map { it.toLong() }
    }

    val finalValues = mutableMapOf<Int, List<Long>>()

    repeat(operations.size) { index ->
        values.forEach {
            finalValues[index] = (finalValues[index] ?: emptyList()) + it[index]
        }
    }

    val result = applyOperations(operations, finalValues)

    println("Solution 1: $result")
}

private fun applyOperations(
    operations: List<Operation>,
    finalValues: Map<Int, List<Long>>
): Long {
    val results = operations.mapIndexed { index, operation ->
        val values = finalValues[index]!!
        operation.operation(values)
    }

    return results.sum()
}

private fun solution2(input: List<String>) {
    val lastLine = input.last().split("").filterNot { it.isEmpty() }
    val operationIndexes = lastLine.getOperationIndexes()
    val operations = operationIndexes.map { lastLine[it] }.map { Operation.fromSymbol(it) }

    val values = input.dropLast(1).map { it.groupWithOperationIndexes(operationIndexes) }
    val transposedValues = values.transpose()
    val verticalNumbers = transposedValues.map { it.getVerticalNumbers() }

    val result = operations.mapIndexed { index, operation ->
        val values = verticalNumbers[index]
        operation.operation(values)
    }.sum()

    println("Solution 2: $result")
}

private fun List<String>.getOperationIndexes(): List<Int> {
    val indexes = mutableListOf<Int>()

    this.forEachIndexed { index, string -> if (string != " ") indexes += index }

    return indexes
}

private fun String.groupWithOperationIndexes(operationIndexes: List<Int>): List<String> {
    val result = mutableListOf<String>()

    operationIndexes.forEachIndexed { index, operationIndex ->
        if (index == operationIndexes.size - 1) {
            val substring = this.substring(operationIndex)
            result += substring

            return@forEachIndexed
        }

        // We go to the space before the previous operation, exclusive.
        val endIndex = operationIndexes[index + 1] - 1
        val substring = this.substring(operationIndex, endIndex)
        result += substring
    }

    return result
}

private fun List<List<String>>.transpose(): List<List<String>> {
    return List(this.first().size) { index ->
        val result = mutableListOf<String>()

        this.forEach { result += it[index] }

        result
    }
}

private fun List<String>.getVerticalNumbers(): List<Long> {
    val result = mutableMapOf<Int, String>()

    repeat(this.first().length) { index ->
        this.forEach {
            result[index] = result[index].orEmpty() + it[index]
        }
    }

    return result.values.map { it.trim().toLong() }.reversed()
}


private enum class Operation(val operation: (List<Long>) -> Long) {
    Addition({ it.sum() }),
    Multiplication({ it.reduce { acc, long -> acc * long } });

    companion object {
        fun fromSymbol(symbol: String): Operation {
            return when (symbol) {
                "+" -> Addition
                "*" -> Multiplication
                else -> error("Unknown symbol: [$symbol]")
            }
        }
    }
}

@Suppress("unused")
private val example = """
   123 328  51 64 
    45 64  387 23 
     6 98  215 314
   *   +   *   + 
""".trimIndent().split("\n")
