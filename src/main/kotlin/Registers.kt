class Registers {
    val general = ByteArray(8)
    var programCounter: Int = 0
    var timer: Byte = 0
    var address: Int = 0
    var memoryFlag: Boolean = false
}