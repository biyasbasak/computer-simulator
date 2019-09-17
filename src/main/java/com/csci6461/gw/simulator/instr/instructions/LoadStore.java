package com.csci6461.gw.simulator.instr.instructions;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.instr.Instruction;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;

import java.util.HashMap;

public class LoadStore {
    public static class LDR  extends Instruction{
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
        }
    }
}
