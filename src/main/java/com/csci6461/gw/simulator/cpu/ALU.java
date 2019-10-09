package com.csci6461.gw.simulator.cpu;

import org.apache.logging.log4j.Logger; 
import org.apache.logging.log4j.LogManager; 

/**
 * Arithmetic logic unit
 */
public class ALU {
    private static Logger LOG = LogManager.getLogger("CPU.ALU");
    private static final String ZERO = "0000000000000000";

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

    public String addition(String addend1, String addend2) {
        return addition(addend1, addend2, true);
    }

    /**
     * Perform binary addition
     */
    public String addition(String addend1, String addend2, boolean cc) {
        String result = "";
        boolean carry = false;

        assert addend1.length() == 16 && addend2.length() == 16;
        for(int i = 15; i >= 0; i--) {
            if(addend1.charAt(i) == '1' && addend2.charAt(i) == '1') {
                if(carry) {
                    result = "1" + result;
                } else {
                    result = "0" + result;
                }
                carry = true;
            } else if((addend1.charAt(i) == '0' && addend2.charAt(i) == '1') 
                    || (addend1.charAt(i) == '1' && addend2.charAt(i) == '0')) {
                if(carry) {
                    result = "0" + result;
                } else {
                    result = "1" + result;
                    carry = false;
                }
            } else {
                if(carry) {
                    result = "1" + result;
                } else {
                    result = "0" + result;
                }
                carry = false;
            }
        }
        if(cc) {
            updateCC(carry, false, false, result.equals(ZERO));
        }
        return result;
    }

    public String subtraction(String minuend, String subtrahend) {
        return subtraction(minuend, subtrahend, true);
    }

    /**
     * Perform the subtraction
     */
    public String subtraction(String minuend, String subtrahend, boolean cc) {
        String result = "";
        boolean borrow = false;

        assert minuend.length() == 16 && subtrahend.length() == 16;
        for(int i = 15; i >= 0; i--) {
            if(minuend.charAt(i) == '1') {

            }
        }
        return result;
    }
}
