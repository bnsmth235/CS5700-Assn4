class CPU {
    var running: Boolean = true
    private val memory: Memory
    private val registers: Registers
    private val screen: Screen
    private val keyboard: Keyboard
    private val instructionFactory = InstructionFactory()

    constructor(memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        this.memory = memory
        this.registers = registers
        this.screen = screen
        this.keyboard = keyboard
    }

    fun fetchNextByte(): Byte {
        val byte = memory.read(registers.programCounter)
        registers.programCounter += 1
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
        if (running) {
            val opcode = fetchNextByte()
            val instruction = instructionFactory.create(opcode)
            executeInstruction(instruction)
            if (registers.timer > 0) {
                registers.timer--
            }
        }
    }
}