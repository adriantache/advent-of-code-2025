package year2025.day5

import util.getInput

/**
 * Solution for https://adventofcode.com/2025/day/5
 */
fun main() {
    val input = getInput(5, 2025, true)
//    val input = example

    solution1(input)
    solution2(input)
}

private fun solution1(input: List<String>) {
    val splitIndex = input.indexOfFirst { it.isBlank() }
    val ranges = input.subList(0, splitIndex)
        .filterNot { it.isBlank() }
        .map {
            val (start, end) = it.split("-")

            start.trim().toLong()..end.trim().toLong()
        }
    val ingredients = input.subList(splitIndex + 1, input.size)
        .filterNot { it.isBlank() }
        .map { it.trim().toLong() }

    val result = ingredients.count { ranges.any { range -> range.contains(it) } }

    println("Solution 1: $result")
}

private fun solution2(input: List<String>) {
    val splitIndex = input.indexOfFirst { it.isBlank() }
    val ranges = input.subList(0, splitIndex)
        .filterNot { it.isBlank() }
        .map {
            val (start, end) = it.split("-")

            Range(start.trim().toLong(), end.trim().toLong())
        }

    val filteredRanges = reduceRanges(ranges)

    val result = filteredRanges.sumOf { it.count() }

    println("Solution 2: $result")
}

private fun reduceRanges(input: List<Range>): List<Range> {
    lateinit var overlap: Range
    val range = input.find { range ->
        range.findOverlap(input)?.also { overlap = it } != null
    } ?: return input

    val newList = input.toMutableList()
    newList.remove(range)
    newList.remove(overlap)

    val newRange = Range(minOf(range.start, overlap.start), maxOf(range.end, overlap.end))
    newList.add(newRange)

    return reduceRanges(newList)
}

private fun Range.findOverlap(input: List<Range>): Range? {
    val comparison = input.toMutableList()
    comparison.remove(this)

    return comparison.find { it.contains(start) || it.contains(end) }
}

private data class Range(
    val start: Long,
    val end: Long,
) {
    fun toLongRange() = start..end

    fun contains(value: Long) = value in toLongRange()

    fun count() = end - start + 1
}

@Suppress("unused")
private val example = """
   3-5
   10-14
   16-20
   12-18

   1
   5
   8
   11
   17
   32
""".trimIndent().split("\n")
