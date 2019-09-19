package com.csci6461.gw.simulator;

import com.csci6461.gw.simulator.instr.Assembler;
import static org.junit.Assert.*;
import org.junit.Test;

public class AssemblerTest {
    @Test
    public void AsmTest() {
        Assembler asm = new Assembler();

        String bitcode = asm.assembleOne(0, "ldr r0, x0, 5");
        assertTrue(bitcode.equals("0000010000000101"));

        try {
            String whitespace_test = asm.assembleOne(0,  "ldr r0, x0, 5       \t");
            assertTrue(bitcode.equals(whitespace_test));
        } catch(RuntimeException ex) {
            System.out.println(ex.getMessage());
            assertTrue(false);
        }
    }
}
