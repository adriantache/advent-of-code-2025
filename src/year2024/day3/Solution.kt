package year2024.day3

import util.getInput

private const val DO_STRING = "do()"
private const val DONT_STRING = "don't()"

/**
 * Solution for https://adventofcode.com/2024/day/3
 */
fun main() {
    val input = getInput(3, 2024)
//    val input = example
//    val input = example2

    solution1(input.joinToString(""))
    solution2(input.joinToString(""))
}

private val regex = """mul\((\d+,\d+)\)""".toRegex()

private fun solution1(input: String) {
    val result = getAllOperations(input)

    println("Solution 1: $result")
}

private fun solution2(input: String) {
    var currentString = input
    var result = 0

    while (regex.find(currentString) != null) {
        val nextDont = currentString.indexOf(DONT_STRING)

        if (nextDont == -1) {
            result += getAllOperations(currentString)
            break
        }

        val beforeDontString = currentString.substring(0, nextDont)
        result += getAllOperations(beforeDontString)

        val afterDontString = currentString.substring(nextDont + DONT_STRING.length)
        val nextDo = afterDontString.indexOf(DO_STRING)

        if (nextDo == -1) break

        val afterDoString = afterDontString.substring(nextDo)
        currentString = afterDoString
    }

    println("Solution 2: $result")
}

private fun getAllOperations(input: String): Int {
    return regex.findAll(input).sumOf {
        val (num1, num2) = it.groupValues[1].split(",")

        num1.toInt() * num2.toInt()
    }
}

@Suppress("unused")
private val example = """
   xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
""".trimIndent().split("\n")

@Suppress("unused")
private val example2 = """
   xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
""".trimIndent().split("\n")
