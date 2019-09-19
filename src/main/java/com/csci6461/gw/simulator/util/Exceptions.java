package com.csci6461.gw.simulator.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.StringWriter;
import java.io.PrintWriter;

public class Exceptions {
    private static Logger LOG = LogManager.getLogger("Util.Exc");

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
    }

    public static class AssemblerException extends SimulatorException {
        private int _lineno;

        public AssemblerException(int lineno, String message) {
            super(message);
            _lineno = lineno;
        }
    }
}
