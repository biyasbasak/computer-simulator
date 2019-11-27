package com.csci6461.gw.simulator.instr;

import static com.csci6461.gw.simulator.util.Exceptions.*;
import static com.csci6461.gw.simulator.util.BitOperations.*;
import static com.csci6461.gw.simulator.util.StringOperations.*;
import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.util.Element;

import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * LIAR: Low-quality Improvised Assembler Reloaded
 */
public class Assembler {
    private static Logger LOG = LogManager.getLogger("Ins.Assembler");

    /**
     * Opcodes by its name.
     */
    private static final HashMap<String, Integer> OPCODE_LIST = new HashMap<String, Integer>() {{
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
        put("VADD", 043);
        put("VSUB", 044);
        put("CNVRT", 31);
        put("LDFR", 40);
        put("STFR", 41);
    }};

    private HashMap<String, Integer> labelMap;

    /*
     * Address map
     * [0, 8) - reserved
     * 8 - use as zero memory
     * 9 - stack pointer
     * 10 - temporary value
     * [0x700, 0x800) - stack memory
     */

    public Assembler() {
        labelMap = new HashMap<>();
    }

    private String readUntil(Scanner scan, char delim) {
        return readUntil(scan, delim, true);
    }

    /**
     * Read a symbol until delimiter.
     */
    private String readUntil(Scanner scan, char delim, boolean autostrip) {
        String r = "";
        while(scan.hasNext()) {
            char c = scan.next().charAt(0);
            if(c == delim) {
                if(autostrip) {
                    skipWhitespace(scan);
                }
                return rstrip(r);
            }
            r += c;
        }
        if(autostrip) {
            r = rstrip(r);
        }
        return r;
    }

    /**
     * Skip all the whitespaces
     */
    private void skipWhitespace(Scanner scan) {
        while(scan.hasNext("\\s")) {
            scan.next();
        }
        return;
    }

    /**
     * Assemble a program.
     * Handles pseudo-instructions as well.
     */
    public String[] assemble(String source) throws AssemblerException {
        Scanner scan = new Scanner(source);
        List<String> result = new ArrayList<>();
        int lineno = 0;

        /* Label first */
        this.preprocessLabels(source);

        while(scan.hasNextLine()) {
            String line = lstrip(rstrip(scan.nextLine()));
            lineno++;
            if(line.endsWith(":")) {  // a label
                continue;
            } else if(line.startsWith("!")) {
                handlePseudoInstruction(0, lineno, line.substring(1), result, false);
            } else if(line.equals("") || line.startsWith("#")) {
                continue;
            } else {
                result.add(assembleOne(lineno, line));
            }
        }

        scan.close();
        return result.toArray(new String[0]);
    }

    /**
     * Preprocess labels
     */
    private void preprocessLabels(String source) {
        Scanner scan = new Scanner(source);
        int pc = 0, lineno = 0;

        while(scan.hasNextLine()) {
            String line = lstrip(rstrip(scan.nextLine()));
            lineno++;
            if(line.endsWith(":")) {
                String labelName = line.substring(0, line.length() - 1);
                labelMap.put(labelName, pc);
            } else if(line.startsWith("!")) {
                pc = this.handlePseudoInstruction(pc, lineno, line.substring(1), null, true);
            } else if(line.equals("") || line.startsWith("#")) {
                continue;
            } else {
                pc++;
            }
        }
        scan.close();
        return;
    }

    /**
     * Load value to a general register
     */
    private void load(List<String> codes, int lineno, String reg, int value) {
        /*
           load 5 bits every time and shift
           */
        //codes.add(assembleOne(lineno, String.format("LDR %s, X0, 8", reg)));
        codes.add(assembleOne(lineno, String.format("SRC %s, 15, L, L", reg)));
        codes.add(assembleOne(lineno, String.format("SRC %s, 1, L, L", reg)));   // clear reg using shifting
        codes.add(assembleOne(lineno, String.format("AIR %s, %d", reg, (value >> 11) & 0x1f)));
        codes.add(assembleOne(lineno, String.format("SRC %s, 5, L, L", reg)));
        codes.add(assembleOne(lineno, String.format("AIR %s, %d", reg, (value >> 6) & 0x1f)));
        codes.add(assembleOne(lineno, String.format("SRC %s, 5, L, L", reg)));
        codes.add(assembleOne(lineno, String.format("AIR %s, %d", reg, (value >> 1) & 0x1f)));
        codes.add(assembleOne(lineno, String.format("SRC %s, 1, L, L", reg)));
        codes.add(assembleOne(lineno, String.format("AIR %s, %d", reg, value & 1)));
        return;
    }

    /**
     * Push a general register
     */
    private void push(List<String> codes, int lineno, String gr) {
        codes.add(assembleOne(lineno, String.format("STR %s, X0, 9, I", gr)));
        codes.add(assembleOne(lineno, String.format("LDR R0, X0, 9")));
        codes.add(assembleOne(lineno, String.format("SIR R0, 1")));
        codes.add(assembleOne(lineno, String.format("STR R0, X0, 9")));
        return;
    }

    /**
     * Pop a general register
     */
    private void pop(List<String> codes, int lineno, String gr) {
        codes.add(assembleOne(lineno, String.format("LDR R0, X0, 9")));
        codes.add(assembleOne(lineno, String.format("AIR R0, 1")));
        codes.add(assembleOne(lineno, String.format("STR R0, X0, 9")));
        codes.add(assembleOne(lineno, String.format("LDR %s, X0, 9, I", gr)));
    }

    /**
     * Move between registers
     */
    private void move(List<String> codes, int lineno, String gr0, String gr1) {
        codes.add(assembleOne(lineno, String.format("AND %s, %s", gr0, gr1)));
        codes.add(assembleOne(lineno, String.format("ORR %s, %s", gr0, gr1)));
    }

    /**
     * Jump to address 
     */
    private void jmp(List<String> codes, int lineno, int address) {
        this.load(codes, lineno, "R0", address);
        codes.add(assembleOne(lineno, "STR R0, X0, 31"));
        codes.add(assembleOne(lineno, "JMA X0, 31, I"));
    }

    /**
     * Negation
     */
    private void neg(List<String> codes, int lineno, String gr) {
        codes.add(assembleOne(lineno, String.format("NOT %s", gr)));
        codes.add(assembleOne(lineno, String.format("AIR %s, 1", gr)));
    }

    /**
     * Load index register
     */
    private void loadx(List<String> codes, int lineno, String xr, int value) {
        this.load(codes, lineno, "R0", value);
        codes.add(assembleOne(lineno, "STR R0, X0, 31"));
        codes.add(assembleOne(lineno, String.format("LDX %s, 31", xr)));
    }

    /**
     * Handle pseudo-instruction.
     */
    public int handlePseudoInstruction(int pc, int lineno, String line, List<String> codes, boolean dryrun) throws AssemblerException {
        Scanner scan = new Scanner(line);
        scan.useDelimiter("");

        String command = readUntil(scan, ' ').toUpperCase();

        String gr, label, gr0, gr1;
        int number, address;
        switch(command) {
            case "ORG":   // set base address
                int newpc = Integer.decode(readUntil(scan, ' '));
                pc = newpc;
                break;
            case "DEFINE":  // define a label
                label = readUntil(scan, ',');
                number = Integer.decode(readUntil(scan, ' '));
                labelMap.put(label, number);
                break;
            case "WORD":    // define a word at current address
                int word = Integer.decode(readUntil(scan, ' '));
                if(!dryrun) {
                    codes.add(Element.fromInt(word).toString());
                }
                pc = pc + 1;
                break;
            case "ASCII":   // define string
            case "ASCIZ":
                readUntil(scan, '"', false);
                String content = readUntil(scan, '"', false);
                if(!dryrun) {
                    for(int i = 0; i < content.length(); i++) {
                        codes.add(Element.fromInt((int)content.charAt(i)).toString());
                    }

                    if(command.equals("ASCIZ")) {
                        codes.add(Element.fromInt(0).toString());
                    }
                }
                pc = pc + content.length();
                if(command.equals("ASCIZ")) {
                    pc++;
                }
                break;
            case "LDR":     // load a general register, since we normally can't load 16-bit numbers
                if(!dryrun) {
                    String reg = readUntil(scan, ',');
                    int value = translateLabel(lineno, readUntil(scan, ' '));
                    this.load(codes, lineno, reg, value);
                }
                pc = pc + 9;
                break;
            case "LDX":
                if(!dryrun) {
                    String xr = readUntil(scan, ',');
                    int value = translateLabel(lineno, readUntil(scan, ' '));
                    this.loadx(codes, lineno, xr, value);
                }
                pc = pc + 11;
                break;
            case "PUSH":    // push a general register
                if(!dryrun) {
                    gr = readUntil(scan, ' ');
                    this.push(codes, lineno, gr);
                }
                pc = pc + 4;
                break;
            case "POP":     // pop a general register
                if(!dryrun) {
                    gr = readUntil(scan, ' ');
                    this.pop(codes, lineno, gr);
                }
                pc = pc + 4;
                break;
            case "CALL":    // call a subroutine
                if(!dryrun) {
                    label = readUntil(scan, ' ');
                    address = translateLabel(lineno, label);
                    this.load(codes, lineno, "R0", address);
                    codes.add(assembleOne(lineno, "STR R0, X0, 10"));
                    codes.add(assembleOne(lineno, "JSR X0, 10, I"));
                }
                pc = pc + 11;
                break;
            case "JMP":     // jmp to a label
                if(!dryrun) {
                    label = readUntil(scan, ' ');
                    address = translateLabel(lineno, label);
                    this.jmp(codes, lineno, address);
                }
                pc = pc + 11;
                break;
            case "PROLOG":  // subroutine prolog
                if(!dryrun) {
                    this.push(codes, lineno, "R3");
                }
                pc = pc + 4;
                break;
            case "MOV":
                if(!dryrun) {
                    gr0 = readUntil(scan, ',');
                    gr1 = readUntil(scan, ' ');
                    this.move(codes, lineno, gr0, gr1);
                }
                pc = pc + 2;
                break;
            case "RET":     // Return from subroutine
                if(!dryrun) {
                    this.pop(codes, lineno, "R3");
                    codes.add(assembleOne(lineno, "RFS 0"));
                }
                pc = pc + 5;
                break;
            case "NEG":
                if(!dryrun) {
                    gr = readUntil(scan, ' ');
                    this.neg(codes, lineno, gr);
                }
                pc = pc + 2;
                break;
            case "PRINT":
                if(!dryrun) {
                    label = readUntil(scan, ' ');
                    LOG.info("Assembler label '{}' = {}", label, this.translateLabel(lineno, label));
                }
                break;
            default:
                throw new AssemblerException(lineno, "Unrecognised pseudo-instruction");
        }
        return pc;
    }

    /**
     * Translate a label to integer.
     */
    private int translateLabel(int lineno, String ident) throws AssemblerException {
        try {
            return Integer.decode(ident);
        } catch(NumberFormatException ex) {
            // Pass over
        }

        Integer address = labelMap.get(ident);
        if(address == null) {
            throw new AssemblerException(lineno, "Undefined identifier");
        }
        return address;
    }

    /**
     * Register name to index
     */
    private int regNameToIndex(int lineno, String name) throws AssemblerException {
        switch(name) {
            case "FR0":
            case "R0":
            case "X0":
            case "0":
                return 0;
            case "FR1":
            case "R1":
            case "X1":
            case "1":
                return 1;
            case "R2":
            case "X2":
            case "2":
                return 2;
            case "R3":
            case "X3":
            case "3":
                return 3;
            default:
                throw new AssemblerException(lineno, "Unknown register");
        }
    }

    /**
     * Pack binary string format 1. (opcode, r, ix, i, address)
     */
    private String packFormat1(int gr, int xr, Boolean indirect, int address) {
        return intToString(gr, 2) + intToString(xr, 2) + (indirect ? "1" : "0") + intToString(address, 5);
    }

    /**
     * Pack binary string format 2. (opcode, rx, ry, 0)
     */
    private String packFormat2(int rx, int ry) {
        return intToString(rx, 2) + intToString(ry, 2) + "000000";
    }

    /**
     * Pack binary string format 3. (Shift instructions)
     */
    private String packFormat3(int r, boolean arith, boolean lr, int count) {
        return intToString(r, 2) + (arith ? "0" : "1") + (lr ? "1" : "0") + "00" + intToString(count, 4);
    }

    /**
     * Pack binary string format 4. (I/O instruction)
     */
    private String packFormat4(int r, int devID) {
        return intToString(r, 2) + "000" + intToString(devID, 5);
    }

    /**
     * Assemble one line
     */
    public String assembleOne(int lineno, String line) throws AssemblerException {
        String result = "";
        Scanner scan = new Scanner(line);
        scan.useDelimiter("");  // read one by one

        // find & append opcode
        String mnenomic = readUntil(scan, ' ').toUpperCase();
        Integer opcode = OPCODE_LIST.get(mnenomic);
        if(opcode == null) {
            throw new AssemblerException(lineno, "Invalid instruction");
        }
        result += intToString(opcode, 6);

        /* Format 1 */
        int gr, xr, address;
        String indirect;

        /* Format 2 */
        int rx, ry;

        /* Format 3 */
        int r, count;
        String arith, lr;

        /* Format 4 */
        int devID;

        /* TRAP */
        int trapco;

        // append operands according to instruction format
        switch(mnenomic) {
            case "HLT":
                result += "0000000000";
                break;
            case "TRAP":
                trapco = translateLabel(lineno, readUntil(scan, ','));
                result += "000000" + intToString(trapco, 4);
                break;
            case "LDR":
            case "STR":
            case "LDA":
            case "JZ":
            case "JNE":
            case "JCC":
            case "SOB":
            case "JGE":
            case "AMR":
            case "SMR":
            case "FADD":
            case "FSUB":
            case "VADD":
            case "VSUB":
            case "CNVRT":
            case "LDFR":
            case "STFR":
                gr = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                xr = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                address = translateLabel(lineno, readUntil(scan, ','));
                indirect = readUntil(scan, ',').toUpperCase();
                result += packFormat1(gr, xr, indirect.equals("I"), address);
                break;
            case "LDX":
            case "STX":
            case "JMA":
            case "JSR":
                xr = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                address = translateLabel(lineno, readUntil(scan, ','));
                indirect = readUntil(scan, ',').toUpperCase();
                result += packFormat1(0, xr, indirect.equals("I"), address);
                break;
            case "RFS":
                address = translateLabel(lineno, readUntil(scan, ','));
                result += packFormat1(0, 0, false, address);
                break;
            case "AIR":
            case "SIR":
                gr = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                address = translateLabel(lineno, readUntil(scan, ','));
                result += packFormat1(gr, 0, false, address);
                break;
            case "MLT":
            case "DVD":
            case "TRR":
            case "AND":
            case "ORR":
                rx = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                ry = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                result += packFormat2(rx, ry);
                break;
            case "NOT":
                rx = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                result += packFormat2(rx, 0);
                break;
            case "SRC":
            case "RRC":
                r = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                count = translateLabel(lineno, readUntil(scan, ','));
                lr = readUntil(scan, ',').toUpperCase();
                arith = readUntil(scan, ',').toUpperCase();
                result += packFormat3(r, arith.equals("A"), lr.equals("L"), count);
                break;
            case "IN":
            case "OUT":
            case "CHK":
                r = regNameToIndex(lineno, readUntil(scan, ',').toUpperCase());
                devID = translateLabel(lineno, readUntil(scan, ','));
                result += packFormat4(r, devID);
                break;
            default:
                throw new AssemblerException(lineno, "Unknown mnenomic");
        }

        if(result.length() != 16) {
            throw new AssemblerException(lineno, "Internal error");
        }
        scan.close();
        return result;
    }
}
