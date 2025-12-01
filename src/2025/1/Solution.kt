package `2025`.`1`

import util.getInput

/**
 * Solution for https://adventofcode.com/2025/day/1
 */
fun main() {
    val input = getInput(1, 2025)
//    val input = example

    solution1(input)
    solution2(input)
}

private fun solution1(input: List<String>) {
    var result = 0

    val safe = Safe {}

    input.forEach {
        val direction = it.first()
        val amount = it.drop(1).toInt()

        when (direction) {
            'R' -> safe.spinRight(amount)
            'L' -> safe.spinLeft(amount)
        }

        if (safe.currentDigit == 0) result++
    }

    println("Solution 1: $result")
}


private fun solution2(input: List<String>) {
    var result = 0

    val safe = Safe { result++ }

    input.forEach {
        val direction = it.first()
        val amount = it.drop(1).toInt()

        when (direction) {
            'R' -> safe.spinLeft(amount)
            'L' -> safe.spinRight(amount)
        }
    }

    println("Solution 2: $result")
}

private class Safe(private val onZeroClick: () -> Unit) {
    var currentDigit: Int = 50
        private set

    fun spinRight(spins: Int) {
        var spins = spins

        while (spins-- > 0) {
            currentDigit--
            checkDigit()
        }
    }

    fun spinLeft(spins: Int) {
        var spins = spins

        while (spins-- > 0) {
            currentDigit++
            checkDigit()
        }
    }

    private fun checkDigit() {
        currentDigit = when (currentDigit) {
            -1 -> 99
            100 -> 0
            else -> currentDigit
        }

        if (currentDigit == 0) {
            onZeroClick()
        }
    }

    override fun toString(): String {
        return currentDigit.toString()
    }
}

@Suppress("unused")
private val example = """
   L68
   L30
   R48
   L5
   R60
   L55
   L1
   L99
   R14
   L82
""".trimIndent().split("\n")
