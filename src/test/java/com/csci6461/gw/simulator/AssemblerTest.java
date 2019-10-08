package com.csci6461.gw.simulator;

import com.csci6461.gw.simulator.instr.Assembler;
import static com.csci6461.gw.simulator.util.Exceptions.AssemblerException;
import static com.csci6461.gw.simulator.util.StringOperations.readAllBytes;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class AssemblerTest {
    @Test
    public void AsmOneLineTest() {
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

    @Test
    public void AsmTest() {
        Assembler asm = new Assembler();

        try {
            String asmcode = new String(readAllBytes(getClass().getResourceAsStream("/program1.asm")));
            String[] bitcode = asm.assemble(asmcode);
            for(int i = 0; i < bitcode.length; i++) {
                System.out.printf("%d: %s\n", i, bitcode[i]);
            }
        } catch(IOException ex) {
            ex.printStackTrace();
            assertTrue(false);
        } catch(AssemblerException e) {
            System.err.println(e.getMessage());
            assertTrue(false);
        }
    }
}
