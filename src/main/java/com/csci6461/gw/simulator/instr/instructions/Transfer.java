package com.csci6461.gw.simulator.instr.instructions;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.instr.Instruction;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Transfer {
    private static Logger LOG = LogManager.getLogger("Ins.LoadStore");
    public static class JZ extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            String R = instruction.get("reg");
            Register GR = registers.getGeneralRegister(Integer.parseInt(R, 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            if (GR.value() == 0 ) {
                registers.setPC(effectiveAddress);
            } else {
                registers.advance();
            }
        }
    }
    public static class JNE extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            String R = instruction.get("reg");
            Register GR = registers.getGeneralRegister(Integer.parseInt(R, 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            if (GR.value() != 0 ) {
                registers.setPC(effectiveAddress);
            } else {
                registers.advance();
            }
        }
    }
    public static class JCC extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            String R = instruction.get("reg");
            Register CC = registers.getGeneralRegister(Integer.parseInt(R, 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            if (CC.value() == 1) {
                registers.setPC(effectiveAddress);
            } else {
                registers.advance();
            }
        }
    }
    public static class JMA extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.setPC(effectiveAddress);
        }
    }
}
