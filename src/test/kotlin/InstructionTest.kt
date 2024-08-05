import kotlin.test.*

class InstructionTest {

    @Test
    fun storeInstruction_shouldStoreValueInRegister() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = StoreInstruction()
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(0x02u, registers.general[1])
    }

    @Test
    fun addInstruction_shouldAddValuesInRegisters() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = AddInstruction()
        registers.general[1] = 5u
        registers.general[2] = 10u
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(5u, registers.general[2])
    }

    @Test
    fun subInstruction_shouldSubtractValuesInRegisters() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = SubInstruction()
        registers.general[1] = 10u
        registers.general[2] = 5u
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(10u, registers.general[2])
    }

    @Test
    fun readInstruction_shouldReadValueFromMemory() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = ReadInstruction()
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        memory.writeRam(0u, 0xFFu)
        registers.address = 0
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(0xFFu, registers.general[1])
    }

    @Test
    fun writeInstruction_shouldWriteValueToMemory() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = WriteInstruction()
        registers.general[1] = 0xFFu
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        registers.address = 0
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(0xFFu, memory.readRam(0u))
    }

    @Test
    fun jumpInstruction_shouldSetProgramCounter() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = JumpInstruction()
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(0x0102u, cpu.programCounter)
    }

    @Test
    fun readKeyboardInstruction_shouldReadInput() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = ReadKeyboardInstruction()
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        // Simulate user input
        System.setIn(java.io.ByteArrayInputStream("A\n".toByteArray()))
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(0x0Au, registers.general[1])
    }

    @Test
    fun switchMemoryInstruction_shouldToggleMemoryFlag() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = SwitchMemoryInstruction()
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertTrue(registers.memoryFlag)
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertFalse(registers.memoryFlag)
    }

    @Test
    fun skipEqualInstruction_shouldSkipIfEqual() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = SkipEqualInstruction()
        registers.general[1] = 5u
        registers.general[2] = 5u
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(2u, cpu.programCounter)
    }

    @Test
    fun skipNotEqualInstruction_shouldSkipIfNotEqual() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = SkipNotEqualInstruction()
        registers.general[1] = 5u
        registers.general[2] = 10u
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(4u, cpu.programCounter)
    }

    @Test
    fun setAInstruction_shouldSetAddressRegister() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = SetAInstruction()
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(0x0102, registers.address)
    }

    @Test
    fun setTInstruction_shouldSetTimer() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = SetTInstruction()
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(0x10u, cpu.timer)
    }

    @Test
    fun readTInstruction_shouldReadTimer() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = ReadTInstruction()
        cpu.timer = 0x12u
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(0x12u, registers.general[1])
    }

    @Test
    fun convertToBase10Instruction_shouldConvertValue() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = ConvertToBase10Instruction()
        registers.general[1] = 123u
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        registers.address = 0
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(1u, memory.readRam(0u))
        assertEquals(2u, memory.readRam(1u))
        assertEquals(3u, memory.readRam(2u))
    }

    @Test
    fun convertToAsciiInstruction_shouldConvertValueToAscii() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val instruction = ConvertToAsciiInstruction()
        registers.general[1] = 0x0Au
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, Screen(), Keyboard())
        assertEquals(0u, registers.general[2])
    }

    @Test
    fun drawInstruction_shouldDrawOnScreen() {
        val memory = Memory()
        val cpu = CPU()
        val registers = Registers()
        val screen = Screen()
        val instruction = DrawInstruction()
        registers.general[1] = 0x41u // ASCII for 'A'
        ubyteArrayOf(0xA1u, 0x02u).copyInto(memory.rom)
        cpu.programCounter = 0u
        instruction.execute(cpu, memory, registers, screen, Keyboard())
        assertEquals(0u, screen.memory[1 * 8 + 2])
    }
}