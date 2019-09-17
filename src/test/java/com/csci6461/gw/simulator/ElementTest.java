package com.csci6461.gw.simulator;

import com.csci6461.gw.simulator.util.Element;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.BitSet;

public class ElementTest {
    @Test
    public void testElement() {
        Element a = new Element(8);
        a.setByValue(5);
        a.add(3);
        assertTrue(a.value() == 8l);
        a.sub(2);
        assertTrue(a.value() == 6l);
        a.mult(3);
        assertTrue(a.value() == 18l);
        a.div(2);
        assertTrue(a.value() == 9l);
    }

    @Test
    public void testWrapAround() {
        Element w = new Element(4);
        w.setByValue(15);
        w.add(1);
        assertTrue(w.value() == 0l);
    }

    @Test
    public void testBitSet() {
        Element w = new Element(8);
        BitSet b = new BitSet(8);
        b.set(0);
        b.set(2);
        b.set(4);
        w.set(b);
        assertTrue(w.value() == 21l);
    }
}

