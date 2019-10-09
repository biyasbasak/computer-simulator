package com.csci6461.gw.simulator.instr;

import java.util.HashMap;

public class Decoder {
    public static HashMap<String, String> decode(String word) {
        HashMap<String, String> instruction = new HashMap<>();
        
        // OpCode
        instruction.put("opCode", word.substring(0,6));
        
        // Type 0 format
        instruction.put("trapCode", word.substring(12, 16));

        // Type 1 format
        instruction.put("reg", word.substring(6,8));
        instruction.put("indexReg", word.substring(8, 10));
        instruction.put("indirectBit",  word.substring(10,11));
        instruction.put("address", word.substring(11, 16));

        // Type 2 format
        instruction.put("Rx", word.substring(6, 8));
        instruction.put("Ry", word.substring(8, 10));
        
        // Type 3 & 4 common
        instruction.put("R", word.substring(6, 8));

        // Type 3 format
        instruction.put("A/L", word.substring(8, 9));
        instruction.put("L/R", word.substring(9, 10));
        instruction.put("count", word.substring(12, 16));

        // Type 4 format
        instruction.put("DevID", word.substring(11, 16));

        return instruction;
    }
}
