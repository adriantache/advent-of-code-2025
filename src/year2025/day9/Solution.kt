package year2025.day9

import util.model.Position
import kotlin.math.absoluteValue

/**
 * Solution for https://adventofcode.com/2025/day/9
 */
fun main() {
//    val input = getInput(9, 2025)
    val input = example

    println()
    solution1(input)
    println()
    solution2(input)
    println()
}

private fun solution1(input: List<String>) {
    val redTiles = input.map { row ->
        val (x, y) = row.split(",").map { it.trim().toInt() }
        Position(x, y)
    }

    val rectangles = redTiles.findRectangles()

    val maxRectangle = rectangles.maxBy { it.area }
    val sorted = rectangles.sortedBy { it.area }.reversed()
    val result = maxRectangle.area

    println("Solution 1: $result")
}

private fun List<Position>.findRectangles(): List<Rectangle> {
    val rectangles = mutableSetOf<Rectangle>()

    this.forEach { first ->
        this.forEach { second ->
            if (first == second) return@forEach

            rectangles.add(Rectangle(first, second))
        }
    }

    return rectangles.toList()
}

private data class Rectangle(val start: Position, val end: Position) {
    val area = ((start.x - end.x).absoluteValue + 1).toLong() * ((start.y - end.y).absoluteValue + 1)

    override fun equals(other: Any?) = when (other) {
        null -> false
        !is Rectangle -> false
        else -> (start == other.start && end == other.end) || (start == other.end && end == other.start)
    }

    override fun hashCode() = start.hashCode() + end.hashCode()

    override fun toString(): String {
        return "$start $end Area: $area"
    }
}

private fun solution2(input: List<String>) {

    println("Solution 2: ")
}

@Suppress("unused")
private val example = """
   7,1
   11,1
   11,7
   9,7
   9,5
   2,5
   2,3
   7,3
""".trimIndent().split("\n")
