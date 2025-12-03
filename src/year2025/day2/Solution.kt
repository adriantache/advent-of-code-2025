package year2025.day2

import util.getInput

/**
 * Solution for https://adventofcode.com/2025/day/2
 */
fun main() {
    val input = getInput(2, 2025)
//    val input = example

    solution1(input)
    solution2(input)
}

private fun solution1(input: List<String>) {
    val ranges = input.first().split(",").map { range ->
        val (start, end) = range.split("-").map { it.toLong() }

        start..end
    }

    val result: Long = ranges.sumOf { validateRange(it).sum() }

    println("Solution 1: $result")
}

private fun solution2(input: List<String>) {
    val ranges = input.first().split(",").map { range ->
        val (start, end) = range.split("-").map { it.toLong() }

        start..end
    }

    val result: Long = ranges.sumOf {
        validateRange(it, acceptMultiples = true).sum()
    }

    println("Solution 2: $result")
}

private fun validateRange(
    range: LongRange,
    acceptMultiples: Boolean = false,
): List<Long> {
    return range.filterNot {
        if (!acceptMultiples) {
            validateNumber(it.toString(), length = it.toString().length / 2)
        } else {
            validateAllNumberParts(it.toString())
        }
    }
}

private fun validateNumber(numberString: String, length: Int): Boolean {
    val partStrings = numberString.splitIntoParts(length)

    return !partStrings.areAllEqual()
}

private fun validateAllNumberParts(numberString: String): Boolean {
    for (length in 1..numberString.length / 2) {
        if (!validateNumber(numberString, length)) return false
    }

    return true
}

private fun String.splitIntoParts(length: Int): List<String> {
    if (length == 0) return emptyList()

    var remainder = this
    val result = mutableListOf<String>()

    while (remainder.isNotEmpty()) {
        result += remainder.take(length)
        remainder = remainder.drop(length)
    }

    return result
}

private fun List<String>.areAllEqual(): Boolean {
    if (this.isEmpty()) return false

    var previousElement = this[0]

    for (i in 1 until this.size) {
        if (this[i] != previousElement) return false
        previousElement = this[i]
    }

    return true
}

@Suppress("unused")
private val example = """
   11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
""".trimIndent().split("\n")
