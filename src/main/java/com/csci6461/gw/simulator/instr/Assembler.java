package com.csci6461.gw.simulator.instr;

import static com.csci6461.gw.simulator.util.Exceptions.*;
import java.util.HashMap;
import java.util.Scanner;

public class Assembler {

    private static final HashMap<String, Integer> OPCODE_LIST = new HashMap<>() {{
       put("HLT", 0);
       put("TRAP", 30);
       put("LDR", 1);
       put("STR", 2);
       put("LDA", 3);
       put("LDX", 33);
       put("STX", 34);
       put("JZ", 8);
       put("JNE", 9);
       put("JCC", 10);
       put("JMA", 11);
       put("JSR", 12);
       put("RFS", 13);
       put("SOB", 14);
       put("JGE", 15);
       put("AMR", 4);
       put("SMR", 5);
       put("AIR", 6);
       put("SIR", 7);
       put("MLT", 16);
       put("DVD", 17);
       put("TRR", 18);
       put("AND", 19);
       put("ORR", 20);
       put("NOT", 21);
       put("SRC", 25);
       put("RRC", 26);
       put("IN", 49);
       put("OUT", 50);
       put("CHK", 51);
       put("FADD", 27);
       put("FSUB", 28);
       put("VADD", 29);
       put("VSUB", 30);
       put("CNVRT", 31);
       put("LDFR", 40);
       put("STFR", 41);
    }};

    private String readUntil(Scanner scan, char delim) {
        String r = "";
        while(true) {
            char c = scan.next().charAt(0);
            if(c == delim) {
                return r;
            }
            r += c;
        }
    }

    public String assembleOne(int lineno, String line) throws AssemblerException {
        Scanner scan = new Scanner(line);
        scan.useDelimiter("");  // read one by one

        String mnenomic = readUntil(scan, ' ');
        
        Integer opcode = OPCODE_LIST.get(mnenomic);
        if(opcode == null) {
            throw new AssemblerException(lineno, "Invalid instruction");
        }

        // TODO

        return "";
    }
}
