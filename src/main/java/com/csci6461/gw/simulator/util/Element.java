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
        int value = wraparound(BitOperations.bitsToInt(this) + addend);
        this.set(BitOperations.intToBits(value));
    }

    /**
     * Element subtraction: self = self - subtraction.
     */
    public void sub(int subtraction) {
        int value = wraparound(BitOperations.bitsToInt(this) - subtraction);
        this.set(BitOperations.intToBits(value));
    }

    /**
     * Element multiplication: self = self * multiplier.
     */
    public void mult(int multiplier) {
        int value = wraparound(BitOperations.bitsToInt(this) * multiplier);
        this.set(BitOperations.intToBits(value));
    }

    /**
     * Element division: self = self / divisor.
     */
    public void div(int divisor) {
        int value = wraparound(BitOperations.bitsToInt(this) / divisor);
        this.set(BitOperations.intToBits(value));
    }

    /**
     * Get the integer representation of the element.
     */
    public int value() {
        return BitOperations.bitsToInt(this);
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
        this.set(BitOperations.intToBits(wraparound(value)));
    }
}
