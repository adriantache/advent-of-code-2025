package year2025.day4

import util.getInput

/**
 * Solution for https://adventofcode.com/2025/day/4
 */
fun main() {
    val input = getInput(4, 2025)
//    val input = example

    solution1(input)
    solution2(input)
}

private fun solution1(input: List<String>) {
    val matrix = Matrix(input.filterNot { it.isBlank() }
        .map { it.trim().split("") }
        .map { list -> list.filterNot { it.isBlank() } })

    val accessibleRolls = matrix.getAccessibleRolls()

    val result = accessibleRolls.count()

    println("Solution 1: $result")
}

private fun Matrix.getAccessibleRolls(): List<Position> {
    val result = mutableListOf<Position>()

    this.forEachIndexed { position, element ->
        if (element != "@") return@forEachIndexed

        if (this.checkAdjacentRolls(position)) result += position
    }

    return result
}

private fun Matrix.checkAdjacentRolls(position: Position): Boolean {
    var counter = 0

    val startX = position.x - 1
    val endX = position.x + 1
    val startY = position.y - 1
    val endY = position.y + 1

    for (x in startX..endX) {
        for (y in startY..endY) {
            if (position.equals(x, y)) continue

            val element = get(x, y) ?: continue

            if (element == "@") {
                if (counter == 3) {
                    return false
                } else {
                    counter++
                }
            }
        }
    }

    return counter < 4
}

private fun solution2(input: List<String>) {
    val matrix = Matrix(input.filterNot { it.isBlank() }
        .map { it.trim().split("") }
        .map { list -> list.filterNot { it.isBlank() } })

    var rolls = 0

    while (true) {
        val removedRolls = matrix.getAccessibleRolls()
        matrix.updateRemovedRolls(removedRolls)
        rolls += removedRolls.size

        if (removedRolls.isEmpty()) break
    }

    println("Solution 2: $rolls")
}

private fun Matrix.updateRemovedRolls(removedRolls: List<Position>) {
    removedRolls.forEach {
        this.set(it, ".")
    }
}

// Adapted from 2024/4
private class Matrix(private var input: List<List<String>>) {
    fun get(x: Int, y: Int) = if (isValidCoordinate(x, y)) input[y][x] else null

    fun getY(y: Int) = if (isYValid(y)) input[y] else null

    fun set(position: Position, value: String) {
        input = input.toMutableList().apply {
            val yColumn = this[position.y].toMutableList()
            yColumn[position.x] = value
            this[position.y] = yColumn
        }
    }

    private fun isValidCoordinate(x: Int, y: Int) = isYValid(y) && isXValid(x, y)

    fun forEachIndexed(onElement: (Position, String) -> Unit) {
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, element ->
                onElement(Position(x, y), element)
            }
        }
    }

    private fun isXValid(x: Int, y: Int, position: Position? = null): Boolean {
        val yValue = getY(y)

        return when {
            x < 0 -> false
            yValue == null -> false
            x >= yValue.size -> false
            position?.x == x -> false
            else -> true
        }
    }

    private fun isYValid(y: Int, position: Position? = null): Boolean {
        return when {
            y < 0 -> false
            y >= input.size -> false
            position?.y == y -> false
            else -> true
        }
    }

    override fun toString(): String {
        return input.joinToString("\n")
    }
}

private data class Position(val position: Pair<Int, Int>) {
    constructor(x: Int, y: Int) : this(x to y)

    val x = position.first
    val y = position.second

    fun equals(x: Int, y: Int) = this.x == x && this.y == y
}

@Suppress("unused")
private val example = """
   ..@@.@@@@.
   @@@.@.@.@@
   @@@@@.@.@@
   @.@@@@..@.
   @@.@@@@.@@
   .@@@@@@@.@
   .@.@.@.@@@
   @.@@@.@@@@
   .@@@@@@@@.
   @.@.@@@.@.
""".trimIndent().split("\n")
