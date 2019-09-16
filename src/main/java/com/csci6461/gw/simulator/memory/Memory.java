package com.csci6461.gw.simulator.memory;

import java.util.BitSet;

public class Memory {

    private static final int MAX_MEMORY_SIZE = 2048;
    private static final int MAX_WORD_SIZE = 16;
    private final int memorySize;
    private final int wordSize;
    private BitSet[] memory = null;

    public Memory() {
        this.memorySize =  MAX_MEMORY_SIZE;
        this.wordSize = MAX_WORD_SIZE;
        this.memory = new BitSet[MAX_MEMORY_SIZE];
    }
    public void initialize() {
        for (int i = 0; i< MAX_MEMORY_SIZE; i++) {
            this.memory[i] = new BitSet(MAX_WORD_SIZE);
        }
    }
    public void set(int index, String word) {
        if (word.length() > this.wordSize) {
            System.out.println("Error: word size exceeds the word size limit supported by the memory");
        }
        BitSet memoryChunk = this.memory[index];
        for (int i = 0; i < word.length(); i++) {
            char bit = word.charAt(i);
            boolean bool = Character.getNumericValue(bit) == 1;
            memoryChunk.set(i, bool);
        }
    }
    public BitSet fetch(int index) {
        BitSet memoryChunk = this.memory[index];
        return memoryChunk;
    }
}
