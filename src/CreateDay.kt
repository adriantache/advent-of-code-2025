import util.getCodeFile
import util.getInputFile
import java.io.File
import java.net.HttpURLConnection
import java.net.URI

// TODO: update to 2025
private const val YEAR = 2024
private const val DAY = 2
private const val AOC_URL = "https://adventofcode.com/${YEAR}/day/${DAY}"
private const val AOC_INPUT_URL = "$AOC_URL/input"

fun main() {
    checkDay()

    createInputFile()
    createCodeTemplate()
}

private fun checkDay() {
    val inputFile = getInputFile(DAY, YEAR)

    if (inputFile.exists()) error("Folder already exists for $DAY/$YEAR!")
}

private fun createInputFile() {
    val destinationFile = getInputFile(DAY, YEAR)
    val contents = getInputFromServer()

    destinationFile.writeText(contents)
}

private fun createCodeTemplate() {
    val codeFile = getCodeFile(DAY, YEAR)

    codeFile.writeText(FILE_TEMPLATE)
}

private fun getInputFromServer(): String {
    val cookie = File("./secret/cookie").readText()

    val url = URI(AOC_INPUT_URL).toURL()
    val conn = (url.openConnection() as HttpURLConnection).apply {
        requestMethod = "GET"
        setRequestProperty("Cookie", cookie)
    }

    val responseCode = conn.responseCode
    if (responseCode != HttpURLConnection.HTTP_OK) {
        error("Server returned HTTP $responseCode")
    }

    val body = conn.inputStream.bufferedReader().use { it.readText() }

    conn.disconnect()

    return body
}

private val FILE_TEMPLATE = """
    package `$YEAR`.`$DAY`

    import util.getInput

    /**
     * Solution for $AOC_URL
     */
    fun main() {
    //    val input = getInput($DAY, $YEAR)
        val input = example
        
        solution1(input)
        solution2(input)
    }
    
    private fun solution1(input: List<String>) {
        
        println("Solution 1: ")
    }
    
    private fun solution2(input: List<String>) {
        
        println("Solution 2: ")
    }
    
    @Suppress("unused")
    private val example = ""${'"'}
       
    ""${'"'}.trimIndent().split("\n")
""".trimIndent()
