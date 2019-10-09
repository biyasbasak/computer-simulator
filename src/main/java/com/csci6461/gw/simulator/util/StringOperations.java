package com.csci6461.gw.simulator.util;

import java.io.InputStream;
import java.io.IOException;

// Util class for string operations
public class StringOperations {
    /**
     * Strip leading whitespaces.
     */
    public static String lstrip(String s) {
        String v = "";
        int idx = 0;
        while(idx < s.length()) {
            if(!Character.isWhitespace(s.charAt(idx))) {
                break;
            }
            idx += 1;
        }

        while(idx < s.length()) {
            v += s.charAt(idx);
            idx += 1;
        }
        return v;
    }

    /**
     * Strip trailing whitespaces.
     */
    public static String rstrip(String s) {
        String v = "";
        int idx = s.length() - 1;
        while(idx >= 0) {
            if(!Character.isWhitespace(s.charAt(idx))) {
                break;
            }
            idx -= 1;
        }
        for(int i = 0; i <= idx; i++) {
            v += s.charAt(i);
        }
        return v;
    }

    /**
     * Read all bytes from an input stream.
     */
    public static byte[] readAllBytes(InputStream is) throws IOException {
        int length = is.available();
        byte[] r = new byte[length];
        is.read(r, 0, length);
        return r;
    }
}
