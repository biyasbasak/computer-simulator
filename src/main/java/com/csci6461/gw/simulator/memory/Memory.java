package com.csci6461.gw.simulator.memory;

import com.csci6461.gw.simulator.instr.Instruction;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element; 

import java.util.BitSet;
import java.util.HashMap;

public class Memory {

    private static final int MAX_MEMORY_SIZE = 2048;
    private static final int MAX_WORD_SIZE = 16;
    private final int memorySize;
    private final int wordSize;
    private Element[] memory = null;

    public Memory() {
        this.memorySize =  MAX_MEMORY_SIZE;
        this.wordSize = MAX_WORD_SIZE;
        this.memory = new Element[MAX_MEMORY_SIZE];
    }
    public void initialize() {
        for (int i = 0; i< MAX_MEMORY_SIZE; i++) {
            this.memory[i] = new Element(MAX_WORD_SIZE);
        }
    }
    public void set(int index, String word) {
        if (word.length() > this.wordSize) {
            System.out.println("Error: word size exceeds the word size limit supported by the memory");
        }
        Element memoryChunk = this.memory[index];
        for (int i = 0; i < word.length(); i++) {
            char bit = word.charAt(i);
            boolean bool = Character.getNumericValue(bit) == 1;
            memoryChunk.set(i, bool);
        }
    }
    public Element fetch(int index) {
        Element memoryChunk = this.memory[index];
        return memoryChunk;
    }
    public int calculateEffectiveAddress(MachineRegisters registers, HashMap<String, String> instruction) {
        int effectiveAddress = 0;
        String address = instruction.get("address");
        String indirectBit = instruction.get("indirectBit");
        String indexReg = instruction.get("indexReg");
        if (Integer.parseInt(indirectBit, 2) == 0) {
            if (Integer.parseInt(indexReg, 2) == 0) {
                effectiveAddress = Integer.parseInt(address, 2);
                return effectiveAddress;
            } else {
                Register register = registers.getIndexRegister(Integer.parseInt(indexReg, 2));
                long registerValue = register.value();
                // fetch contents of index register
            }

        } else {

        }
        return effectiveAddress;
    }
}
