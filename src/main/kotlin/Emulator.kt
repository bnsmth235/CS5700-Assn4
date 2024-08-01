import java.io.File
import java.io.IOException

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
        try {
            val file = File(path)
            val bytes = file.readBytes()
            System.arraycopy(bytes, 0, memory.rom, 0, bytes.size)
        } catch (e: IOException) {
            println("Failed to load program: ${e.message}")
        }
    }
}