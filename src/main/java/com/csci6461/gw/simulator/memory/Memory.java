package com.csci6461.gw.simulator.memory;

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
        for (int i = 0; i< this.memorySize; i++) {
            this.memory[i] = new Element(this.wordSize);
        }
    }

    public void set(int index, String word) {
        if (word.length() > this.wordSize) {
            System.out.println("Error: word size exceeds the word size limit supported by the memory");
        }
        Element memoryChunk = this.memory[index];
        memoryChunk.setByValue(Integer.parseInt(word, 2));
    }
    public Element fetch(int index) {
        Element memoryChunk = this.memory[index];
        return memoryChunk;
    }
    public int calculateEffectiveAddress(MachineRegisters registers, HashMap<String, String> instruction) {
        int effectiveAddress = 0;
        String opCode = instruction.get("opCode");
        String address = instruction.get("address");
        String indirectBit = instruction.get("indirectBit");
        String indexReg = instruction.get("indexReg");

        // In case of LDX and STX index Registers are ignored
        if (Integer.parseInt(opCode, 2) == 041 || Integer.parseInt(opCode, 2) == 042    ) {
            if (Integer.parseInt(indexReg, 2) == 0) {
                effectiveAddress = Integer.parseInt(address, 2);
            } else {
                Element memoryChunk = fetch(Integer.parseInt(address,2));
                effectiveAddress = Integer.parseInt(memoryChunk.toString(), 2);
            }
            return  effectiveAddress;
        }
        // if indirect bit is not set
        if (Integer.parseInt(indirectBit, 2) == 0) {
            // if index reg is also not set
            if (Integer.parseInt(indexReg, 2) == 0) {
                effectiveAddress = Integer.parseInt(address, 2);
            } else {
                Register register = registers.getIndexRegister(Integer.parseInt(indexReg, 2));
                int registerValue = register.value();
                effectiveAddress = registerValue + Integer.parseInt(address, 2);
            }
        } else {
            if (Integer.parseInt(indexReg, 2) == 0) {
                Element memoryChunk = fetch(Integer.parseInt(address, 2));
                effectiveAddress =  Integer.parseInt(memoryChunk.toString(), 2);
            } else {
                Register register = registers.getIndexRegister(Integer.parseInt(indexReg, 2));
                int registerValue = register.value();
                int registerAndAddressValue = registerValue + Integer.parseInt(address, 2);
                Element memoryChunk = fetch(registerAndAddressValue);
                effectiveAddress = Integer.parseInt(memoryChunk.toString(), 2);
            }
        }
        return effectiveAddress;
    }
}
