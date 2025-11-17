package util

import java.io.File

const val SOURCE_ROOT = "./src"
const val INPUT_FILE = "input.txt"
const val CODE_FILE = "Solution.kt"

fun getInput(
    day: Int,
    year: Int,
): List<String> {
    val file = getInputFile(day, year)
    val contents = file.readText()

    return contents.split("\n").filterNot { it.isBlank() }
}

fun getInputFile(
    day: Int,
    year: Int,
): File {
    val root = getRootPath(year, day)
    return File(root, INPUT_FILE)
        .also { File(it.parent).mkdirs() }
}

fun getCodeFile(
    day: Int,
    year: Int,
): File {
    val root = getRootPath(year, day)
    return File(root, CODE_FILE)
        .also { File(it.parent).mkdirs() }
}

private fun getRootPath(year: Int, day: Int): String = "$SOURCE_ROOT/$year/$day"
