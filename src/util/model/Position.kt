package util.model

data class Position(
    val x: Int,
    val y: Int,
) {
    constructor(pair: Pair<Int, Int>) : this(pair.first, pair.second)

    fun equals(x: Int, y: Int) = this.x == x && this.y == y

    fun plusX(value: Int) = this.copy(x = x + value)
    fun plusY(value: Int) = this.copy(y = y + value)

    fun minusX(value: Int) = plusX(-value)
    fun minusY(value: Int) = plusY(-value)
}
