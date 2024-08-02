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
        val instruction = cpu.fetchNextByte().toInt()
        val registerIndex = (instruction and 0x0F)
        if (registerIndex < 0 || registerIndex >= registers.general.size) {
            throw IllegalArgumentException("Register index out of bounds: $registerIndex")
        }
        val value = cpu.fetchNextByte()
        registers.general[registerIndex] = value
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
        registers.programCounter += 2
    }
}

class ReadInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        registers.general[registerIndex.toInt()] = memory.read(registers.address)
        registers.programCounter += 2
    }
}

class WriteInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        memory.write(registers.address, registers.general[registerIndex.toInt()])
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
    }
}

class ReadKeyboardInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        val input = keyboard.read() ?: 0
        registers.general[registerIndex.toInt()] = input.toByte()
        registers.programCounter += 2
    }
}

class SwitchMemoryInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        registers.memoryFlag = !registers.memoryFlag
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
        } else {
            registers.programCounter += 2
        }
    }
}

class SetAInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val address = cpu.fetchNextWord()
        registers.address = address
        registers.programCounter += 2
    }
}

class SetTInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val timerValue = cpu.fetchNextByte()
        registers.timer = timerValue
        registers.programCounter += 2
    }
}

class ReadTInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val registerIndex = cpu.fetchNextByte() and 0x0F
        registers.general[registerIndex.toInt()] = registers.timer
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
        registers.programCounter += 2
    }
}

class DrawInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val operands1 = cpu.fetchNextByte()
        val operands2 = cpu.fetchNextByte()
        val rX = (operands1.toInt() and 0xF0).shr(4)
        val rY = operands1.toInt() and 0x0F
        val rZ = (operands2.toInt() and 0xF0).shr(4)

        val charValue = registers.general[rX].toInt() and 0xFF
        if (charValue > 0x7F) {
            throw IllegalArgumentException("Value in register $rX is greater than 7F (127 in base-10)")
        }
        val character = charValue.toChar()

        val row = registers.general[rY].toInt()
        val column = registers.general[rZ].toInt()

        screen.drawCharacter(character, row, column)
        registers.programCounter += 2
    }
}
