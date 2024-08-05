fun main(args: Array<String>) {
    //get rom directory from user input
    var romDirectory = ""
    if (args.isEmpty()) {
        println("Please enter the directory of the rom file")
        romDirectory = readLine() ?: ""
    } else {
        romDirectory = args[0]
    }

    val programPath = romDirectory
    val emulator = Emulator()
    emulator.start(programPath)
}
