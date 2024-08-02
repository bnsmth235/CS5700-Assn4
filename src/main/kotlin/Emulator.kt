import java.io.File
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class Emulator {
    private val memory = Memory()
    private val registers = Registers()
    private val screen = Screen()
    private val keyboard = Keyboard()
    private val cpu = CPU(memory, registers, screen, keyboard)
    private val timer = Timer(registers)
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var cpuFuture: ScheduledFuture<*>? = null

    fun start(programPath: String) {
        loadProgram(programPath)
        timer.start()
        val cpuRunnable = Runnable {
            cpu.decodeAndExecute()
        }
        cpuFuture = executor.scheduleAtFixedRate(
            cpuRunnable,
            0,
            1000L / 500L, // repeat frequency - every 2 ms
            TimeUnit.MILLISECONDS
        )
    }

    fun stop() {
        cpuFuture?.cancel(true)
        timer.stop()
        executor.shutdown()
    }

    private fun loadProgram(path: String) {
        try {
            val fullpath = System.getProperty("user.dir") + "/src/main/resources/roms/" + path
            val file = File(fullpath)
            val bytes = file.readBytes()
            System.arraycopy(bytes, 0, memory.rom, 0, bytes.size)
        } catch (e: IOException) {
            println("Failed to load program: ${e.message}")
        }
    }
}