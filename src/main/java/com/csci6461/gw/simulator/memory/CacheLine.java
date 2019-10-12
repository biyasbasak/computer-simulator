package com.csci6461.gw.simulator.memory;

import org.apache.logging.log4j.Logger; 
import org.apache.logging.log4j.LogManager; 

import com.csci6461.gw.simulator.util.Element; 

/**
 * Implementation of a simple direct-mapped cache line
 */
public class CacheLine {
    private static Logger LOG = LogManager.getLogger("Memory.Cacheline");

    private int size;

    private Element[] caches;

    private Integer tag;

    private int line_number;

    private boolean[] dirty, cached;

    private Cache ch;

    public CacheLine(int line_size, int line_no, Cache ch) {
        size = line_size;
        this.ch = ch;

        caches = new Element[size];
        dirty = new boolean[size];
        cached = new boolean[size];
        
        line_number = line_no;
        tag = null;
    }

    /**
     * Fetch from current cache line
     */
    public Element fetch(int tag, int offset) {
        if(this.tag == null || tag != this.tag) {
            LOG.info("Cache miss on line {}: Tag mismatch", line_number);
            return null;
        }

        if(!cached[offset]) {
            LOG.info("Cache miss on line {}: Address not cached", line_number);
            return null;
        }
        
        LOG.info("Cache hit on line {}", line_number);
        return caches[offset];
    }

    /**
     * For UI purpose
     */
    public Element get(int offset) {
        return caches[offset];
    }

    /**
     * Flush current cache line 
     */
    public void flush() {
        for(int i = 0; i < size; i++) {
            if(cached[i]) {
                if(dirty[i]) {  // dirty data, write back to memory
                    ch.writeback(tag, line_number, i, caches[i]);
                    dirty[i] = false;
                }
                caches[i] = null;
                cached[i] = false;
            }
        }
        return;
    }

    /**
     * Check if cached
     */
    public boolean cached(int tag, int offset) {
        if(this.tag == null || this.tag != tag) {
            return false;
        }
        return cached[offset];
    }

    /**
     * Store into current cache line
     */
    public void store(int tag, int offset, Element elem) {
        if(this.tag == null || this.tag != tag) {
            LOG.info("Flushing cache line {}: Tag mismatch", line_number);
            flush();
        }

        if(cached[offset]) {
            dirty[offset] = true;   // set dirty bit, write back on flush
        }
        this.tag = tag;
        cached[offset] = true;
        caches[offset] = elem;
        return;
    }

    /**
     * Get the size of the cache line
     */
    public int size() {
        return size;
    }

    /**
     * Get tag 
     */
    public Integer tag() {
        return this.tag;
    }
}
