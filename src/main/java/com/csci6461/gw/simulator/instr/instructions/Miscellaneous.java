package com.csci6461.gw.simulator.instr.instructions;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.instr.Instruction;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Miscellaneous {
    private static Logger LOG = LogManager.getLogger("Instr.Misc");

    public static class HLT extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            LOG.info("HLT: Halting machine");
            cpu.halt();
            return;
        }
    }

    public static class TRAP extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            int trapco = Integer.parseInt(this.getInstruction().get("trapCode"), 2);
            LOG.info("TRAP: Trapping with code {}", trapco);
            cpu.trap(trapco);
            return;
        }
    }
}
