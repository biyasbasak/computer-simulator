package com.csci6461.gw.simulator.util;

import java.util.BitSet;

/**
 * Utility class dealing with bit operations
 */
public class BitOperations {
    /**
     * Convert a number to a 16-bit BitSet
     */
    public static BitSet intToBits(int num) {
        return intToBits(num, 16);
    }

    /**
     * Convert a number to a BitSet
     */
    public static BitSet intToBits(int num, int nbits) {
        BitSet bits = new BitSet();
        int idx = nbits - 1;
        while(num != 0 && idx >= 0) {
            if(num % 2 == 1) {
                bits.set(idx);
            }
            num >>>= 1;
            idx -= 1;
        }
        return bits;
    }

    /**
     * Convert a BitSet to a 16-bit number
     */
    public static int bitsToInt(BitSet bits) {
        return bitsToInt(bits, 16);
    }

    /**
     * Convert a BitSet to a number
     */
    public static int bitsToInt(BitSet bits, int nbits) {
        int num = 0;
        for(int i = nbits - 1; i >= 0; i--) {
            if(bits.get(i)) {
                num = num | (1 << (nbits - (i + 1)));
            }
        }
        return num;
    }

    /**
     * Convert a string to a BitSet
     */
    public static BitSet stringToBits(String s) {
        BitSet b = new BitSet();
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '1') {
                b.set(i);
            }
        }
        return b;
    }

    /**
     * Convert a binary string to an integer
     */
    public static int stringToInt(String s) {
        int r = 0, idx = 0;
        for(int i = s.length() - 1; i >= 0; i--, idx++) {
            if(s.charAt(i) == '1') {
                r = r | (1 << idx);
            }
        }
        return r;
    }
}

