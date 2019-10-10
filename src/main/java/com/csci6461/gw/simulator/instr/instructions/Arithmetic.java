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

public class Arithmetic {
    private static Logger LOG = LogManager.getLogger("Instr.Arithmetic");

    public static class AMR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            return;
        }
    }

    public static class SMR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            return;
        }
    }

    public static class AIR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            return;
        }
    }

    public static class SIR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            return;
        }
    }

    public static class MLT extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            return;
        }
    }

    public static class DVD extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            return;
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
            return;
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
            return;
        }
    }

    public static class OR extends Instruction {
        @Override
        public void execute(CPU cpu, Memory memory, MachineRegisters registers) {
            HashMap<String, String> instruction = this.getInstruction();
            Register rx = registers.getGeneralRegister(Integer.parseInt(instruction.get("Rx"), 2));
            Register ry = registers.getGeneralRegister(Integer.parseInt(instruction.get("Ry"), 2));
            Element result = cpu.getALU().or(rx, ry);
            LOG.info("OR: {} & {} = {}", rx.value(), ry.value(), result.value());
            rx.set(result);
            return;
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
            return;
        }
    }
}
