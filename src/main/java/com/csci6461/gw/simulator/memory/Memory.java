package com.csci6461.gw.simulator.memory;

import java.util.BitSet;

public class Memory {

    private static final int MAX_MEMORY_SIZE = 2048;
    private static final int MAX_WORD_SIZE = 16;
    private final int memorySize;
    private final int wordSize;
    private static BitSet[] memory = null;

    public Memory() {
        this.memorySize =  MAX_MEMORY_SIZE;
        this.wordSize = MAX_WORD_SIZE;
    }
    public void initialize() {
        memory = new BitSet[MAX_MEMORY_SIZE];
        for (int i = 0; i< MAX_MEMORY_SIZE; i++) {
            memory[i] = new BitSet(MAX_WORD_SIZE);
        }
    }

}
