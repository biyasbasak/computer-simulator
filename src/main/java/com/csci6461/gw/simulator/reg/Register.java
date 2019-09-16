package com.csci6461.gw.simulator.reg;

import com.csci6461.gw.simulator.util.*;

/**
 * A class describing the register
 */
public class Register extends Element {
    /**
     * The name of the register
     */
    private String _name;

    /**
     * Class constructor
     * @param   nbits the number of bits inside the register
     */
    public Register(String name, int nbits) {
        super(nbits);
        _name = name;
    }

    /**
     * Get the name of the register 
     */
    public String getName() {
        return _name;
    }
}
