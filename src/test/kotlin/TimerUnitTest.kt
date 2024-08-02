import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class TimerUnitTest {
    @Test
    fun testTimerDecrements() {
        val registers = Registers()
        val timer = Timer(registers)

        registers.timer = 10
        timer.start()

        TimeUnit.MILLISECONDS.sleep(50) // Wait for a few ticks

        timer.stop()
        assertTrue(registers.timer < 10)
    }
}