package com.csci6461.gw.simulator.instr;

import com.csci6461.gw.simulator.cpu.CPU;

/**
 * Interface every instrction instance implements
 */
public interface Instruction {
    int opcode();

    void execute(CPU cpu);
}
