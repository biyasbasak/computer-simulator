package com.csci6461.gw.simulator.instr.instructions;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.cpu.ALU;
import com.csci6461.gw.simulator.instr.Instruction;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Transfer {
    private static Logger LOG = LogManager.getLogger("Ins.Transfer");
    public static class JZ extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            String R = instruction.get("reg");
            Register GR = registers.getGeneralRegister(Integer.parseInt(R, 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            if (GR.value() == 0) {
                registers.setPC(effectiveAddress);
            } else {
                registers.advance();
            }
            LOG.info("JZ: PC <- {}", registers.pc());
        }
    }
    public static class JNE extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            String R = instruction.get("reg");
            Register GR = registers.getGeneralRegister(Integer.parseInt(R, 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            if (GR.value() != 0) {
                registers.setPC(effectiveAddress);
            } else {
                registers.advance();
            }
            LOG.info("JNE: PC <- {}", registers.pc());
        }
    }
    public static class JCC extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int cc = Integer.parseInt(instruction.get("reg"), 2);
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            if (registers.getAllRegisters().get("CC").get(cc)) {
                registers.setPC(effectiveAddress);
            } else {
                registers.advance();
            }
            LOG.info("JCC: PC <- {}", registers.pc());
        }
    }
    public static class JMA extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);

            LOG.info("JMA: PC <- {}", effectiveAddress);

            registers.setPC(effectiveAddress);
        }
    }
    public static class JSR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            Register GR = registers.getGeneralRegister(3);
            GR.setByValue(registers.pc() + 1);
            registers.setPC(effectiveAddress);
            
            LOG.info("JSR: PC <- {}", registers.pc());
        }
    }
    public static class RFS extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            Register GR0 = registers.getGeneralRegister(0);
            Register GR3 = registers.getGeneralRegister(3);
            GR0.setByValue(effectiveAddress);
            registers.setPC(GR3.value());
            
            LOG.info("RFS: PC <- {}", registers.pc());
        }
    }
    public static class SOB extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            String R = instruction.get("reg");
            Register GR = registers.getGeneralRegister(Integer.parseInt(R, 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            
            Element result = cpu.getALU().subtraction(GR, Element.fromString(ALU.ONE));
            if (result.value() > 0) {
                registers.setPC(effectiveAddress);
            } else {
                registers.advance();
            }
            GR.set(result);
            
            LOG.info("SOB: PC <- {}", registers.pc());
        }
    }
    public static class JGE extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            String R = instruction.get("reg");
            Register GR = registers.getGeneralRegister(Integer.parseInt(R, 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            if (GR.value() >= 0) {
                registers.setPC(effectiveAddress);
            } else {
                registers.advance();
            }
            
            LOG.info("JGE: PC <- {}", registers.pc());
        }
    }
}
