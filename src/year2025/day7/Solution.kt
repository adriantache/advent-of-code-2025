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
    solution1(input)
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

private var amplifiedPositions = mutableMapOf<Position, Long>()

private fun solution2(input: List<String>) {
    val matrix = Matrix.fromInput(input)
    val startPosition = matrix.getStartPosition().plusY(1)

    amplifiedPositions += startPosition to 1

    matrix.hitBeamSplittersByLayer()

    val result = amplifiedPositions.values.sumOf { it }

    println("Solution 2: $result")
}

private fun Matrix.hitBeamSplittersByLayer() {
    while (this.hasLowerLevel(amplifiedPositions)) {
        val previousLevel = amplifiedPositions
        amplifiedPositions = mutableMapOf()

        previousLevel.forEach { this.addLowerLayer(it.key, it.value) }
    }
}

private fun Matrix.hasLowerLevel(amplifiedPosition: MutableMap<Position, Long>) =
    this.get(amplifiedPosition.keys.first().plusY(1)) != null

private fun Matrix.addLowerLayer(position: Position, power: Long) {
    assert(power > 0) {
        "Failed at $position $power $amplifiedPositions"
    }
    val nextPosition = position.plusY(1)
    val next = this.get(nextPosition)

    when (next) { // No emitters next to each other, so we don't need to cover that case.
        null -> error("Unexpected lower level $nextPosition for $size")

        "^" -> {
            val leftNextPosition = nextPosition.minusX(1)
            if (get(leftNextPosition) != null) {
                amplifiedPositions[leftNextPosition] = (amplifiedPositions[leftNextPosition] ?: 0) + power
            }

            val rightNextPosition = nextPosition.plusX(1)
            if (get(rightNextPosition) != null) {
                amplifiedPositions[rightNextPosition] = (amplifiedPositions[rightNextPosition] ?: 0) + power
            }
        }

        else -> amplifiedPositions[nextPosition] = (amplifiedPositions[nextPosition] ?: 0) + power
    }
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
