import kotlin.test.*

class ByteParserTest {

    @Test
    fun combineNibbles_shouldCombineCorrectly() {
        val parser = ByteParser(0xABu, 0xCDu)
        val result = parser.combineNibbles(0xA0u, 0x0Bu).toUByte()
        assertEquals((0xB).toUByte(), result)
    }

    @Test
    fun getAddress_shouldReturnCorrectAddress() {
        val parser = ByteParser(0x12u, 0x34u)
        val address = parser.getAddress().toUByte()
        assertEquals((0x34).toUByte(), address)
    }

    @Test
    fun nibbles_shouldExtractNibblesCorrectly() {
        val parser = ByteParser(0xABu, 0xCDu)
        listOf(0x0Au, 0x0Bu, 0x0Cu, 0x0Du).forEachIndexed { index, nibble ->
            assertEquals(nibble.toUByte(), parser.nibbles[index])
        }
    }

    @Test
    fun combineNibbles_withZeroValues_shouldReturnZero() {
        val parser = ByteParser(0x00u, 0x00u)
        val result = parser.combineNibbles(0x00u, 0x00u)
        assertEquals(0x00, result)
    }

    @Test
    fun getAddress_withZeroValues_shouldReturnZero() {
        val parser = ByteParser(0x00u, 0x00u)
        val address = parser.getAddress()
        assertEquals(0x0000u, address)
    }

    @Test
    fun nibbles_withMaxValues_shouldExtractCorrectly() {
        val parser = ByteParser(0xFFu, 0xFFu)
        listOf(0x0Fu, 0x0Fu, 0x0Fu, 0x0Fu).forEachIndexed { index, nibble ->
            assertEquals(nibble.toUByte(), parser.nibbles[index])
        }
    }
}