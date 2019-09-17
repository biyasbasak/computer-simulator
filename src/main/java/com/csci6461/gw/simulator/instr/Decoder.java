package com.csci6461.gw.simulator.instr;

import java.util.HashMap;

public class Decoder {
    public static HashMap<String, String> decode(String word) {
        HashMap<String, String> instruction = null;
        instruction.put("opCode", word.substring(0,6));
        instruction.put("reg", word.substring(6,8));
        instruction.put("indexReg", word.substring(8, 10));
        instruction.put("indirectBit",  word.substring(10,11));
        instruction.put("address", word.substring(11, 16));
        return instruction;
    }
}
