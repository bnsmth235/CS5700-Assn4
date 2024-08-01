class CPU {
    var running: Boolean = true
    private val memory: Memory
    private val registers: Registers
    private val screen: Screen
    private val keyboard: Keyboard

    constructor(memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        this.memory = memory
        this.registers = registers
        this.screen = screen
        this.keyboard = keyboard
    }

    fun fetchNextByte(): Byte {
        val byte = memory.read(registers.programCounter)
        registers.programCounter++
        return byte
    }

    fun fetchNextWord(): Int {
        val highByte = fetchNextByte().toInt() and 0xFF
        val lowByte = fetchNextByte().toInt() and 0xFF
        return (highByte shl 8) or lowByte
    }

    fun executeInstruction(instruction: Instruction) {
        instruction.execute(this, memory, registers, screen, keyboard)
    }

    fun decodeAndExecute() {
        while (running) {
            val opcode = fetchNextByte().toInt() and 0xF0 shr 4
            val instruction: Instruction = when (opcode) {
                0x0 -> StoreInstruction()
                0x1 -> AddInstruction()
                0x2 -> SubInstruction()
                0x3 -> ReadInstruction()
                0x4 -> WriteInstruction()
                0x5 -> JumpInstruction()
                0x6 -> ReadKeyboardInstruction()
                0x7 -> SwitchMemoryInstruction()
                0x8 -> SkipEqualInstruction()
                0x9 -> SkipNotEqualInstruction()
                0xA -> SetAInstruction()
                0xB -> SetTInstruction()
                0xC -> ReadTInstruction()
                0xD -> ConvertToBase10Instruction()
                0xE -> ConvertToAsciiInstruction()
                0xF -> DrawInstruction()
                else -> throw IllegalArgumentException("Unknown opcode: $opcode")
            }
            executeInstruction(instruction)
        }
    }
}
