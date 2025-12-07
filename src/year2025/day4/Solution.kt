package year2025.day4

import util.getInput
import util.model.Matrix
import util.model.Position

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
    val matrix = Matrix.fromInput(input)

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
