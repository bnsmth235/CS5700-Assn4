import kotlin.test.*

class RegistersTest {

    @Test
    fun programCounter_shouldBeEven() {
        val registers = Registers()
        registers.programCounter = 5
        assertEquals(4, registers.programCounter)
    }

    @Test
    fun programCounter_shouldSetCorrectly() {
        val registers = Registers()
        registers.programCounter = 10
        assertEquals(10, registers.programCounter)
    }

    @Test
    fun timer_shouldSetCorrectly() {
        val registers = Registers()
        registers.timer = 20
        assertEquals(20, registers.timer)
    }

    @Test
    fun address_shouldSetCorrectly() {
        val registers = Registers()
        registers.address = 100
        assertEquals(100, registers.address)
    }

    @Test
    fun memoryFlag_shouldToggleCorrectly() {
        val registers = Registers()
        registers.memoryFlag = true
        assertTrue(registers.memoryFlag)
        registers.memoryFlag = false
        assertFalse(registers.memoryFlag)
    }

    @Test
    fun generalRegisters_shouldInitializeToZero() {
        val registers = Registers()
        for (i in registers.general.indices) {
            assertEquals(0.toUByte(), registers.general[i])
        }
    }

    @Test
    fun generalRegisters_shouldSetValuesCorrectly() {
        val registers = Registers()
        registers.general[0] = 10u
        registers.general[1] = 20u
        assertEquals(10u, registers.general[0])
        assertEquals(20u, registers.general[1])
    }
}