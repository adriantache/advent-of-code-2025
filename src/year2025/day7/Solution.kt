package year2025.day7

import util.getInput
import util.model.Matrix
import util.model.Position

/**
 * Solution for https://adventofcode.com/2025/day/7
 */
fun main() {
    val input = getInput(7, 2025)
//    val input = example

    println()
//    solution1(input)
    println()
    solution2(input)
    println()
}

private val splitterPositions = mutableSetOf<Position>()
private val beamPositions = mutableSetOf<Position>()
private var terminatedBeams = 0

private fun solution1(input: List<String>) {
    val matrix = Matrix.fromInput(input)
    val startPosition = matrix.getStartPosition()

    matrix.hitBeamSplitters(startPosition, false)

    val result = splitterPositions.size

    println("Solution 1: $result")
}

private fun Matrix.getStartPosition(): Position {
    val firstLine = getY(0) ?: error("Empty matrix: $this")
    val positionX = firstLine.indexOfFirst { it == "S" }

    return Position(positionX, 0)
}

private fun Matrix.hitBeamSplitters(startPosition: Position, allowRepeats: Boolean) {
    val nextPosition = startPosition.plusY(1)
    val next = this.get(nextPosition)

    when (next) {
        null -> {
            terminatedBeams += 1
            println("Terminated beams: $terminatedBeams")
            return
        }

        "^" -> {
            splitterPositions += nextPosition

            // No emitters next to each other, so we don't need to cover that case.

            val leftNextPosition = nextPosition.minusX(1)
            val leftNext = this.get(leftNextPosition)
            val isLeftNotTravelled = allowRepeats || leftNextPosition !in beamPositions
            if (leftNext != null && isLeftNotTravelled) {
                beamPositions += leftNextPosition
                this.hitBeamSplitters(leftNextPosition, allowRepeats)
            }

            val rightNextPosition = nextPosition.plusX(1)
            val rightNext = this.get(rightNextPosition)
            val isRightNotTravelled = allowRepeats || rightNextPosition !in beamPositions
            if (rightNext != null && isRightNotTravelled) {
                beamPositions += rightNextPosition
                this.hitBeamSplitters(rightNextPosition, allowRepeats)
            }
        }

        else -> {
            beamPositions += nextPosition
            hitBeamSplitters(nextPosition, allowRepeats)
        }
    }
}

private fun solution2(input: List<String>) {
    val matrix = Matrix.fromInput(input)
    val startPosition = matrix.getStartPosition()

    matrix.hitBeamSplitters(startPosition, allowRepeats = true)

    println("Solution 2: $terminatedBeams")
}

@Suppress("unused")
private val example = """
   .......S.......
   ...............
   .......^.......
   ...............
   ......^.^......
   ...............
   .....^.^.^.....
   ...............
   ....^.^...^....
   ...............
   ...^.^...^.^...
   ...............
   ..^...^.....^..
   ...............
   .^.^.^.^.^...^.
   ...............

""".trimIndent().split("\n")
