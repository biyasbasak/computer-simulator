package com.csci6461.gw.simulator.instr.instructions;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.instr.Instruction;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;

import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class LoadStore {

    private static Logger LOG = LogManager.getLogger("Ins.LoadStore");

    public static class LDR  extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            Element value = memory.fetch(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(value.value());
            generalRegister.setByValue(registers.getAllRegisters().get("MBR").value());
            LOG.info("LDR: EA = {}, reg value = {}", effectiveAddress, generalRegister.value());
            registers.advance();
        }
    }
    public static class STR extends  Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(generalRegister.value());
            memory.set(registers.getAllRegisters().get("MAR").value(), registers.getAllRegisters().get("MBR").toString());
            LOG.info("STR: EA = {}, memory value = {}", effectiveAddress, memory.fetch(effectiveAddress));
            registers.advance();
        }
    }
    public static class LDA extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(effectiveAddress);
            generalRegister.setByValue(registers.getAllRegisters().get("MBR").value());
            LOG.info("LDA: EA = {}, reg value = {}", effectiveAddress, generalRegister.value());
            registers.advance();
        }
    }
    public static class LDX extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register indexRegister = registers.getIndexRegister(Integer.parseInt(instruction.get("indexReg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            Element value = memory.fetch(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(value.value());
            indexRegister.setByValue(registers.getAllRegisters().get("MBR").value());
            LOG.info("LDX: EA = {}, reg value = {}", effectiveAddress, indexRegister.value());
            registers.advance();
        }
    }
    public static class STX extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register indexRegister = registers.getIndexRegister(Integer.parseInt(instruction.get("indexReg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            registers.getAllRegisters().get("MAR").setByValue(effectiveAddress);
            registers.getAllRegisters().get("MBR").setByValue(indexRegister.value());
            memory.set(registers.getAllRegisters().get("MAR").value(), registers.getAllRegisters().get("MBR").toString());
            LOG.info("STX: EA = {}, reg value = {}", effectiveAddress, indexRegister.value());
            registers.advance();
        }
    }
}
