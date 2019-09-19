package com.csci6461.gw.simulator.instr;

import static com.csci6461.gw.simulator.util.Exceptions.*;
import static com.csci6461.gw.simulator.util.BitOperations.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Assembler {

    private static Logger LOG = LogManager.getLogger("Ins.Assembler");

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

    private HashMap<String, Integer> labelMap;

    private int pc;

    public Assembler() {
        labelMap = new HashMap<>();
        pc = 0;
    }

    private String readUntil(Scanner scan, char delim) {
        String r = "";
        while(scan.hasNext()) {
            char c = scan.next().charAt(0);
            if(c == delim) {
                skipWhitespace(scan);
                return r;
            }
            r += c;
        }
        return r;
    }

    private void skipWhitespace(Scanner scan) {
        while(scan.hasNext("\\s")) {
            scan.next();
        }
        return;
    }

    public String assemble(String source) throws AssemblerException {
        Scanner scan = new Scanner(source);
        List<String> result = new ArrayList<>();
        int lineno = 0;

        while(scan.hasNextLine()) {
            String line = scan.nextLine().stripTrailing().stripLeading();
            lineno++;
            if(line.endsWith(":")) {  // a label
                String labelName = line.substring(0, line.length());
                labelMap.put(labelName, pc);
            } else if(line.startsWith("!")) {
                handlePseudoInstruction(lineno, line.substring(1));
            } else {
                result.add(assembleOne(lineno, line));
                pc += 1;
            }
        }

        scan.close();
        return String.join("\n", result);
    }

    public void handlePseudoInstruction(int lineno, String line) {
        Scanner scan = new Scanner(line);
        scan.useDelimiter("");

        String command = readUntil(scan, ' ').toUpperCase();
        switch(command) {
            case "ORG":   // set base address
                int newpc = Integer.decode(readUntil(scan, ' '));
                pc = newpc;
                break;
            default:
                break;
        }
        return;
    }

    private int regNameToIndex(int lineno, String name) throws AssemblerException {
        switch(name) {
            case "R0":
            case "X0":
                return 0;
            case "R1":
            case "X1":
                return 1;
            case "R2":
            case "X2":
                return 2;
            case "R3":
            case "X3":
                return 3;
            default:
                throw new AssemblerException(lineno, "Unknown register");
        }
    }

    private String packFormat1(int gr, int xr, Boolean indirect, int address) {
        return intToString(gr, 2) + intToString(xr, 2) + (indirect ? "1" : "0") + intToString(address, 5);
    }

    public String assembleOne(int lineno, String line) throws AssemblerException {
        Scanner scan = new Scanner(line);
        scan.useDelimiter("");  // read one by one

        String mnenomic = readUntil(scan, ' ').toUpperCase();
        String result = "";
        
        Integer opcode = OPCODE_LIST.get(mnenomic);
        if(opcode == null) {
            throw new AssemblerException(lineno, "Invalid instruction");
        }

        result += intToString(opcode, 6);

        int gr, xr, address;
        String indirect;

        // TODO
        switch(mnenomic) {
            case "HLT":
                result += "0000000000";
                break;
            case "LDR":
            case "STR":
            case "LDA":
                gr = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                xr = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                address = Integer.decode(readUntil(scan, ','));
                indirect = readUntil(scan, ',').toUpperCase();
                result += packFormat1(gr, xr, indirect == "I", address);
                break;
            case "LDX":
            case "STX":
                xr = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                address = Integer.decode(readUntil(scan, ','));
                indirect = readUntil(scan, ',').toUpperCase();
                result += packFormat1(0, xr, indirect == "I", address);
                break;
            default:
                throw new AssemblerException(lineno, "Unknown mnenomic");
        }

        assert result.length() == 16;
        scan.close();
        return result;
    }
}
