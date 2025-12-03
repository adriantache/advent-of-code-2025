package year2024.day4

import util.getInput

/**
 * Solution for https://adventofcode.com/2024/day/4
 */
fun main() {
    val input = getInput(4, 2024)
//    val input = example

    solution1(input)
    solution2(input)
}

private fun solution1(input: List<String>) {
    val matrix = Matrix(input.map { it.split("") })
    val xOccurrences = matrix.indexOfAll("X")
    val mOccurrences = matrix.getAllNeighbours1("M", xOccurrences)
    val aOccurrences = matrix.getAllNeighbours2("A", mOccurrences)
    val sOccurrences = matrix.getAllNeighbours2("S", aOccurrences)

    val result = sOccurrences.count()

    println("Solution 1: $result")
}

private fun solution2(input: List<String>) {
    val matrix = Matrix(input.map { it.split("") })

    val result = matrix.getXmas()

    println("Solution 2: $result")
}

private class Matrix(private val input: List<List<String>>) {
    fun get(x: Int, y: Int) = input[y][x]

    fun getY(y: Int) = input[y]

    fun indexOfAll(search: String): List<Position> {
        val results = mutableListOf<Position>()

        input.forEachIndexed { y, yList ->
            yList.forEachIndexed { x, element ->
                if (element == search) results += Position(x, y)
            }
        }

        return results
    }

    // Weird naming due to type erasure.
    fun getAllNeighbours1(search: String, positions: List<Position>): List<DirectionalPosition> {
        return positions.flatMap {
            filterNeighbours(search, it)
        }
    }

    // Weird naming due to type erasure.
    fun getAllNeighbours2(search: String, directions: List<DirectionalPosition>): List<DirectionalPosition> {
        return directions.flatMap { dp ->
            filterNeighbours(search, dp.position, dp.direction)
        }
    }

    fun getXmas(): Int {
        var result = 0
        val startElements = listOf("M", "S")

        input.forEachIndexed { y, yList ->
            yList.forEachIndexed { x, element ->
                if (element !in startElements) return@forEachIndexed

                val secondX = x + 2
                val secondY = y
                if (!isXValid(secondX, y)) return@forEachIndexed
                val secondElement = get(secondX, secondY)
                if (secondElement !in startElements) return@forEachIndexed

                val thirdX = x + 1
                val thirdY = y + 1
                if (!isValidCoordinate(thirdX, thirdY)) return@forEachIndexed
                val thirdElement = get(thirdX, thirdY)
                if (thirdElement != "A") return@forEachIndexed

                val fourthX = x
                val fourthY = y + 2
                if (!isYValid(fourthY)) return@forEachIndexed
                val fourthElement = get(fourthX, fourthY)
                if (fourthElement !in startElements) return@forEachIndexed
                if (element == secondElement && secondElement == fourthElement) return@forEachIndexed

                val fifthX = x + 2
                val fifthY = y + 2
                if (!isValidCoordinate(fifthX, fifthY)) return@forEachIndexed
                val fifthElement = get(fifthX, fifthY)
                if (fifthElement !in startElements) return@forEachIndexed

                val topElementsEqual = element == secondElement && fourthElement == fifthElement
                val sideElementsEqual = element == fourthElement && secondElement == fifthElement
                if (topElementsEqual || sideElementsEqual) result++
            }
        }

        return result
    }

    private fun filterNeighbours(
        search: String,
        position: Position,
        direction: Direction? = null,
    ): List<DirectionalPosition> {
        val neighbours = mutableListOf<DirectionalPosition>()

        val startX: Int
        val endX: Int
        val startY: Int
        val endY: Int

        if (direction == null) {
            startX = position.x - 1
            endX = position.x + 1
            startY = position.y - 1
            endY = position.y + 1
        } else {
            startX = position.x + direction.x
            endX = startX
            startY = position.y + direction.y
            endY = startY
        }

        for (x in startX..endX) {
            for (y in startY..endY) {
                if (!isValidCoordinate(x, y)) continue

                if (get(x, y) == search) {
                    val foundPosition = Position(x, y)
                    val foundDirection = Direction.get(position, foundPosition)

                    if (direction != null && foundDirection != direction) continue

                    neighbours += DirectionalPosition(foundPosition, foundDirection)
                }
            }
        }

        return neighbours
    }

    private fun isValidCoordinate(x: Int, y: Int) = isYValid(y) && isXValid(x, y)

    private fun isXValid(x: Int, y: Int, position: Position? = null): Boolean {
        return when {
            x < 0 -> false
            x >= getY(y).size -> false
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
}

private class Position(position: Pair<Int, Int>) {
    constructor(x: Int, y: Int) : this(x to y)

    val x = position.first
    val y = position.second

    fun equals(x: Int, y: Int) = this.x == x && this.y == y
}

private class DirectionalPosition(
    val position: Position,
    val direction: Direction,
)

private enum class Direction(val x: Int, val y: Int) {
    NW(-1, -1),
    N(0, -1),
    NE(1, -1),
    E(1, 0),
    SE(1, 1),
    S(0, 1),
    SW(-1, 1),
    W(-1, 0);

    companion object {
        fun get(position: Position, newPosition: Position): Direction {
            val xDiff = newPosition.x - position.x
            val yDiff = newPosition.y - position.y

            return when (xDiff) {
                -1 -> when (yDiff) {
                    -1 -> NW
                    0 -> W
                    1 -> SW
                    else -> error("")
                }

                0 -> when (yDiff) {
                    -1 -> N
                    0 -> error("")
                    1 -> S
                    else -> error("")
                }

                1 -> when (yDiff) {
                    -1 -> NE
                    0 -> E
                    1 -> SE
                    else -> error("")
                }

                else -> error("")
            }
        }
    }
}


@Suppress("unused")
private val example = """
   MMMSXXMASM
   MSAMXMSMSA
   AMXSXMAAMM
   MSAMASMSMX
   XMASAMXAMM
   XXAMMXXAMA
   SMSMSASXSS
   SAXAMASAAA
   MAMMMXMMMM
   MXMXAXMASX
""".trimIndent().split("\n")
