package com.csci6461.gw.simulator.cpu;

import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.memory.Memory;

public class CPU {
    private MachineRegisters registers;
    private Memory memory;

    CPU() {
        registers = new MachineRegisters();
        memory = new Memory();
    }

    public MachineRegisters getRegisters() {
        return registers;
    }

    public Memory getMemory() {
        return memory;
    }
}
