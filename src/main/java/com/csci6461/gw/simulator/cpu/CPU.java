package com.csci6461.gw.simulator.cpu;

import com.csci6461.gw.simulator.instr.Decoder;
import com.csci6461.gw.simulator.instr.instructions.Transfer.*;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.util.Element;
import com.csci6461.gw.simulator.instr.Instruction;
import static com.csci6461.gw.simulator.instr.instructions.LoadStore.*;
import static com.csci6461.gw.simulator.util.Exceptions.*;
import com.csci6461.gw.simulator.cpu.devices.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;

public class CPU {
    private static Logger LOG = LogManager.getLogger("CPU.CPU");

    private MachineRegisters registers;
    private Memory memory;

    public static final int PROGRAM_BASE = 0x100;

    private boolean halted;

    private ALU alu;

    private HashMap<Integer, Device> devices;

    private Keyboard kbd;

    public CPU() {
        this.registers = new MachineRegisters();
        this.memory = new Memory();
        this.halted = false;
        this.alu = new ALU(this);
        this.devices = new HashMap<>();
        this.initializeDevices();
    }

    private void initializeDevices() {

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
     * Execute the given instruction.
     */
    public void execute(HashMap<String, String> instructionData) throws CPUException {
        int opCode = Integer.parseInt(instructionData.get("opCode"), 2);
        Instruction ins;
        switch (opCode) {
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
            throw new CPUException(registers.pc(), "Machine halted");
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
     * 
     */
    public void step() {
        try {
            this.cycle();
        } catch(MemoryException ex) {   // 3
            // TODO
        } catch(CPUException ex) {  // 0, 1, 2
            // TODO
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
        }
        registers.setFault(fault_code);
        registers.setPC(handler.uvalue());
    }
}
