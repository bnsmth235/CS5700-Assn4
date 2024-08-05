import java.io.File
import java.io.FileNotFoundException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

class Emulator {
    lateinit var romFile: File
    val instructionFactory = InstructionFactory()
    val memory = Memory()
    val screen = Screen()
    val keyboard = Keyboard()
    val registers = Registers()
    val cpu = CPU()
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var cpuFuture: ScheduledFuture<*>? = null
    private var timerFuture: ScheduledFuture<*>? = null

    fun start(path: String) {
        try {
            val fullpath = System.getProperty("user.dir") + "/src/main/resources/roms/" + path
            romFile = File(fullpath)
            readRom()
            startCPU()
        } catch (e: FileNotFoundException) {
            println("Please provide a rom that exists")
        }
    }

    private fun readRom() {
        romFile.readBytes().toUByteArray().copyInto(memory.rom)
    }

    fun startCPU() {
        val cpuRunnable = Runnable {
            try {
                val instructionByte = memory.readRom(cpu.programCounter)
                val stopCheckByte = memory.readRom((cpu.programCounter + 1u).toUShort())
                val nibbles = ByteParser(instructionByte, stopCheckByte)
                if (instructionByte.toInt() == 0 && stopCheckByte.toInt() == 0) {
                    stop()
                }
                val instruction = instructionFactory.create(nibbles.nibbles[0])
                instruction.execute(cpu, memory, registers, screen, keyboard)
            } catch (e: Exception) {
                e.printStackTrace()
                stop()
            }
        }
        val timerRunnable = Runnable {
            if (cpu.timer.toInt() != 0) {
                cpu.timer = (cpu.timer - 1u).toUByte()
            }
        }

        cpuFuture = executor.scheduleAtFixedRate(
            cpuRunnable,
            0,
            1000L / 500L,
            TimeUnit.MILLISECONDS
        )
        timerFuture = executor.scheduleAtFixedRate(
            timerRunnable,
            0,
            1000/60L,
            TimeUnit.MILLISECONDS
        )
    }

    fun stop() {
        cpuFuture?.cancel(true)
        timerFuture?.cancel(true)
        exitProcess(0)
    }
}