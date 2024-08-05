class InstructionFactory {
    fun create(opcode: UByte): Instruction {
        return when (opcode.toInt()) {
            0x00 -> StoreInstruction()
            0x01 -> AddInstruction()
            0x02 -> SubInstruction()
            0x03 -> ReadInstruction()
            0x04 -> WriteInstruction()
            0x05 -> JumpInstruction()
            0x06 -> ReadKeyboardInstruction()
            0x07 -> SwitchMemoryInstruction()
            0x08 -> SkipEqualInstruction()
            0x09 -> SkipNotEqualInstruction()
            0x0A -> SetAInstruction()
            0x0B -> SetTInstruction()
            0x0C -> ReadTInstruction()
            0x0D -> ConvertToBase10Instruction()
            0x0E -> ConvertToAsciiInstruction()
            0x0F -> DrawInstruction()
            else -> throw IllegalArgumentException("Unknown opcode: $opcode")
        }
    }
}