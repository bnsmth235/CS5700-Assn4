fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: emulator <program-path>")
        return
    }

    val programPath = args[0]
    val emulator = Emulator()
    emulator.start(programPath)
}
