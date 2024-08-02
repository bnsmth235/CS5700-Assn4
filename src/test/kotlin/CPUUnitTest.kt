import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CPUUnitTest {
    @Test
    fun testFetchNextByte() {
        val memory = Memory()
        val registers = Registers()
        val screen = Screen()
        val keyboard = Keyboard()
        val cpu = CPU(memory, registers, screen, keyboard)

        memory.rom[0] = 0x12
        registers.programCounter = 0

        val byte = cpu.fetchNextByte()
        assertEquals(0x12.toByte(), byte)
        assertEquals(1, registers.programCounter)
    }

    @Test
    fun testFetchNextWord() {
        val memory = Memory()
        val registers = Registers()
        val screen = Screen()
        val keyboard = Keyboard()
        val cpu = CPU(memory, registers, screen, keyboard)

        memory.rom[0] = 0x12
        memory.rom[1] = 0x34
        registers.programCounter = 0

        val word = cpu.fetchNextWord()
        assertEquals(0x1234, word)
        assertEquals(2, registers.programCounter)
    }

    @Test
    fun testExecuteInstruction() {
        val memory = Memory()
        val registers = Registers()
        val screen = Screen()
        val keyboard = Keyboard()
        val cpu = CPU(memory, registers, screen, keyboard)

        val instruction = object : Instruction {
            override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
                registers.general[0] = 0x42
            }
        }

        cpu.executeInstruction(instruction)
        assertEquals(0x42.toByte(), registers.general[0])
    }
}