package year2024.day2

import util.getInput
import kotlin.math.abs
import kotlin.properties.Delegates

/**
 * Solution for https://adventofcode.com/2024/day/2
 */
fun main() {
    val input = getInput(2, 2024)
//    val input = example

    solution1(input)
    solution2(input)
}

private fun solution1(input: List<String>) {
    var goodReports = 0

    input.forEach { reportString ->
        val numbers = reportString.split(" ")

        if (isSafeReport(numbers)) goodReports++
    }

    println("Solution 1: $goodReports")
}

private fun isSafeReport(numbers: List<String>): Boolean {
    var isRising: Boolean? = null
    var previousNumber by Delegates.notNull<Int>()

    numbers.forEachIndexed { index, numberString ->
        val number = numberString.toInt()

        if (index != 0) {
            if (isRising == null) isRising = number > previousNumber

            val isDirectionViolation = number == previousNumber ||
                    (isRising && number < previousNumber) ||
                    (!isRising && number > previousNumber)

            if (isDirectionViolation) return false

            val difference = abs(number - previousNumber)
            val isDifferenceViolation = difference !in listOf(1, 2, 3)

            if (isDifferenceViolation) return false
        }

        previousNumber = number
    }

    return true
}

private fun solution2(input: List<String>) {
    var goodReports = 0

    input.forEach { reportString ->
        val numbers = reportString.split(" ")

        val isSafeReport = isSafeReport(numbers)

        if (isSafeReport) {
            goodReports++
            return@forEach
        } else {
            repeat(numbers.size) { index ->
                val reducedNumbers = numbers.toMutableList().apply {
                    removeAt(index)
                }

                if (isSafeReport(reducedNumbers)) {
                    goodReports++
                    return@forEach
                }
            }
        }
    }

    println("Solution 2: $goodReports")
}

@Suppress("unused")
private val example = """
   7 6 4 2 1
   1 2 7 8 9
   9 7 6 2 1
   1 3 2 4 5
   8 6 4 4 1
   1 3 6 7 9
""".trimIndent().split("\n")
