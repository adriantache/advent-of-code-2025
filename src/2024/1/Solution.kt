package `2024`.`1`

import util.getInput
import kotlin.math.abs

/**
 * Solution for https://adventofcode.com/2024/day/1
 */
fun main() {
    val input = getInput(1, 2024)
//    val input = example

    val list1 = mutableListOf<Int>()
    val list2 = mutableListOf<Int>()

    input.forEach { listItem ->
        val (first, second) = listItem.split("   ")
        list1.add(first.toInt())
        list2.add(second.toInt())
    }

    solution1(list1, list2)
    solution2(list1, list2)
}

private fun solution1(list1: List<Int>, list2: List<Int>) {
    val list1Sorted = list1.sorted()
    val list2Sorted = list2.sorted()

    val sumOfDifferences = list1Sorted.zip(list2Sorted).sumOf {
        abs(it.first - it.second)
    }

    println("Solution 1: $sumOfDifferences")
}

private fun solution2(list1: List<Int>, list2: List<Int>) {
    val list2Frequency = mutableMapOf<Int, Int>()

    list2.forEach {
        list2Frequency[it] = (list2Frequency[it] ?: 0) + 1
    }

    val sum = list1.sumOf { it * (list2Frequency[it] ?: 0) }

    println("Solution 2: $sum")
}

@Suppress("unused")
private val example = """
    3   4
    4   3
    2   5
    1   3
    3   9
    3   3
""".trimIndent().split("\n")
