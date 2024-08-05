import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.TimeUnit
import kotlin.test.*

class EmulatorTest {

    @Test
    fun start_withValidRomPath_shouldLoadRomAndStartCPU() {
        val emulator = Emulator()
        emulator.start("hello.out")
        assertNotNull(emulator.romFile)
        assertTrue(emulator.cpuFuture?.isCancelled == false)
    }

    @Test
    fun start_withInvalidRomPath_shouldPrintErrorMessage() {
        val emulator = Emulator()
        val outputStream = java.io.ByteArrayOutputStream()
        System.setOut(java.io.PrintStream(outputStream))

        emulator.start("invalid_rom_path.rom")

        val output = outputStream.toString()
        assertTrue(output.contains("Please provide a rom that exists"))
    }

    @Test
    fun readRom_shouldLoadRomIntoMemory() {
        val emulator = Emulator()
        emulator.start("hello.out")
        assertTrue(emulator.memory.rom.isNotEmpty())
    }

    @Test
    fun startCPU_shouldScheduleCpuAndTimerTasks() {
        val emulator = Emulator()
        emulator.startCPU()
        assertNotNull(emulator.cpuFuture)
        assertNotNull(emulator.timerFuture)
    }

    @Test
    fun stop_shouldCancelCpuAndTimerTasks() {
        val emulator = Emulator()
        emulator.startCPU()
        emulator.stop()
        assertTrue(emulator.cpuFuture?.isCancelled == true)
        assertTrue(emulator.timerFuture?.isCancelled == true)
    }

    @Test
    fun startCPU_shouldHandleExceptionsAndStop() {
        val emulator = Emulator()
        emulator.memory.rom[0] = 0xFFu // Invalid instruction to trigger exception
        emulator.startCPU()
        TimeUnit.MILLISECONDS.sleep(10000) // Allow some time for the CPU task to run
        assertTrue(emulator.cpuFuture?.isCancelled == true)
        assertTrue(emulator.timerFuture?.isCancelled == true)
    }
}