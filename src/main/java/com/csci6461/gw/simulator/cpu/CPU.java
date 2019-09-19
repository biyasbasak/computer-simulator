package com.csci6461.gw.simulator.cpu;

import com.csci6461.gw.simulator.instr.Decoder;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.util.Element;
import com.csci6461.gw.simulator.instr.Instruction;
import static com.csci6461.gw.simulator.instr.instructions.LoadStore.*;
import static com.csci6461.gw.simulator.util.Exceptions.*;

import java.util.HashMap;

public class CPU {
    private MachineRegisters registers;
    private Memory memory;

    public CPU() {
        this.registers = new MachineRegisters();
        this.memory = new Memory();
    }

    public MachineRegisters getRegisters() {
        return registers;
    }

    public Memory getMemory() {
        return memory;
    }
    
    public void sampleTestProgram() {
        /**
         *  sample Instruction for testing
         */
        String word = "0000011100011111";
        HashMap<String, String> instruction = Decoder.decode(word);
        execute(instruction);
    }

    public void execute(HashMap<String, String> instructionData) throws CPUException {
        int opCode = Integer.parseInt(instructionData.get("opCode"), 2);
        Instruction ins;
        switch (opCode) {
            case 1:
                ins = new LDR();
                break;
            case 2:
                ins = new STR();
                break;
            case 3:
                ins = new LDA();
                break;
            case 041:
                ins = new LDX();
                break;
            case 042:
                ins = new STX();
                break;
            default:
                throw new CPUException(registers.pc(), "Unknown opcode");
        }
        ins.setInstruction(instructionData);
        ins.execute(this, this.memory, this.registers);
    }

    private void fetchInstruction() {
        Element ins = memory.fetch(registers.pc());
        registers.getRegister("IR").set(ins);
    }

    /**
     * One CPU cycle
     */
    public void cycle() {
        // 1. Fetch instruction
        fetchInstruction();
        
        // 2. Decode instruction
        HashMap<String, String> instruction = Decoder.decode(registers.getRegister("IR").toString());

        // 3. Execute instruction
        execute(instruction);
    }
}
