package com.csci6461.gw.simulator;

import com.csci6461.gw.simulator.util.BitOperations;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.BitSet;

public class BitOperationTest {
    @Test
    public void testL2B() {
        int value = 0x449, idx = 0;
        BitSet b = BitOperations.intToBits(value);
        while(value != 0) {
            int u = b.get(idx) ? 1 : 0;
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
        int value = BitOperations.bitsToInt(b);
        assertTrue(value == 149);
    }
}

