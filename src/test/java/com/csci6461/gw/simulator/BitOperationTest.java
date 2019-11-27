package com.csci6461.gw.simulator;

import com.csci6461.gw.simulator.util.BitOperations;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.BitSet;

public class BitOperationTest {
    @Test
    public void testL2B() {
        int value = 0x449, idx = 0;
        BitSet b = BitOperations.intToBits(value, 16, false);
        while(value != 0) {
            int u = b.get(16 - (idx + 1)) ? 1 : 0;
            int v = value & 1;
            assertTrue(u == v);
            value /= 2;
            idx += 1;
        }
    }

    @Test 
    public void testB2L() {
        BitSet b = new BitSet();
        int[] bits = {0, 2, 4, 7};
        for(int bit : bits) {
            b.set(bit);
        }
        int value = BitOperations.bitsToInt(b, 8);
        assertTrue(value == 169);
    }

    @Test
    public void testEquality() {
        BitSet b = new BitSet();
        int[] bits = {0, 2, 4, 7};
        for(int bit : bits) {
            b.set(bit);
        }
    }

    @Test
    public void testB2LSigned() {
        BitSet b = new BitSet();
        int[] bits = {0, 1, 2, 3};
        for(int bit : bits) {
            b.set(bit);
        }
        int value = BitOperations.bitsToInt(b, 4, true);
        assertTrue(value == -1);
    }

    @Test
    public void testB2LSigned2() {
        BitSet b = new BitSet();
        int[] bits = {0, 1, 2};
        for(int bit : bits) {
            b.set(bit);
        }
        int value = BitOperations.bitsToInt(b, 4, true);
        assertTrue(value == -2);
    }

    @Test
    public void testL2BSigned() {
        int v = -1;
        BitSet r = BitOperations.intToBits(v, 16, true);
        for(int i = 0; i < 16; i++) {
            assertTrue(r.get(i));
        }
    }
    
    @Test
    public void testL2BSigned2() {
        int v = -2;
        BitSet r = BitOperations.intToBits(v, 16, true);
        for(int i = 0; i < 15; i++) {
            assertTrue(r.get(i));
        }
        assertFalse(r.get(15));
    }

    @Test
    public void testF2B() {
        double f = 0.75;
        BitSet r = BitOperations.floatToBits(f);
        System.out.println(r.toString());
        System.out.println(BitOperations.bitsToFloat(r));
    }
}

