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
            } else {
                carry = 0;
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
        int a = multiplicand.value();
        int b = multiplier.value();
        Element result = Element.fromInt(a * b, 32);

        if(cc) {
            boolean overflow = false;
            for(int i = 0; i < 16; i++) {
                if(result.get(i)) {
                    overflow = true;
                }
            }
            updateCC(overflow, false, false, result.value() == 0);
        }
        return result;
    }

    /**
     * Perform division
     */
    public Element divide(Element dividend, Element divisor, boolean cc) {
        int a = dividend.value();
        int b = divisor.value();

        Element quotient = Element.fromInt(a / b, 16);
        Element remainder = Element.fromInt(a % b, 16);

        Element result = Element.fromInt(0, 32);
        for(int i = 0; i < 16; i++) {
            result.set(i, quotient.get(i));
            result.set(i + 16, remainder.get(i));
        }

        if(cc) {
            updateCC(false, false, false, quotient.toString().equals(ZERO));
        }
        return result;
    }

    /**
     * Logical shift
     */
    public Element lshift(Element elem, int count, boolean direct, boolean cc) {
        Element result = Element.fromString(ZERO);

        if(direct) {    // left shift
            int end = 16 + count;
            int start = end - 16;
            for(int i = 0; i < 16; i++) {
                result.set(i, elem.get(start + i));
            }
            if(cc) {
                boolean overflow = false;
                for(int i = 0; i < start; i++) {
                    if(elem.get(i)) {
                        overflow = true;
                    }
                }
                updateCC(overflow, false, false, result.toString().equals(ZERO));
            }
        } else {    // right shift
            int start = count;
            for(int i = 0; i < 16; i++) {
                if(i - start >= 0) {
                    result.set(i, elem.get(i - start));
                }
            }

            if(cc) {
                boolean underflow = false;
                for(int i = 16 - start; i < 16; i++) {
                    if(elem.get(i)) {
                        underflow = true;
                    }
                }
                updateCC(false, underflow, false, result.toString().equals(ZERO));
            }
        }
        return result;
    }

    /**
     * Arithmetic shift 
     */
    public Element ashift(Element elem, int count, boolean direct, boolean cc) {
        if(!direct && elem.get(0)) {
            // right shift with msb set
            Element result = lshift(elem, count, direct, cc);     // CC passover
            for(int i = 0; i < count; i++) {
                result.set(i);
            }
            return result;
        }
        return lshift(elem, count, direct, cc);
    }

    /**
     * Rotate
     */
    public Element rotate(Element elem, int count, boolean direct, boolean cc) {
        Element result = Element.fromString(ZERO);

        if(direct) {    // left
            for(int i = 0; i < 16; i++) {
                result.set(i, elem.get((i + count) % 16));
            }
        } else {    // right
            for(int i = 0; i < 16; i++) {
                result.set(i, elem.get(Math.floorMod(i - count, 16)));
            }
        }

        if(cc) {
            updateCC(false, false, false, result.toString().equals(ZERO));
        }
        return result;
    }
}
