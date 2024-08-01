class Keyboard {
    fun read(): Byte {
        // Read input from user
        println("Enter a byte value (0-255):")
        val input = readLine()?.toIntOrNull() ?: 0
        return input.toByte()
    }
}
