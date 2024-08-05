class ByteParser(val b0: UByte, val b1: UByte) {
    val nibbles: List<UByte>

    init {
        nibbles = listOf(
            (b0.toInt() shr 4).toUByte(),
            (b0.toInt() and 0xF).toUByte(),
            (b1.toInt() shr 4).toUByte(),
            (b1.toInt() and 0xF).toUByte()
        )
    }

    fun combineNibbles(n0: UByte, n1: UByte) = (n0.toInt() shl 4) or n1.toInt()

    fun getAddress() = ((nibbles[1].toInt() shl 8) + (nibbles[2].toInt() shl 4) + nibbles[3].toInt()).toUShort()
}