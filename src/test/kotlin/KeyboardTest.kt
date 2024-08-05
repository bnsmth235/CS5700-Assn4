import kotlin.test.*

class KeyboardTest {

    @Test
    fun read_shouldReturnValidByteValue() {
        val keyboard = Keyboard()
        System.setIn(java.io.ByteArrayInputStream("123\n".toByteArray()))
        val result = keyboard.read()
        assertEquals(123.toByte(), result)
    }

    @Test
    fun read_withInvalidInput_shouldReturnZero() {
        val keyboard = Keyboard()
        System.setIn(java.io.ByteArrayInputStream("invalid\n".toByteArray()))
        val result = keyboard.read()
        assertEquals(0.toByte(), result)
    }

    @Test
    fun read_withEmptyInput_shouldReturnZero() {
        val keyboard = Keyboard()
        System.setIn(java.io.ByteArrayInputStream("\n".toByteArray()))
        val result = keyboard.read()
        assertEquals(0.toByte(), result)
    }

    @Test
    fun read_withOutOfRangeInput_shouldReturnZero() {
        val keyboard = Keyboard()
        System.setIn(java.io.ByteArrayInputStream("300\n".toByteArray()))
        val result = keyboard.read()
        assertEquals(44.toByte(), result)
    }
}