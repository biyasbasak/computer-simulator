package com.csci6461.gw.simulator.reg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public static final String IX_NAMES[] = { "X1", "X2", "X3", "X4" };

    /**
     * General registers
     */
    private Register _GR[];

    /**
     * Index registers
     */
    private Register _IX[];

    /**
     * Special registers 
     */
    private Register _MAR, _MBR, _MFR, _PC, _CC, _IR;

    /**
     * Class constructor
     */
    public MachineRegisters() {
        _GR = new Register[GR_COUNT];
        for(int i = 0; i < GR_COUNT; i++) {
            _GR[i] = new Register(GR_NAMES[i], GR_BITS);
        }
        _IX = new Register[IX_COUNT];
        for(int i = 0; i < IX_COUNT; i++) {
            _IX[i] = new Register(IX_NAMES[i], IX_BITS);
        }
        _MAR = new Register("MAR", 16);
        _MBR = new Register("MBR", 16);
        _MFR = new Register("MFR", 4);
        _PC = new Register("PC", 12);
        _CC = new Register("CC", 4);
        _IR = new Register("IR", 16);
    }

    Register getGeneralRegister(int index) {
        return _GR[index];
    }

    Register getIndexRegister(int index) {
        return _IX[index];
    }

    void advance() {
        _PC.add(1);
    }

    /**
     * Dump the entire state of the registers.
     */
    void dumpState() {
        LOG.debug("================= Machine registers dump ===================");
        LOG.debug("============================================================");
        LOG.debug("PC: %04X, CC: %01X, IR: %04X, MAR: %04X, MBR: %04X, MFR: %04X", _PC.value(), _CC.value(), _IR.value(), _MAR.value(), _MBR.value(), _MFR.value());
        for(int i = 0; i < GR_COUNT; i++) {
            LOG.debug("%s: %04X", GR_NAMES[i], _GR[i].value());
        }
        for(int i = 0; i < IX_COUNT; i++) {
            LOG.debug("%s: %04X", IX_NAMES[i], _IX[i].value());
        }
        LOG.debug("============================================================");
        LOG.debug("============================================================");
    }
}
