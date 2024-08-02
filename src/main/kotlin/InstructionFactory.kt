class InstructionFactory {
    fun create(opcode: Byte): Instruction {
        val opcode = opcode.toInt() and 0xF0 shr 4
        return when (opcode) {
            0x0 -> StoreInstruction()
            0x1 -> AddInstruction()
            0x2 -> SubInstruction()
            0x3 -> ReadInstruction()
            0x4 -> WriteInstruction()
            0x5 -> JumpInstruction()
            0x6 -> ReadKeyboardInstruction()
            0x7 -> SwitchMemoryInstruction()
            0x8 -> SkipEqualInstruction()
            0x9 -> SkipNotEqualInstruction()
            0xA -> SetAInstruction()
            0xB -> SetTInstruction()
            0xC -> ReadTInstruction()
            0xD -> ConvertToBase10Instruction()
            0xE -> ConvertToAsciiInstruction()
            0xF -> DrawInstruction()
            else -> throw IllegalArgumentException("Unknown opcode: $opcode")
        }
    }
}