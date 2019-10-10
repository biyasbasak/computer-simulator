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
        int value = wraparound(this.value() + addend);
        this.set(BitOperations.intToBits(value, _nbits, true));
    }

    /**
     * Element subtraction: self = self - subtraction.
     */
    public void sub(int subtraction) {
        int value = wraparound(this.value() - subtraction);
        this.set(BitOperations.intToBits(value, _nbits, true));
    }

    /**
     * Element multiplication: self = self * multiplier.
     */
    public void mult(int multiplier) {
        int value = wraparound(this.value() * multiplier);
        this.set(BitOperations.intToBits(value, _nbits, true));
    }

    /**
     * Element division: self = self / divisor.
     */
    public void div(int divisor) {
        int value = wraparound(this.value() / divisor);
        this.set(BitOperations.intToBits(value, _nbits, true));
    }

    /**
     * Element modulo: self = self % divisor.
     */
    public void mod(int divisor) {
        int value = wraparound(this.value() / divisor);
        this.set(BitOperations.intToBits(value, _nbits, true));
    }

    /**
     * Return the bitsize of this element
     */
    public int bits() {
        return this._nbits;
    }

    /**
     * Get the integer representation of the element.
     */
    public int value() {
        return BitOperations.bitsToInt(this, _nbits, true);
    }

    /**
     * Get the unsigned integer representation of the element.
     */
    public int uvalue() {
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
     * Replace the element by binary string.
     */
    public void set(String s) {
        int value = BitOperations.stringToInt(s);
        this.setByValue(value);
    }

    /**
     * Set a element by its integer representation.
     */
    public void setByValue(int value) {
        this.set(BitOperations.intToBits(wraparound(value), _nbits, true));
    }

    /**
     * Clone this object
     */
    @Override
    public Element clone() {
        Element result = new Element(this.bits());
        result.set(this);
        return result;
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

    /**
     * Create an element from a binary string with bit size.
     */
    public static Element fromString(String s, int nbits) {
        Element e = new Element(nbits);
        e.set(BitOperations.stringToBits(s, nbits));
        return e;
    }

    /**
     * Create an 16 bit element from a binary string.
     */
    public static Element fromString(String s) {
        return fromString(s, 16);
    }

    /**
     * Create an element from integer
     */
    public static Element fromInt(int value, int nbits) {
        Element e = new Element(nbits);
        e.setByValue(value);
        return e;
    }

    /**
     * Create an 16 bit element from integer
     */
    public static Element fromInt(int value) {
        return fromInt(value, 16);
    }
}
