package year2025.day8

import util.getInput
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Solution for https://adventofcode.com/2025/day/8
 */
fun main() {
    val input = getInput(8, 2025)
//    val input = example

    println()
    solution1(input)
    println()
    solution2(input)
    println()
}

private fun solution1(input: List<String>) {
    val positions = input.map { Position3.fromString(it) }

    val distances = positions.calculateAllDistances().sorted()
    val connections = distances.connect(1000)
    val connectionSizes = connections.map { it.size }.sorted().reversed()

    val result = connectionSizes.take(3).reduce { acc, i -> acc * i }

    println("Solution 1: $result")
}

private fun List<Pair<Pair<Position3, Position3>, Float>>.connect(repetitions: Int): List<List<Position3>> {
    val distances = this.take(repetitions).map { it.first }
    val connections = PositionsList()

    distances.forEach {
        connections.add(it.toList())
    }

    return connections.positions
}

private data class PositionsList(
    val positions: MutableList<List<Position3>> = mutableListOf()
) {
    fun isValid(input: Pair<Position3, Position3>): Boolean {
        return positions.none { it.contains(input.first) && it.contains(input.second) }
    }

    fun add(elements: List<Position3>) {
        val existingLists = positions.filter { list -> elements.any { it in list } }
        positions.removeAll(existingLists)

        val newList = (existingLists.flatten() + elements).distinct()
        positions.add(newList)
    }
}

private fun List<Position3>.calculateAllDistances(): Distances {
    val distances = Distances()

    for (i in 0 until size - 1) {
        for (j in i + 1 until size) {
            distances.set(this[i], this[j], this[i].distanceTo(this[j]))
        }
    }

    return distances
}

private fun solution2(input: List<String>) {
    val positions = input.map { Position3.fromString(it) }

    val distances = positions.calculateAllDistances().sorted()

    val lastConnection = distances.connect()
    val result = lastConnection.first.x * lastConnection.second.x

    println("Solution 2: $result")
}

private fun List<Pair<Pair<Position3, Position3>, Float>>.connect(): Pair<Position3, Position3> {
    val distances = this.map { it.first }
    val connections = PositionsList()

    lateinit var lastConnection: Pair<Position3, Position3>

    distances.forEach {
        if (connections.isValid(it)) lastConnection = it
        connections.add(it.toList())
    }

    return lastConnection
}

private data class Position3(val x: Long, val y: Long, val z: Long) {
    fun distanceTo(other: Position3): Float {
        return sqrt(
            (other.x - x).toFloat().pow(2) +
                    (other.y - y).toFloat().pow(2) +
                    (other.z - z).toFloat().pow(2)
        )
    }

    companion object {
        fun fromString(input: String): Position3 {
            val positions = input.split(",").map { it.trim().toLong() }
            return Position3(positions[0], positions[1], positions[2])
        }
    }
}

private data class Distances(
    private val distances: MutableMap<Pair<Position3, Position3>, Float> = mutableMapOf()
) {
    fun get(first: Position3, second: Position3): Float? {
        return distances[first to second] ?: distances[second to first]
    }

    fun set(first: Position3, second: Position3, value: Float) {
        distances[first to second] = value
    }

    fun sorted() = distances.toList().sortedBy { it.second }
}

@Suppress("unused")
private val example = """
   162,817,812
   57,618,57
   906,360,560
   592,479,940
   352,342,300
   466,668,158
   542,29,236
   431,825,988
   739,650,466
   52,470,668
   216,146,977
   819,987,18
   117,168,530
   805,96,715
   346,949,466
   970,615,88
   941,993,340
   862,61,35
   984,92,344
   425,690,689
""".trimIndent().split("\n")
