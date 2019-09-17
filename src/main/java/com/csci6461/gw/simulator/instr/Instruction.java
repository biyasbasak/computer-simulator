package com.csci6461.gw.simulator.instr;

import com.csci6461.gw.simulator.cpu.CPU;
import java.util.BitSet;

/**
 * Interface every instrction instance implements
 */
public abstract class Instruction {
    /**
     * Instruction data of binary representation.
     */
    private BitSet data;

    Instruction(BitSet data) {
        assert data.length() == 16;

    }

    /**
     * Decode the instruction.
     */
    void decode() {

    }

    /**
     * Get opcode
     */
    public abstract int opcode();

    /**
     * Execute instruction within the context of the CPU.
     */
    public abstract void execute(CPU cpu);

    /**
     * Disassemble the instruction
     */
    public abstract String disassemble();
}
