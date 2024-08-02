class Screen {
    private val screenMemory: Array<CharArray> = Array(25) { CharArray(80) { ' ' } } // Assuming a 25x80 screen for this example

    fun drawCharacter(char: Char, row: Int, column: Int) {
        if (row in screenMemory.indices && column in screenMemory[row].indices) {
            screenMemory[row][column] = char
        } else {
            throw IllegalArgumentException("Row or column out of bounds")
        }
    }

    fun draw() {
        clearConsole()
        for (row in screenMemory) {
            println(row.concatToString())
        }
        println("------------")
    }

    private fun clearConsole() {
        print("\u001b[H\u001b[2J")
        System.out.flush()
    }
}
