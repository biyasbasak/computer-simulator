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
            Element value = memory.fetch(effectiveAddress);
            generalRegister.set(value);
            LOG.info("LDR: EA = {}, reg value = {}", effectiveAddress, generalRegister.value());
        }
    }
    public static class STR extends  Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            memory.set(effectiveAddress, Integer.toBinaryString(generalRegister.value()));
            LOG.info("STR: EA = {}, memory value = {}", effectiveAddress, memory.fetch(effectiveAddress));
        }
    }
    public static class LDA extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            generalRegister.set(effectiveAddress);
            LOG.info("LDA: EA = {}, reg value = {}", effectiveAddress, generalRegister.value());
        }
    }
    public static class LDX extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register indexRegister = registers.getIndexRegister(Integer.parseInt(instruction.get("indexReg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            Element value = memory.fetch(effectiveAddress);
            indexRegister.set(value);
            LOG.info("LDX: EA = {}, reg value = {}", effectiveAddress, indexRegister.value());
        }
    }
    public static class STX extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register indexRegister = registers.getIndexRegister(Integer.parseInt(instruction.get("indexReg"), 2));
            int effectiveAddress = memory.calculateEffectiveAddress(registers, instruction);
            memory.set(effectiveAddress, Integer.toBinaryString(indexRegister.value()));
            LOG.info("LDX: EA = {}, reg value = {}", effectiveAddress, indexRegister.value());
        }
    }
}
