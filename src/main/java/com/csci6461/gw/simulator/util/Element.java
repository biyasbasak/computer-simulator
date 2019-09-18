package com.csci6461.gw.simulator.util;

import java.util.BitSet;
import com.csci6461.gw.simulator.util.BitOperations;

/**
 * An element describes a number used to do arithmetics (in a integer modulo ring).
 */
public class Element extends BitSet {
    /**
     * The number of bits to do arithmetic.
     */
    private int _nbits;

    /**
     * Set to true if wrap around is triggered.
     */
    private Boolean _wrapped;

    /**
     * Class constructor
     * @param   nbits the number of bits inside the register
     */
    public Element(int nbits) {
        super(nbits);
        _nbits = nbits;
    }

    /**
     * Wrap the number around within the bound of {@link Element#_nbits}.
     */
    private int wraparound(int value) {
        return value & ((1 << _nbits) - 1);
    }

    /**
     * Element addition: self = self + addend.
     */
    public void add(int addend) {
        int value = wraparound(this.value() + addend);
        this.set(BitOperations.intToBits(value, _nbits));
    }

    /**
     * Element subtraction: self = self - subtraction.
     */
    public void sub(int subtraction) {
        int value = wraparound(this.value() - subtraction);
        this.set(BitOperations.intToBits(value, _nbits));
    }

    /**
     * Element multiplication: self = self * multiplier.
     */
    public void mult(int multiplier) {
        int value = wraparound(this.value() * multiplier);
        this.set(BitOperations.intToBits(value, _nbits));
    }

    /**
     * Element division: self = self / divisor.
     */
    public void div(int divisor) {
        int value = wraparound(this.value() / divisor);
        this.set(BitOperations.intToBits(value, _nbits));
    }

    /**
     * Element modulo: self = self % divisor.
     */
    public void mod(int divisor) {
        int value = wraparound(this.value() / divisor);
        this.set(BitOperations.intToBits(value, _nbits));
    }

    /**
     * Get the integer representation of the element.
     */
    public int value() {
        return BitOperations.bitsToInt(this, _nbits);
    }

    /**
     * Replace the element.
     */
    public void set(BitSet s) {
        this.clear();
        this.or(s);
    }

    /**
     * Set a element by its integer representation.
     */
    public void setByValue(int value) {
        this.set(BitOperations.intToBits(wraparound(value), _nbits));
    }

    /**
     * Convert the element to string.
     */
    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < _nbits; i++) {
            if(this.get(i)) {
                s += "1";
            } else {
                s += "0";
            }
        }
        return s;
    }
}
