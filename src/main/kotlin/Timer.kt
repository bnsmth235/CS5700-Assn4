import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class Timer(private val registers: Registers) {
    private val executor = Executors.newSingleThreadScheduledExecutor()
    private var timerFuture: ScheduledFuture<*>? = null

    fun start() {
        val timerRunnable = Runnable {
            if (registers.timer > 0) {
                registers.timer--
            }
        }
        timerFuture = executor.scheduleAtFixedRate(
            timerRunnable,
            0,
            16, // 60Hz execution rate
            TimeUnit.MILLISECONDS
        )
    }

    fun stop() {
        timerFuture?.cancel(true)
        executor.shutdown()
    }
}