package com.csci6461.gw.simulator.instr;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;

import java.util.BitSet;
import java.util.HashMap;

/**
 * Interface every instrction instance implements
 */
public abstract class Instruction {
    private HashMap<String, String> instruction;

    /**
     * Execute the instruction within the context of the CPU.
     */
    public abstract void execute(CPU cpu, Memory memory, MachineRegisters registers);

    /**
     * Set decoded instruction.
     */
    public void setInstruction(HashMap<String, String> instruction) {
        this.instruction = instruction;
    }

    /**
     * Get decoded instruction.
     */
    public HashMap<String, String> getInstruction() {
        return this.instruction;
    }
}
