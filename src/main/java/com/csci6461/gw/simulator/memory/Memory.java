package com.csci6461.gw.simulator.memory;

import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;
import static com.csci6461.gw.simulator.util.Exceptions.*; 

import java.util.BitSet;
import java.util.HashMap;

public class Memory {

    private static final int MAX_MEMORY_SIZE = 2048;
    private static final int MAX_WORD_SIZE = 16;
    private final int memorySize;
    private final int wordSize;
    private Element[] memory = null;
    private Cache cache;

    public Memory() {
        this.memorySize =  MAX_MEMORY_SIZE;
        this.wordSize = MAX_WORD_SIZE;
        this.memory = new Element[MAX_MEMORY_SIZE];
        this.cache = new Cache(this);
        initialize();
    }

    public void initialize() {
        for (int i = 0; i< this.memorySize; i++) {
            this.memory[i] = new Element(this.wordSize);
        }
    }

    /**
     * Store memory by binary string 
     */
    public void set(int index, String word) {
        Element elem = Element.fromString(word);
        this.store(index, elem);
    }

    /**
     * Store memory directly by binary string
     */
    public void set_direct(int index, String word) {
        Element elem = Element.fromString(word);
        this.store_direct(index, elem);
    }

    /**
     * Store memory 
     */
    public void store(int index, Element elem) {
        if(index >= this.memorySize) {
            throw new MemoryException(index, "Out-of-bounds write");
        }
        cache.store(index, elem);
        return;
    }

    /**
     * Fetch memory
     */
    public Element fetch(int index) {
        if(index >= this.memorySize) {
            throw new MemoryException(index, "Out-of-bounds read");
        }
        return cache.fetch(index);
    }

    /**
     * Bypassing the cache, read directly from the memory 
     */
    public Element fetch_direct(int address) {
        if(address >= this.memorySize) {
            throw new MemoryException(address, "Out-of-bounds read");
        }
        return this.memory[address];
    }

    /**
     * Bypassing the cache, write directly to the memory 
     */
    public void store_direct(int address, Element element) {
        if(address >= this.memorySize) {
            throw new MemoryException(address, "Out-of-bounds write");
        }
        this.memory[address] = element;
    }

    /**
     * Calculate effective address
     */
    public int calculateEffectiveAddress(MachineRegisters registers, HashMap<String, String> instruction) {
        int effectiveAddress = 0;
        String opCode = instruction.get("opCode");
        String address = instruction.get("address");
        String indirectBit = instruction.get("indirectBit");
        String indexReg = instruction.get("indexReg");

        // In case of LDX and STX index Registers are ignored
        if (Integer.parseInt(opCode, 2) == 041 || Integer.parseInt(opCode, 2) == 042) {
            if (Integer.parseInt(indirectBit, 2) == 0) {
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
                int registerValue = register.uvalue();
                effectiveAddress = registerValue + Integer.parseInt(address, 2);
            }
        } else {
            if (Integer.parseInt(indexReg, 2) == 0) {
                Element memoryChunk = fetch(Integer.parseInt(address, 2));
                effectiveAddress =  Integer.parseInt(memoryChunk.toString(), 2);
            } else {
                Register register = registers.getIndexRegister(Integer.parseInt(indexReg, 2));
                int registerValue = register.uvalue();
                int registerAndAddressValue = registerValue + Integer.parseInt(address, 2);
                Element memoryChunk = fetch(registerAndAddressValue);
                effectiveAddress = Integer.parseInt(memoryChunk.toString(), 2);
            }
        }
        return effectiveAddress;
    }

    /**
     * Get the size of memory
     */
    public int size() {
        return this.memorySize;
    }

    /**
     * Get the cache object
     */
    public Cache getCache() {
        return cache;
    }
}
