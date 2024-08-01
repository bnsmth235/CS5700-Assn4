class CPU(private val memory: Memory, private val registers: Registers, private val screen: Screen, private val keyboard: Keyboard) {
    fun execute() {
        // Fetch and execute instructions
    }

    private fun fetch(): Pair<Byte, Byte> {
        // Fetch instruction from memory
        return Pair(0, 0)
    }

    private fun decode(opcode: Byte, operand: Byte) {
        // Decode and execute instruction
    }
}
