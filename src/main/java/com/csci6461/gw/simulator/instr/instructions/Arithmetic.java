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

public class Arithmetic {
    private static Logger LOG = LogManager.getLogger("Instr.Arithmetic");

    public static class AMR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            Element value = memory.fetch(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(value.value());
            Element result = cpu.getALU().addition(generalRegister, value);
            LOG.info("AMR : {} + {} = {}", generalRegister.value(), value.value(), result.value());
            generalRegister.set(result);
            registers.advance();
        }
    }

    public static class SMR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            Element value = memory.fetch(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(value.value());
            Element result = cpu.getALU().subtraction(generalRegister, value);
            LOG.info("SMR : {} + {} = {}", generalRegister.value(), value.value(), result.value());
            generalRegister.set(result);
            registers.advance();
        }
    }

    public static class AIR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            Element immed = Element.fromString(instruction.get("address"));
            Element result = cpu.getALU().addition(generalRegister, immed);
            LOG.info("AIR: {} + {} = {}", generalRegister.value(), immed.value(), result.value());
            generalRegister.set(result);
            registers.advance();
        }
    }

    public static class SIR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            Element immed = Element.fromString(instruction.get("address"));
            Element result = cpu.getALU().subtraction(generalRegister, immed);
            LOG.info("SIR: {} + {} = {}", generalRegister.value(), immed.value(), result.value());
            generalRegister.set(result);
            registers.advance();
        }
    }

    public static class MLT extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int Rx = Integer.parseInt(instruction.get("Rx"), 2);
            int Ry = Integer.parseInt(instruction.get("Ry"), 2);
            if(Rx % 2 == 1 || Ry % 2 == 1) {
                throw new CPUException(registers.pc(), "Invalid multiplication register");
            }

            Register rx = registers.getGeneralRegister(Rx);
            Register ry = registers.getGeneralRegister(Ry);
            Element result = cpu.getALU().multiply(rx, ry, true);

            Element lowbits = new Element(16), hibits = new Element(16);

            lowbits.set(result.get(16, 32));
            hibits.set(result.get(0, 16));

            LOG.info("MLT: {} * {} = {} ({}|{})", rx.value(), ry.value(), result.value(), hibits.toString(), lowbits.toString());

            registers.getGeneralRegister(Rx).set(hibits);
            registers.getGeneralRegister(Rx + 1).set(lowbits);

            registers.advance();
        }
    }

    public static class DVD extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int Rx = Integer.parseInt(instruction.get("Rx"), 2);
            int Ry = Integer.parseInt(instruction.get("Ry"), 2);
            if(Rx % 2 == 1 || Ry % 2 == 1) {
                throw new CPUException(registers.pc(), "Invalid multiplication register");
            }

            Register rx = registers.getGeneralRegister(Rx);
            Register ry = registers.getGeneralRegister(Ry);

            if(ry.value() == 0) {
                LOG.info("DVD: Division by zero");

                registers.setEqual(false);
                registers.setDivZero(true);
                registers.setOverflow(false);
                registers.setUnderflow(false);
            } else {
                Element result = cpu.getALU().divide(rx, ry, true);

                Element remainder = new Element(16), quotient = new Element(16);

                remainder.set(result.get(16, 32));
                quotient.set(result.get(0, 16));

                LOG.info("DVD: {} / {} = {}, remainder = {}", rx.value(), ry.value(), quotient.value(), remainder.value());

                registers.getGeneralRegister(Rx).set(quotient);
                registers.getGeneralRegister(Rx + 1).set(remainder);
            }

            registers.advance();
        }
    }

    public static class TRR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register rx = registers.getGeneralRegister(Integer.parseInt(instruction.get("Rx"), 2));
            Register ry = registers.getGeneralRegister(Integer.parseInt(instruction.get("Ry"), 2));
            LOG.info("TRR: {} == {}", rx.value(), ry.value());
            cpu.getALU().test(rx, ry);
            registers.advance();
        }
    }

    public static class AND extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register rx = registers.getGeneralRegister(Integer.parseInt(instruction.get("Rx"), 2));
            Register ry = registers.getGeneralRegister(Integer.parseInt(instruction.get("Ry"), 2));
            Element result = cpu.getALU().and(rx, ry);
            LOG.info("AND: {} & {} = {}", rx.value(), ry.value(), result.value());
            rx.set(result);
            registers.advance();
        }
    }

    public static class ORR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register rx = registers.getGeneralRegister(Integer.parseInt(instruction.get("Rx"), 2));
            Register ry = registers.getGeneralRegister(Integer.parseInt(instruction.get("Ry"), 2));
            Element result = cpu.getALU().or(rx, ry);
            LOG.info("ORR: {} | {} = {}", rx.value(), ry.value(), result.value());
            rx.set(result);
            registers.advance();
        }
    }

    public static class NOT extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register rx = registers.getGeneralRegister(Integer.parseInt(instruction.get("Rx"), 2));
            Element result = cpu.getALU().not(rx);
            LOG.info("NOT: {} = {}", rx.value(), result.value());
            rx.set(result);
            registers.advance();
        }
    }

    public static class SRC extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register r = registers.getGeneralRegister(Integer.parseInt(instruction.get("R"), 2));
            boolean arith = Integer.parseInt(instruction.get("A/L"), 2) == 0;
            boolean direction = Integer.parseInt(instruction.get("L/R"), 2) == 1;
            int count = Integer.parseInt(instruction.get("count"), 2);

            Element result;
            if(arith) {
                result = cpu.getALU().ashift(r, count, direction, true);
            } else {
                result = cpu.getALU().lshift(r, count, direction, true);
            }

            LOG.info("SRC: {}, {}, {}, {} = {}", r.toString(), arith ? "A" : "L", direction ? "L" : "R", count, result.toString());
            r.set(result);

            registers.advance();
        }
    }

    public static class RRC extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register r = registers.getGeneralRegister(Integer.parseInt(instruction.get("R"), 2));
            boolean direction = Integer.parseInt(instruction.get("L/R"), 2) == 1;
            int count = Integer.parseInt(instruction.get("count"), 2);
            Element result = cpu.getALU().rotate(r, count, direction, true);

            LOG.info("RRC: {}, {}, {} = {}", r.toString(), direction ? "L" : "R", count, result.toString());
            r.set(result);

            registers.advance();
        }
    }
}
