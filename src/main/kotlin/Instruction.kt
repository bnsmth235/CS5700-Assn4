interface Instruction {
    fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard)
}

class StoreInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        val bb = nibbles.b1
        registers.general[rX] = bb
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class AddInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        val rY = nibbles.nibbles[2].toInt()
        val rZ = nibbles.nibbles[3].toInt()
        registers.general[rZ] = (registers.general[rX] + registers.general[rY]).toUByte()
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class SubInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        val rY = nibbles.nibbles[2].toInt()
        val rZ = nibbles.nibbles[3].toInt()
        registers.general[rZ] = (registers.general[rX] - registers.general[rY]).toUByte()
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class ReadInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        val address = registers.address.toUShort()
        val value = if (registers.memoryFlag) memory.readRom(address) else memory.readRam(address)
        registers.general[rX] = value.toUByte()
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class WriteInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        val address = registers.address.toUShort()
        val value = registers.general[rX].toUByte()
        if (registers.memoryFlag) {
            // Attempt to write to ROM (future-proofing)
            // Currently, this will fail for most ROM chips
        } else {
            memory.writeRam(address, value)
        }
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class JumpInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val address = nibbles.getAddress()
        if (address.toInt() % 2 != 0) {
            throw IllegalArgumentException("Address not divisible by 2")
        }
        cpu.programCounter = address
    }
}

class ReadKeyboardInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        println("Please enter a hex digit (0-F):")
        val input = readLine()?.take(2)?.toIntOrNull(16) ?: 0
        registers.general[rX] = input.toUByte()
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class SwitchMemoryInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        registers.memoryFlag = !registers.memoryFlag
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class SkipEqualInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        val rY = nibbles.nibbles[2].toInt()
        if (registers.general[rX] == registers.general[rY]) {
            cpu.programCounter = (cpu.programCounter + 4u).toUShort()
        } else {
            cpu.programCounter = (cpu.programCounter + 2u).toUShort()
        }
    }
}

class SkipNotEqualInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        val rY = nibbles.nibbles[2].toInt()
        if (registers.general[rX] != registers.general[rY]) {
            cpu.programCounter = (cpu.programCounter + 4u).toUShort()
        } else {
            cpu.programCounter = (cpu.programCounter + 2u).toUShort()
        }
    }
}

class SetAInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val address = nibbles.getAddress()
        registers.address = address.toInt()
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class SetTInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val value = nibbles.combineNibbles(nibbles.nibbles[1], nibbles.nibbles[2])
        cpu.timer = value.toUByte()
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class ReadTInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        registers.general[rX] = cpu.timer
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class ConvertToBase10Instruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        val value = registers.general[rX].toInt()
        val hundreds = value / 100
        val tens = (value / 10) % 10
        val ones = value % 10
        memory.writeRam(registers.address.toUShort(), hundreds.toUByte())
        memory.writeRam((registers.address + 1).toUShort(), tens.toUByte())
        memory.writeRam((registers.address + 2).toUShort(), ones.toUByte())
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class ConvertToAsciiInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rX = nibbles.nibbles[1].toInt()
        val rY = nibbles.nibbles[2].toInt()
        val value = registers.general[rX].toInt()
        if (value > 0xF) {
            throw IllegalArgumentException("Value in rX is greater than F")
        }
        registers.general[rY] = (value + 0x30).toUByte() // Convert to ASCII
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}

class DrawInstruction : Instruction {
    override fun execute(cpu: CPU, memory: Memory, registers: Registers, screen: Screen, keyboard: Keyboard) {
        val nibbles = ByteParser(memory.readRom(cpu.programCounter), memory.readRom((cpu.programCounter + 1u).toUShort()))
        val rx = nibbles.nibbles[1].toInt()
        val ry = nibbles.nibbles[2].toInt()
        val rz = nibbles.nibbles[3].toInt()
        val x = registers.general[rx]
        if (x > 127u) {
            throw Exception("Cannot draw ascii value greater than 127")
        }
        // draw the ascii value of x at the screen location specified by ry and rz
        screen.memory[ry * 8 + rz] = x.toUByte()
        screen.draw()
        cpu.programCounter = (cpu.programCounter + 2u).toUShort()
    }
}