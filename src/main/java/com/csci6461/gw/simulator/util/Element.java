package com.csci6461.gw.simulator.util;

import java.util.BitSet;
import com.csci6461.gw.simulator.util.BitOperations;

/**
 * An element describes a number used to do arithmetics (in a integer modulo ring).
 */
public class Element {
    /**
     * The actually value for the element.
     */
    private BitSet _value;

    /**
     * The number of bits to do arithmetic.
     */
    private int _nbits;

    /**
     * Class constructor
     * @param   nbits the number of bits inside the register
     */
    public Element(int nbits) {
        _nbits = nbits;
        _value = new BitSet(nbits);
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
        long value = wraparound(BitOperations.bitsToLong(_value) + addend);
        _value = BitOperations.longToBits(value);
    }

    /**
     * Element subtraction: self = self - subtraction.
     */
    public void sub(long subtraction) {
        long value = wraparound(BitOperations.bitsToLong(_value) - subtraction);
        _value = BitOperations.longToBits(value);
    }

    /**
     * Element multiplication: self = self * multiplier.
     */
    public void mult(long multiplier) {
        long value = wraparound(BitOperations.bitsToLong(_value) * multiplier);
        _value = BitOperations.longToBits(value);
    }

    /**
     * Element division: self = self / divisor.
     */
    public void div(long divisor) {
        long value = wraparound(BitOperations.bitsToLong(_value) / divisor);
        _value = BitOperations.longToBits(value);
    }

    /**
     * Get the integer representation of the element.
     */
    public long value() {
        return BitOperations.bitsToLong(_value);
    }

    public BitSet get() {
        return _value;
    }

    /**
     * Set the value by BitSet.
     */
    public void value(BitSet value) {
        _value = value;
    }

    /**
     * Set the value by integer representation.
     */
    public void value(long value) {
        _value = BitOperations.longToBits(wraparound(value));
    }
}
