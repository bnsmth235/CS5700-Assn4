import kotlin.experimental.and

interface Instruction {
    fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard)
}

class StopInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        cpu.running = false
        println("Execution stopped.")
    }
}

class StoreInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        val value = cpu.fetchNextByte()
        registers.general[registerIndex.toInt()] = value
        println("Stored value $value in register $registerIndex.")
        registers.programCounter += 2
    }
}

class AddInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val operands = cpu.fetchNextByte()
        val rX = (operands.toInt() and 0xF0).shr(4)
        val rY = operands and 0x0F
        val rZ = cpu.fetchNextByte() and 0x0F
        registers.general[rZ.toInt()] = (registers.general[rX] + registers.general[rY.toInt()]).toByte()
        println("Added values in register $rX and register $rY, result stored in register $rZ.")
        registers.programCounter += 2
    }
}

class SubInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val operands = cpu.fetchNextByte()
        val rX = (operands.toInt() and 0xF0).shr(4)
        val rY = operands and 0x0F
        val rZ = cpu.fetchNextByte() and 0x0F
        registers.general[rZ.toInt()] = (registers.general[rX] - registers.general[rY.toInt()]).toByte()
        println("Subtracted value in register $rY from register $rX, result stored in register $rZ.")
        registers.programCounter += 2
    }
}

class ReadInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        registers.general[registerIndex.toInt()] = memory.read(registers.address)
        println("Read value from address ${registers.address}, stored in register $registerIndex.")
        registers.programCounter += 2
    }
}

class WriteInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        memory.write(registers.address, registers.general[registerIndex.toInt()])
        println("Wrote value ${registers.general[registerIndex.toInt()]} to address ${registers.address}.")
        registers.programCounter += 2
    }
}

class JumpInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val address = cpu.fetchNextWord()
        if (address % 2 != 0) {
            throw IllegalArgumentException("Jump address must be divisible by 2")
        }
        registers.programCounter = address
        println("Jumped to address $address.")
    }
}

class ReadKeyboardInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        val input = keyboard.read() ?: 0
        registers.general[registerIndex.toInt()] = input.toByte()
        println("Read value $input from keyboard, stored in register $registerIndex.")
        registers.programCounter += 2
    }
}

class SwitchMemoryInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        registers.memoryFlag = !registers.memoryFlag
        println("Switched memory bank to ${if (registers.memoryFlag) "RAM" else "ROM"}.")
        registers.programCounter += 2
    }
}

class SkipEqualInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val operands = cpu.fetchNextByte()
        val rX = (operands.toInt() and 0xF0).shr(4)
        val rY = operands and 0x0F
        if (registers.general[rX] == registers.general[rY.toInt()]) {
            registers.programCounter += 4
            println("Skipped next instruction because values in registers $rX and $rY are equal.")
        } else {
            registers.programCounter += 2
        }
    }
}

class SkipNotEqualInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val operands = cpu.fetchNextByte()
        val rX = (operands.toInt() and 0xF0).shr(4)
        val rY = operands and 0x0F
        if (registers.general[rX] != registers.general[rY.toInt()]) {
            registers.programCounter += 4
            println("Skipped next instruction because values in registers $rX and $rY are not equal.")
        } else {
            registers.programCounter += 2
        }
    }
}

class SetAInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val address = cpu.fetchNextWord()
        registers.address = address
        println("Set address register A to $address.")
        registers.programCounter += 2
    }
}

class SetTInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val timerValue = cpu.fetchNextByte()
        registers.timer = timerValue
        println("Set timer register T to $timerValue.")
        registers.programCounter += 2
    }
}

class ReadTInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        registers.general[registerIndex.toInt()] = registers.timer
        println("Read value from timer register T, stored in register $registerIndex.")
        registers.programCounter += 2
    }
}

class ConvertToBase10Instruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        val value = registers.general[registerIndex.toInt()] and 0xFF.toByte()
        memory.write(registers.address, (value / 100).toByte())
        memory.write(registers.address + 1, ((value / 10) % 10).toByte())
        memory.write(registers.address + 2, (value % 10).toByte())
        println("Converted value in register $registerIndex to base-10 and stored in memory at addresses ${registers.address}, ${registers.address + 1}, and ${registers.address + 2}.")
        registers.programCounter += 2
    }
}

class ConvertToAsciiInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val operands = cpu.fetchNextByte()
        val rX = (operands.toInt() and 0xF0).shr(4)
        val rY = operands and 0x0F
        val value = registers.general[rX].toInt() and 0xFF
        if (value > 0x0F) {
            throw IllegalArgumentException("Value in register $rX is greater than F (base-16)")
        }
        registers.general[rY.toInt()] = (value + 0x30).toByte()  // ASCII conversion for digits 0-F
        println("Converted value in register $rX to ASCII and stored in register $rY.")
        registers.programCounter += 2
    }
}

class DrawInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val operands1 = cpu.fetchNextByte()
        val operands2 = cpu.fetchNextByte()
        val rX = (operands1.toInt() and 0xF0).shr(4)
        val rY = operands1 and 0x0F
        val rZ = operands2 and 0x0F
        val value = registers.general[rX].toInt() and 0xFF
        if (value > 0x7F) {
            throw IllegalArgumentException("Value in register $rX is greater than 7F (127 in base-10)")
        }
        screen.draw(value.toChar(), registers.general[rY.toInt()].toInt(), registers.general[rZ.toInt()].toInt())
        println("Drew ASCII character ${value.toChar()} at row ${registers.general[rY.toInt()]}, column ${registers.general[rZ.toInt()]}.")
        registers.programCounter += 2
    }
}
