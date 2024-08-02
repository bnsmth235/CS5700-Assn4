class Registers{
    val general = ByteArray(8)
    var programCounter: Int = 0
        set(value) {
            field = value and 0xFFFFFFFE.toInt() // Ensure program counter is always even
        }
    var timer: Byte = 0
    var address: Int = 0
    var memoryFlag: Boolean = false
}