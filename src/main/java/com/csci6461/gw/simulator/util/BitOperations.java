package com.csci6461.gw.simulator.util;

import java.util.BitSet;
import java.lang.Math;

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

    /**
     * Convert a 16-bit BitSet to floating point
     */
    public static double bitsToFloat(BitSet bits) {
        boolean signed = bits.get(0);
        int exponent = bitsToInt(bits.get(1, 8), 7);
        boolean denormal = exponent == 0;

        if(exponent == 127) {   // all 1
            return Double.NaN;
        }

        int e = exponent - 63;
        if(denormal) {  // denormalized
            e += 1;
        }

        // get mantissa
        double fraction = 0.0, factor = 1.0;
        for(int i = 8; i < 16; i++) {
            factor = factor / 2;
            if(bits.get(i)) {
                fraction += 1.0 * factor;
            }
        }

        if(!denormal) {
            fraction += 1.0;
        }

        double result = Math.pow(2.0, (double)e) * fraction;
        if(signed) {
            result = -result;
        }
        return result;
    }

    /**
     * Convert a double-precision to a 16-bit BitSet
     */
    public static BitSet floatToBits(double value) {
        BitSet bits = new BitSet(16);
        BitSet nan = new BitSet(16);
        for(int i = 1; i <= 7; i++) {
            nan.set(i);
        }
        
        boolean signed = value < 0;

        if(value == Double.NaN) {
            return nan;
        }

        long b = Double.doubleToLongBits(value);
        long mantissa = b & 0x000fffffffffffffL;
        long exponent = (b & 0x7ff0000000000000L) >> 52;
        long e;
        
        if(exponent == 0) {     // normalized
            e = -63;
        } else {
            e = exponent - 1023;
            if(e > 64 || e < -62) {
                return nan;
            }
        }

        long significand = mantissa >> 44;
        for(int i = 8; i < 16; i++) {
            if((significand >> (7 - (i - 8))) % 2 != 0) {
                bits.set(i);
            }
        }

        long exp = e + 63;
        for(int i = 1; i <= 7; i++) {
            if((exp >> (6 - (i - 1))) % 2 != 0) {
                bits.set(i);
            }
        }

        if(signed) {
            bits.set(0);
        }
        return bits;
    }
}

