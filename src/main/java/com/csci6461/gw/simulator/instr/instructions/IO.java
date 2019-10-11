package com.csci6461.gw.simulator.instr.instructions;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.instr.Instruction;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public abstract class IO {
    private static Logger LOG = LogManager.getLogger("Instr.IO");

    public static class IN extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int devid = Integer.parseInt(instruction.get("DevID"), 2);
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            Element result = cpu.input(devid);
            generalRegister.set(result);
            LOG.info("IN: {} = {}", generalRegister.getName(), result.uvalue());
            registers.advance();
        }
    }

    public static class OUT extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            int devid = Integer.parseInt(instruction.get("DevID"), 2);
            Register generalRegister = registers.getGeneralRegister(Integer.parseInt(instruction.get("reg"), 2));
            cpu.output(devid, generalRegister);
            LOG.info("OUT: {} = {}", generalRegister.getName(), generalRegister.uvalue());
            registers.advance();
        }
    }
}
