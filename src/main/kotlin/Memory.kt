class Memory(size: Int) {
    private val memoryArray = ByteArray(size)

    fun read(address: Int): Byte {
        if (address < 0 || address >= memoryArray.size) {
            throw IllegalArgumentException("Address out of bounds: $address")
        }
        return memoryArray[address]
    }

    fun write(address: Int, value: Byte) {
        if (address < 0 || address >= memoryArray.size) {
            throw IllegalArgumentException("Address out of bounds: $address")
        }
        memoryArray[address] = value
    }
}
