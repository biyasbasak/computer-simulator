package com.csci6461.gw.simulator.cpu;

import com.csci6461.gw.simulator.instr.Decoder;
import com.csci6461.gw.simulator.instr.instructions.LoadStore;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.memory.Memory;

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
    public void decodeInstruction(String word) {

    }
    public void sampleTestProgram() {
        /**
         *  sample Instruction for testing
         */
        String word = "0000011100011111";
        HashMap<String, String> instruction = Decoder.decode(word);
        execute(instruction);
    }

    public void execute(HashMap<String, String> instructionData) {
        String opCode = instructionData.get("opCode");
        switch (opCode) {
            case "000001":
                LoadStore.LDR ldr = new LoadStore.LDR();
                ldr.setInstruction(instructionData);
                ldr.execute(this, this.memory, this.registers);
        }
    }
}
