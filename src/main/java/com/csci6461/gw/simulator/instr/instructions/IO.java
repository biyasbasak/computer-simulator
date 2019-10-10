package com.csci6461.gw.simulator.instr.instructions;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.instr.Instruction;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class IO {
    private static Logger LOG = LogManager.getLogger("Instr.IO");

    public static class IN extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            return;
        }
    }

    public static class OUT extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            return;
        }
    }
}
