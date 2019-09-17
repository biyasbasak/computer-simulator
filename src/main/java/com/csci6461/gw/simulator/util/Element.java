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
    private long wraparound(long value) {
        return value & ((1 << _nbits) - 1);
    }

    /**
     * Element addition: self = self + addend.
     */
    public void add(long addend) {
        long value = wraparound(BitOperations.bitsToLong(this) + addend);
        this.set(BitOperations.longToBits(value));
    }

    /**
     * Element subtraction: self = self - subtraction.
     */
    public void sub(long subtraction) {
        long value = wraparound(BitOperations.bitsToLong(this) - subtraction);
        this.set(BitOperations.longToBits(value));
    }

    /**
     * Element multiplication: self = self * multiplier.
     */
    public void mult(long multiplier) {
        long value = wraparound(BitOperations.bitsToLong(this) * multiplier);
        this.set(BitOperations.longToBits(value));
    }

    /**
     * Element division: self = self / divisor.
     */
    public void div(long divisor) {
        long value = wraparound(BitOperations.bitsToLong(this) / divisor);
        this.set(BitOperations.longToBits(value));
    }

    /**
     * Get the integer representation of the element.
     */
    public long value() {
        return BitOperations.bitsToLong(this);
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
    public void setByValue(long value) {
        this.set(BitOperations.longToBits(wraparound(value)));
    }
}
