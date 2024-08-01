class Screen {
    val display = ByteArray(64)

    fun draw(char: Char, row: Int, col: Int) {
        // Draw character on the screen
        val index = row * 8 + col
        display[index] = char.code.toByte()
    }
}