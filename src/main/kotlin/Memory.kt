class Memory {
    val rom = ByteArray(4096)
    val ram = ByteArray(4096)

    fun read(address: Int): Byte {
        if (address < 0 || address >= rom.size) {
            throw IllegalArgumentException("Address out of bounds: $address")
        }
        return if (address < 2048) rom[address] else ram[address - 2048]
    }

    fun write(address: Int, value: Byte) {
        if (address < 0 || address >= rom.size) {
            throw IllegalArgumentException("Address out of bounds: $address")
        }
        if (address < 2048) {
            throw IllegalArgumentException("Cannot write to ROM address: $address")
        }
        ram[address - 2048] = value
    }
}