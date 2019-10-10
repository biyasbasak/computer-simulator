package com.csci6461.gw.simulator.cpu;

import org.apache.logging.log4j.Logger; 
import org.apache.logging.log4j.LogManager; 

import com.csci6461.gw.simulator.util.Element; 

/**
 * Arithmetic logic unit
 */
public class ALU {
    private static Logger LOG = LogManager.getLogger("CPU.ALU");
    private static final String ZERO = "0000000000000000";
    private static final String MINUS_ONE = "1111111111111111";
    private static final String ONE = "0000000000000001";

    private CPU cpu;

    public ALU(CPU cpu) {
        this.cpu = cpu;
    }

    /**
     * Update CC register
     */
    private void updateCC(boolean overflow, boolean underflow, boolean divzero, boolean eq) {
        cpu.getRegisters().setOverflow(overflow);
        cpu.getRegisters().setUnderflow(underflow);
        cpu.getRegisters().setDivZero(divzero);
        cpu.getRegisters().setEqual(eq);
    }

    /**
     * Perform binary addition
     */
    public Element addition(Element addend1, Element addend2) {
        return addition(addend1, addend2, true);
    }

    /**
     * Perform binary addition
     */
    public Element addition(Element addend1, Element addend2, boolean cc) {
        Element result = Element.fromString(ZERO);

        int cin = 0, cout = 0;
        int carry = 0;

        for(int i = 15; i >= 0; i--) {
            int r = carry + (addend1.get(i) ? 1 : 0) + (addend2.get(i) ? 1 : 0);
            if(r >= 2) {
                carry = 1;
            }

            if((r & 1) == 1) {
                result.set(i, true);
            }

            if(i == 1) {
                cin = carry;
            } else if(i == 0) {
                cout = carry;
            }
        }

        if(cc) {
            updateCC(cin != cout, false, false, result.toString().equals(ZERO));
        }
        return result;
    }

    /**
     * Perform bitwise xor
     */
    public Element xor(Element e1, Element e2) {
        return xor(e1, e2, true);
    }

    /**
     * Perform bitwise xor
     */
    public Element xor(Element e1, Element e2, boolean cc) {
        Element result = Element.fromString(ZERO);

        for(int i = 15; i >= 0; i--) {
            result.set(i, e1.get(i) ^ e2.get(i));
        }

        if(cc) {
            updateCC(false, false, false, result.toString().equals(ZERO));
        }
        return result;
    }

    /**
     * Perform bitwise and
     */
    public Element and(Element e1, Element e2) {
        return and(e1, e2, true);
    }

    /**
     * Perform bitwise and
     */
    public Element and(Element e1, Element e2, boolean cc) {
        Element result = Element.fromString(ZERO);

        for(int i = 15; i >= 0; i--) {
            result.set(i, e1.get(i) & e2.get(i));
        }

        if(cc) {
            updateCC(false, false, false, result.toString().equals(ZERO));
        }
        return result;
    }

    /**
     * Perform bitwise or
     */
    public Element or(Element e1, Element e2) {
        return or(e1, e2, true);
    }

    /**
     * Perform bitwise or
     */
    public Element or(Element e1, Element e2, boolean cc) {
        Element result = Element.fromString(ZERO);

        for(int i = 15; i >= 0; i--) {
            result.set(i, e1.get(i) | e2.get(i));
        }

        if(cc) {
            updateCC(false, false, false, result.toString().equals(ZERO));
        }
        return result;
    }

    /**
     * Perform bitwise not
     */
    public Element not(Element e) {
        return not(e, true);
    }

    /**
     * Perform bitwise not 
     */
    public Element not(Element e, boolean cc) {
        Element result = Element.fromString(ZERO);

        for(int i = 15; i >= 0; i--) {
            result.set(i, !e.get(i));
        }

        if(cc) {
            updateCC(false, false, false, result.toString().equals(ZERO));
        }
        return result;
    }

    /**
     * Test
     */
    public void test(Element e1, Element e2) {
        updateCC(false, false, false, e1.toString().equals(e2.toString()));
        return;
    }

    /**
     * 2's complement negation
     */
    public Element negate(Element e) {
        Element result = e.clone();
        result.xor(Element.fromString(MINUS_ONE));
        return addition(result, Element.fromString(ONE), false);
    }

    /**
     * Perform binary subtraction
     */
    public Element subtraction(Element minuend, Element subtrahend) {
        return subtraction(minuend, subtrahend, true);
    }

    /**
     * Perform binary subtraction
     */
    public Element subtraction(Element minuend, Element subtrahend, boolean cc) {
        Element neg_subtrahend = negate(subtrahend);
        return addition(minuend, neg_subtrahend, cc);   // a - b = a + (-b)
    }
    
    /**
     * Perform multiplication
     */
    public Element multiply(Element multiplicand, Element multiplier, boolean cc) {
        // TODO
        return null;
    }

    /**
     * Perform division
     */
    public Element divide(Element dividend, Element divisor, boolean cc) {
        // TODO
        return null;
    }
}
