package com.csci6461.gw.simulator.cpu;

import com.csci6461.gw.simulator.instr.Decoder;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.util.Element;
import com.csci6461.gw.simulator.instr.Instruction;
import static com.csci6461.gw.simulator.util.Exceptions.*;
import com.csci6461.gw.simulator.cpu.devices.*;
import com.csci6461.gw.simulator.ui.Controller;
import static com.csci6461.gw.simulator.instr.instructions.Transfer.*;
import static com.csci6461.gw.simulator.instr.instructions.LoadStore.*;
import static com.csci6461.gw.simulator.instr.instructions.Arithmetic.*;
import static com.csci6461.gw.simulator.instr.instructions.IO.*;
import static com.csci6461.gw.simulator.instr.instructions.Miscellaneous.*;
import static com.csci6461.gw.simulator.instr.instructions.FloatingPoint.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Deque;


public class CPU {
    private static Logger LOG = LogManager.getLogger("CPU.CPU");

    private MachineRegisters registers;
    private Memory memory;

    public static final int PROGRAM_BASE = 0x100;

    private boolean halted;

    private ALU alu;

    private HashMap<Integer, Device> devices;

    private Controller ctrler;

    public CPU(Controller ctrl) {
        this.registers = new MachineRegisters();
        this.memory = new Memory();
        this.halted = false;
        this.alu = new ALU(this);
        this.ctrler = ctrl;
        this.devices = new HashMap<>();
        this.initializeDevices();
    }

    private void initializeDevices() {
        devices.put(0, new Keyboard(0, "keyboard", this.ctrler));
        devices.put(1, new Printer(1, "printer", this.ctrler));
    }

    /**
     * Get machine registers.
     */
    public MachineRegisters getRegisters() {
        return registers;
    }

    /**
     * Get memory object.
     */
    public Memory getMemory() {
        return memory;
    }

    /**
     * Get ALU 
     */
    public ALU getALU() {
        return this.alu;
    }

    /**
     * Initialize
     */
    public void initialize() {
        this.halted = false;
    }
    
    /**
     * Execute the given instruction.
     */
    public void execute(HashMap<String, String> instructionData) throws CPUException {
        int opCode = Integer.parseInt(instructionData.get("opCode"), 2);
        Instruction ins;
        switch (opCode) {
            case 0:
                ins = new HLT();
                break;
            case 036:
                ins = new TRAP();
                break;
            case 1:
                ins = new LDR();
                break;
            case 2:
                ins = new STR();
                break;
            case 3:
                ins = new LDA();
                break;
            case 041:
                ins = new LDX();
                break;
            case 042:
                ins = new STX();
                break;
            case 010:
                ins = new JZ();
                break;
            case 011:
                ins = new JNE();
                break;
            case 012:
                ins = new JCC();
                break;
            case 013:
                ins = new JMA();
                break;
            case 014:
                ins = new JSR();
                break;
            case 015:
                ins = new RFS();
                break;
            case 016:
                ins = new SOB();
                break;
            case 017:
                ins = new JGE();
                break;
            case 4:
                ins = new AMR();
                break;
            case 5:
                ins = new SMR();
                break;
            case 6:
                ins = new AIR();
                break;
            case 7:
                ins = new SIR();
                break;
            case 020:
                ins = new MLT();
                break;
            case 021:
                ins = new DVD();
                break;
            case 022:
                ins = new TRR();
                break;
            case 023:
                ins = new AND();
                break;
            case 024:
                ins = new ORR();
                break;
            case 025:
                ins = new NOT();
                break;
            case 031:
                ins = new SRC();
                break;
            case 032:
                ins = new RRC();
                break;
            case 061:
                ins = new IN();
                break;
            case 062:
                ins = new OUT();
                break;
            case 063:
                ins = new CHK();
                break;
            case 033:
                ins = new FADD();
                break;
            case 034:
                ins = new FSUB();
                break;
            case 043:
                ins = new VADD();
                break;
            case 044:
                ins = new VSUB();
                break;
            case 037:
                ins = new CNVRT();
                break;
            case 050:
                ins = new LDFR();
                break;
            case 051:
                ins = new STFR();
                break;
            default:
                throw new CPUException(registers.pc(), "Unknown opcode");
        }
        ins.setInstruction(instructionData);
        ins.execute(this, this.memory, this.registers);
    }

    /**
     * Fetch a program to IR register.
     */
    private void fetchInstruction() {
        Element ins = memory.fetch(registers.pc());
        registers.getRegister("IR").set(ins);
    }

    /**
     * Load a program to PROGRAM_BASE.
     */
    public void loadProgram(String[] program) {
        for(int i = 0; i < program.length; i++) {
            memory.set_direct(PROGRAM_BASE + i, program[i]);
        }
        registers.setPC(PROGRAM_BASE);
    }

    /**
     * One instruction cycle,
     * assuming single-cycle machine...
     */
    public void cycle() {
        if(halted) {
            throw new CPUException(registers.pc(), "Machine halted", true);
        }

        // 1. Fetch instruction
        fetchInstruction();
        
        // 2. Decode instruction
        HashMap<String, String> instruction = Decoder.decode(registers.getRegister("IR").toString());

        // 3. Execute instruction
        execute(instruction);
    }

    /**
     * Halt the machine
     */
    public void halt() {
        this.halted = true;
    }

    /**
     * Return the status of the machine.
     */
    public boolean halted() {
        return this.halted;
    }

    /**
     * Run until halted.
     */
    public void run() {
        while(!this.halted()) {
            this.step();
        }
    }

    /**
     * Single step on instruction cycle level.
     */
    public void step() {
        try {
            this.cycle();
        } catch(MemoryException ex) {
            LOG.info("Memory fault: {}", ex.getMessage());
            this.fault_handler(ex.isReservedAccess() ? 1 : 4);
        } catch(CPUException ex) {
            LOG.info("CPU fault: {}", ex.getMessage());
            if(!ex.isHalted()) {
                this.fault_handler(3);
            }
        }
    }

    /**
     * Trigger trap
     */
    public void trap(int trap_code) {
        int npc = registers.pc() + 1;

        LOG.info("Trap captured, trap_code: {}", trap_code);

        Element table = memory.fetch(0);
        Element handler = memory.fetch(table.uvalue() + trap_code);

        registers.setPC(handler.uvalue());
        memory.store(2, Element.fromInt(npc));
        return;
    }

    /**
     * Handles a machine fault
     */
    private void fault_handler(int fault_code) {
        int npc = registers.pc() + 1;

        LOG.warn("Machine fault captured, fault code: {}", fault_code);

        memory.store(4, Element.fromInt(npc));
        Element handler = memory.fetch(1);
        if(handler.uvalue() >= memory.size()) {
            LOG.error("Double fault occured in fault handler, halting");
            this.halt();
            return;
        }
        registers.setFault(fault_code);
        registers.setPC(handler.uvalue());
    }

    /**
     * Commit an input buffer
     */
    public void commit(Deque<Integer> bufQ) {
        Keyboard kbd = (Keyboard)devices.get(0);
        kbd.commit(bufQ);
    }

    /**
     * Input from device 
     */
    public Element input(int devid) {
        Device dev = devices.get(devid);
        if(dev == null) {
            return Element.fromInt(0);
        }
        return dev.input();
    }
    
    /**
     * Output to device 
     */
    public void output(int devid, Element elem) {
        Device dev = devices.get(devid);
        if(dev == null) {
            return;
        }
        dev.output(elem);
    }

    /**
     * Check device status
     */
    public boolean check(int devid) {
        Device dev = devices.get(devid);
        if(dev == null) {
            return false;
        }
        return dev.check();
    }
}
