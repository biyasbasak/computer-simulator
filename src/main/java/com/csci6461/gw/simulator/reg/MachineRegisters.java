package com.csci6461.gw.simulator.reg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * Class representing all the registers the CPU need.
 */
public class MachineRegisters {

    private static final Logger LOG = LogManager.getLogger("sim.register");

    public static final int GR_COUNT = 4;
    public static final int GR_BITS = 16;
    public static final int IX_COUNT = 4;
    public static final int IX_BITS = 16;

    public static final String GR_NAMES[] = { "R0", "R1", "R2", "R3" };
    public static final String IX_NAMES[] = { "X0", "X1", "X2", "X3" };

    public static final String REG_NAMES[] = { "R0", "R1", "R2", "R3", "PC", "CC", "IR", "MAR", "MBR", "MFR", "X1", "X2", "X3" };

    /**
     * General registers
     */
    private Register _GR[];

    /**
     * Index registers
     */
    private Register _IX[];

    /**
     * Hashmap for simplicity
     */
    private HashMap<String, Register> _Regs;

    /**
     * Class constructor
     */
    public MachineRegisters() {
        _Regs = new HashMap<>();
        
        // general registers
        _GR = new Register[GR_COUNT];
        for(int i = 0; i < GR_COUNT; i++) {
            _GR[i] = new Register(GR_NAMES[i], GR_BITS);
            _Regs.put(GR_NAMES[i], _GR[i]);
        }
        
        // index registers
        _IX = new Register[IX_COUNT];
        for(int i = 0; i < IX_COUNT; i++) {
            _IX[i] = new Register(IX_NAMES[i], IX_BITS);
            _Regs.put(IX_NAMES[i], _IX[i]);
        }
     
        // special registers
        _Regs.put("PC", new Register("PC", 12));
        _Regs.put("CC", new Register("CC", 4));
        _Regs.put("IR", new Register("IR", 16));
        _Regs.put("MAR", new Register("MAR", 16));
        _Regs.put("MBR", new Register("MBR", 16));
        _Regs.put("MFR", new Register("MFR", 4));
    }

    /**
     * Get GR.
     */
    public Register getGeneralRegister(int index) {
        return _GR[index];
    }

    /**
     * Get IX.
     */
    public Register getIndexRegister(int index) {
        return _IX[index];
    }

    /**
     * Get all registers.
     */
    public HashMap<String, Register> getAllRegisters() {
        return _Regs;
    }

    /**
     * Get register by name.
     */
    public Register getRegister(String name) {
        return _Regs.get(name);
    }

    /**
     * PC = PC + 1
     */
    public void advance() {
        _Regs.get("PC").add(1);
    }

    /**
     * Get the value of PC 
     */
    public int pc() {
        return _Regs.get("PC").value();
    }

    /**
     * Set the value of PC
     */
    public void setPC(int value) {
        _Regs.get("PC").setByValue(value);
    }

    /**
     * Set overflow condition
     */
    public void setOverflow(Boolean overflow) {
        _Regs.get("CC").set(0, overflow);
    }

    /**
     * Set underflow condition
     */
    public void setUnderflow(Boolean underflow) {
        _Regs.get("CC").set(1, underflow);
    }

    /**
     * Set division by zero condition
     */
    public void setDivZero(Boolean divByZero) {
        _Regs.get("CC").set(2, divByZero);
    }

    /**
     * Set eq condition
     */
    public void setEqual(Boolean equal) {
        _Regs.get("CC").set(3, equal);
    }

    /**
     * Set machine fault type
     */
    public void setFault(int fault) {
        assert fault >= 1 && fault <= 4;
        Register MFR = _Regs.get("MFR");
        MFR.clear();
        MFR.set(fault - 1, true);
    }

    /**
     * Dump the entire state of the registers.
     */
    public void dumpState() {
        LOG.debug("================= Machine registers dump ===================");
        LOG.debug("============================================================");
        _Regs.forEach((k, v) -> {
            LOG.debug("%s: %016X", k, v.value());
        });
        LOG.debug("============================================================");
        LOG.debug("============================================================");
    }
}
