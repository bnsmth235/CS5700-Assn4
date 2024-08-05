class Memory {
    val rom = UByteArray(4096)
    val ram = UByteArray(4096)

    fun readRom(address: UShort): UByte {
        if (address.toInt() >= rom.size) {
            throw IndexOutOfBoundsException("Attempted to read from out of bounds memory address")
        }
        return rom[address.toInt()]
    }

    fun readRam(address: UShort): UByte{
        if (address.toInt() >= ram.size) {
            throw IndexOutOfBoundsException("Attempted to read from out of bounds memory address")
        }
        return ram[address.toInt()]
    }

    fun writeRam(address: UShort, value: UByte) {
        if (address.toInt() >= ram.size) {
            throw IndexOutOfBoundsException("Attempted to write to out of bounds memory address")
        }
        ram[address.toInt()] = value
    }
}