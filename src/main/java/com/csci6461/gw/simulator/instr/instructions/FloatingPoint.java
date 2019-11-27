package com.csci6461.gw.simulator.instr.instructions;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.instr.Instruction;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;
import static com.csci6461.gw.simulator.util.Exceptions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class FloatingPoint {
    private static Logger LOG = LogManager.getLogger("Instr.FloatingPoint");

    public static class FADD extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int index = Integer.parseInt(instruction.get("reg"), 2);
            if(index >= 2) {
                throw new CPUException(registers.pc(), "Invalid FP register");
            }
            Register fpr = registers.getFPRegister(index);
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            Element value = memory.fetch(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(value.value());
            Element result = cpu.getALU().fadd(fpr, value, true);
            LOG.info("FADD: {} + {} = {}", fpr.fvalue(), value.fvalue(), result.fvalue());
            fpr.set(result);
            registers.advance();
        }
    }

    public static class FSUB extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int index = Integer.parseInt(instruction.get("reg"), 2);
            if(index >= 2) {
                throw new CPUException(registers.pc(), "Invalid FP register");
            }
            Register fpr = registers.getFPRegister(index);
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            Element value = memory.fetch(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(value.value());
            Element result = cpu.getALU().fsub(fpr, value, true);
            LOG.info("FSUB: {} - {} = {}", fpr.fvalue(), value.fvalue(), result.fvalue());
            fpr.set(result);
            registers.advance();
        }
    }

    public static class VADD extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int index = Integer.parseInt(instruction.get("reg"), 2);
            if(index >= 2) {
                throw new CPUException(registers.pc(), "Invalid FP register");
            }
            Register fpr = registers.getFPRegister(index);
            int ea = memory.calculateEffectiveAddress(registers, instruction);
            boolean indirect = Integer.parseInt(instruction.get("indirectBit"), 2) == 1;

            int size = (int)fpr.fvalue();
            int mem1 = memory.fetch(ea).value();
            if(indirect) {
                mem1 = memory.fetch(mem1).value();
            }

            int mem2 = memory.fetch(ea + 1).value();
            if(indirect) {
                mem2 = memory.fetch(mem2).value();
            }

            for(int i = 0; i < size; i++) {
                Element e1 = memory.fetch(mem1 + i);
                Element e2 = memory.fetch(mem2 + i);
                Element result = cpu.getALU().addition(e1, e2);
                memory.store(mem1 + i, result);
            }

            LOG.info("VADD: Vector at ({}, {})", mem1, mem2);
            registers.advance();
        }
    }

    public static class VSUB extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int index = Integer.parseInt(instruction.get("reg"), 2);
            if(index >= 2) {
                throw new CPUException(registers.pc(), "Invalid FP register");
            }
            Register fpr = registers.getFPRegister(index);
            int ea = memory.calculateEffectiveAddress(registers, instruction);
            boolean indirect = Integer.parseInt(instruction.get("indirectBit"), 2) == 1;

            int size = (int)fpr.fvalue();
            int mem1 = memory.fetch(ea).value();
            if(indirect) {
                mem1 = memory.fetch(mem1).value();
            }

            int mem2 = memory.fetch(ea + 1).value();
            if(indirect) {
                mem2 = memory.fetch(mem2).value();
            }

            for(int i = 0; i < size; i++) {
                Element e1 = memory.fetch(mem1 + i);
                Element e2 = memory.fetch(mem2 + i);
                Element result = cpu.getALU().subtraction(e1, e2);
                memory.store(mem1 + i, result);
            }

            LOG.info("VADD: Vector at ({}, {})", mem1, mem2);
            registers.advance();
        }
    }

    public static class CNVRT extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int index = Integer.parseInt(instruction.get("reg"), 2);
            if(index >= 2) {
                throw new CPUException(registers.pc(), "Invalid CPU operation");
            }

            int ea = memory.calculateEffectiveAddress(registers, instruction);
            if(index == 0) {
                Element value = memory.fetch(ea);
                registers.getGeneralRegister(0).setByValue((int)value.fvalue());
            } else {
                Element value = memory.fetch(ea);
                registers.getFPRegister(0).setByFValue((double)value.value());
            }
            LOG.info("CNVRT: {}", index);
            registers.advance();
        }
    }

    public static class LDFR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int index = Integer.parseInt(instruction.get("reg"), 2);
            if(index >= 2) {
                throw new CPUException(registers.pc(), "Invalid FP register");
            }
            Register fpr = registers.getFPRegister(index);
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            Element value = memory.fetch(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(value.value());
            fpr.setByFValue(value.fvalue());
            LOG.info("LDFR: {} <- EA({})", fpr.fvalue(), effectiveAddress);
            registers.advance();
        }
    }

    public static class STFR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int index = Integer.parseInt(instruction.get("reg"), 2);
            if(index >= 2) {
                throw new CPUException(registers.pc(), "Invalid FP register");
            }
            Register fpr = registers.getFPRegister(index);
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            memory.store(effectiveAddress, fpr);
            LOG.info("STFR: {} -> EA({})", fpr.fvalue(), effectiveAddress);
            registers.advance();
        }
    }
}
