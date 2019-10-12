package com.csci6461.gw.simulator.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.StringWriter;
import java.io.PrintWriter;

public class Exceptions {
    private static Logger LOG = LogManager.getLogger("Util.Exc");

    /**
     * General simulator exception.
     */
    public static abstract class SimulatorException extends RuntimeException {
        private String _message;

        SimulatorException(String message) {
            _message = message;
        }

        /**
         * Print stack trace to log frontend.
         */
        public void logStackTrace() {
            StringWriter sw = new StringWriter();
            this.printStackTrace(new PrintWriter(sw));
            LOG.warn(sw.toString());
        }

        @Override
        public String getMessage() {
            return _message;
        }
    }

    /**
     * Assembler-specific exception.
     */
    public static class AssemblerException extends SimulatorException {
        private int _lineno;

        public AssemblerException(int lineno, String message) {
            super(message);
            _lineno = lineno;
        }

        @Override
        public String getMessage() {
            return String.format("[Assembler] %d: %s", _lineno, super.getMessage());
        }
    }

    /**
     * CPU-specific exception.
     */
    public static class CPUException extends SimulatorException {
        private int _pc;

        private boolean _halted;

        public CPUException(int pc, String message) {
            this(pc, message, false);
        }

        public CPUException(int pc, String message, boolean halted) {
            super(message);
            _pc = pc;
            _halted = halted;
        }

        @Override
        public String getMessage() {
            return String.format("[CPU] %s at PC %d", super.getMessage(), _pc);
        }

        public boolean isHalted() {
            return _halted;
        }
    }

    /**
     * Memory-specific exception.
     */
    public static class MemoryException extends SimulatorException {
        private int _address;

        private boolean _roaccess;

        public MemoryException(int address, String message) {
            this(address, message, false);
        }

        public MemoryException(int address, String message, boolean ro) {
            super(message);
            _address = address;
            _roaccess = ro;
        }

        @Override
        public String getMessage() {
            return String.format("[Memory] %s at address %x", super.getMessage(), _address);
        }

        public boolean isReservedAccess() {
            return _roaccess;
        }
    }
}
