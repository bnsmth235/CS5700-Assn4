class InstructionFactory {
    fun create(opcode: Byte): Instruction {
        return when (opcode.toInt()) {
            0x00 -> StopInstruction()
            0x01 -> StoreInstruction()
            0x02 -> AddInstruction()
            0x03 -> JumpInstruction()
            0x04 -> ReadKeyboardInstruction()
            0x05 -> SwitchMemoryInstruction()
            0x06 -> SkipEqualInstruction()
            0x07 -> DrawInstruction()
            0x08 -> SetAInstruction()
            0x09 -> SetTInstruction()
            0x0A -> ReadTInstruction()
            0x0B -> SubInstruction()
            0x0C -> ReadInstruction()
            0x0D -> ConvertToBase10Instruction()
            0x0E -> ConvertToAsciiInstruction()
            0x0F -> SkipNotEqualInstruction()
            0x10 -> WriteInstruction()
            else -> throw IllegalArgumentException("Unknown opcode: $opcode")
        }
    }
}
