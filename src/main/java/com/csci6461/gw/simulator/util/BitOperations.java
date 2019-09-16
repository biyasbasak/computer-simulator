package com.csci6461.gw.simulator.util;

import java.util.BitSet;

/**
 * Utility class dealing with bit operations
 */
public class BitOperations {
    /**
     * Convert a number to a BitSet
     */
    public static BitSet longToBits(long num) {
        BitSet bits = new BitSet();
        int idx = 0;
        while(num != 0) {
            if(num % 2 == 1) {
                bits.set(idx);
            }
            num /= 2;
        }
        return bits;
    }

    /**
     * Convert a BitSet to a number
     */
    public static long bitsToLong(BitSet bits) {
        long num = 0;
        for(int i = 0; i < bits.size(); i++) {
            if(bits.get(i)) {
                num = num | (1 << i);
            }
        }
        return num;
    }
}
