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
        return intToBits(num, 16, false);
    }

    /**
     * Convert a number to a BitSet
     */
    public static BitSet intToBits(int num, int nbits, boolean signed) {
        boolean minus = false;
        if(signed && num < 0) {
            minus = true;
            num = (-num) - 1;
        }

        BitSet bits = new BitSet();
        int idx = nbits - 1;
        while(idx >= 0) {
            if(num % 2 == 1) {
                bits.set(idx, !minus);
            } else {
                bits.set(idx, minus);
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
        return bitsToInt(bits, 16, false);
    }

    /**
     * 
     */
    public static int bitsToInt(BitSet bits, int nbits) {
        return bitsToInt(bits, nbits, false);
    }

    /**
     * Convert a BitSet to a number
     */
    public static int bitsToInt(BitSet bits, int nbits, boolean signed) {
        int num = 0;

        if(!bits.get(0) || !signed) {
            for(int i = nbits - 1; i >= 0; i--) {
                if(bits.get(i)) {
                    num = num | (1 << (nbits - (i + 1)));
                }
            }
        } else {  // minus
            for(int i = nbits -1; i >= 0; i--) {
                if(!bits.get(i)) {
                    num = num | (1 << (nbits - (i + 1)));
                }
            }
            num += 1;
            num = -num;
        }
        return num;
    }

    /**
     * Convert a string to a BitSet
     */
    public static BitSet stringToBits(String s, int nbits) {
        BitSet b = new BitSet();
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(s.length() - i - 1) == '1') {
                b.set(nbits - i - 1);
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

    /**
     * Convert an integer to an nbits binary string
     */
    public static String intToString(int num, int nbits) {
        String r = Integer.toBinaryString(num);
        
        if(r.length() > nbits) {
            throw new RuntimeException("Integer bitsize larger than nbits");
        }

        while(r.length() < nbits) {
            r = "0" + r;
        }
        return r;
    }
}

