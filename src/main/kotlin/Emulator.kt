class Emulator {
    private val memory = Memory()
    private val registers = Registers()
    private val screen = Screen()
    private val keyboard = Keyboard()
    private val cpu = CPU(memory, registers, screen, keyboard)

    fun start(programPath: String) {
        loadProgram(programPath)
        cpu.execute()
    }

    private fun loadProgram(path: String) {
        // Load program into ROM
    }
}