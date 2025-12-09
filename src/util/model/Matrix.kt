package util.model

class Matrix(private var input: List<List<String>>) {
    val size = input.first().size to input.size

    fun get(position: Position) = get(position.x, position.y)

    fun get(x: Int, y: Int) = if (isValidCoordinate(x, y)) input[y][x] else null

    fun getY(y: Int) = if (isYValid(y)) input[y] else null

    fun set(position: Position, value: String) {
        input = input.toMutableList().apply {
            val yColumn = this[position.y].toMutableList()
            yColumn[position.x] = value
            this[position.y] = yColumn
        }
    }

    private fun isValidCoordinate(x: Int, y: Int) = isYValid(y) && isXValid(x, y)

    fun forEachIndexed(onElement: (Position, String) -> Unit) {
        input.forEachIndexed { y, row ->
            row.forEachIndexed { x, element ->
                onElement(Position(x, y), element)
            }
        }
    }

    private fun isXValid(x: Int, y: Int, position: Position? = null): Boolean {
        val yValue = getY(y)

        return when {
            x < 0 -> false
            yValue == null -> false
            x >= yValue.size -> false
            position?.x == x -> false
            else -> true
        }
    }

    private fun isYValid(y: Int, position: Position? = null): Boolean {
        return when {
            y < 0 -> false
            y >= input.size -> false
            position?.y == y -> false
            else -> true
        }
    }

    override fun toString(): String {
        return input.joinToString("\n")
    }

    companion object {
        fun fromInput(input: List<String>): Matrix {
            return Matrix(input.filterNot { it.isBlank() }
                .map { list -> list.trim().split("").filterNot { it.isEmpty() } }
                .map { list -> list.filterNot { it.isBlank() } })
        }
    }
}
