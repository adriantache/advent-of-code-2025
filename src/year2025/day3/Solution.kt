package year2025.day3

import util.getInput

/**
 * Solution for https://adventofcode.com/2025/day/3
 */
fun main() {
    val input = getInput(3, 2025)
//    val input = example
//    val input = listOf(example2)

    solution1(input)
    solution2(input)
}

private fun solution1(input: List<String>) {
    val batteryBanks = input.map { banks: String ->
        banks.split("").filterNot { it.isBlank() }.map { it.toInt() }
    }

    val result = batteryBanks.sumOf {
        it.findMaxOutput()
    }

    println("Solution 1: $result")
}

// The second solution is so much better than this.
private fun List<Int>.findMaxOutput(): Int {
    var firstMaxIndex = this.indexOfMax()

    if (firstMaxIndex == this.size - 1) {
        firstMaxIndex = this.subList(0, firstMaxIndex).indexOfMax()
    }

    val firstMax = this[firstMaxIndex]
    val secondMax = this.getSecondMax(firstMaxIndex)

    return firstMax * 10 + secondMax
}

private fun List<Int>.indexOfMax() = this.indexOfFirst { it == this.max() }

private fun List<Int>.getSecondMax(firstMaxIndex: Int): Int {
    val sublist = this.subList(firstMaxIndex + 1, this.size)

    return sublist.max()
}

private fun solution2(input: List<String>) {
    val batteryBanks = input.map { banks: String ->
        banks.split("").filterNot { it.isBlank() }.map { it.toInt() }
    }

    val result = batteryBanks.sumOf {
        it.findMaxDozenOutput()
    }

    println("Solution 2: $result")
}

private fun List<Int>.findMaxDozenOutput(): Long {
    return this.findLocalMaximum(result = "", length = 12).toLong()
}

private fun List<Int>.findLocalMaximum(result: String, length: Int): String {
    if (length == 0) return result

    val sublist = this.subList(0, size - length + 1)

    val max = sublist.max()

    val maxIndex = this.indexOf(max)
    val newList = this.subList(maxIndex + 1, size)

    return newList.findLocalMaximum(result + max, length - 1)
}

@Suppress("unused")
private val example = """
   987654321111111
   811111111111119
   234234234234278
   818181911112111
""".trimIndent().split("\n")

@Suppress("unused")
private val example2 =
    "2132134422231312132224223332252152652232411222642322332221274223212222242274222123442322222222238231"
