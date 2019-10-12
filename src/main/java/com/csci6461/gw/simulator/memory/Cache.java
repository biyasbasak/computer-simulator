package com.csci6461.gw.simulator.memory;

import org.apache.logging.log4j.Logger; 
import org.apache.logging.log4j.LogManager; 

import com.csci6461.gw.simulator.util.Element; 
import static com.csci6461.gw.simulator.util.Exceptions.*;

/**
 * Implementation of a simple direct-mapped cache
 * Policy: write-back, no write-allocate
 */
public class Cache {
    private static Logger LOG = LogManager.getLogger("Memory.Cache");

    public final int CACHE_LINES = 16;  // 4 bit index

    public final int CACHE_SIZE = 16;  // in words, offset bits = 4

    public final int TAG_BITS = 8;    // 8 tag bits

    private int hit, miss;  // for debug purpose

    private CacheLine[] caches;

    private Memory mem;

    public Cache(Memory memory) {
        hit = 0;
        miss = 0;

        mem = memory;
        caches = new CacheLine[CACHE_LINES];
        for(int i = 0; i < CACHE_LINES; i++) {
            caches[i] = new CacheLine(CACHE_SIZE, i, this);
        }
    }

    /**
     * Parse an address, a[0] = tag, a[1] = index, a[2] = offset
     */
    public int[] parseAddress(int address) {
        int[] result = new int[3];
        result[2] = address & 0xf;
        result[1] = (address >> 4) & 0xf;
        result[0] = (address >> 8) & 0xff;
        return result;
    }

    /**
     * Combine an address from tag, index and offset
     */
    public int combineAddress(int tag, int index, int offset) {
        return (offset | (index << 4) | (tag << 8));
    }

    /**
     * Try fetch from cache, update cache if failed
     */
    public Element fetch(int address) {
        Element result = null;
        int[] addrs = this.parseAddress(address);

        result = caches[addrs[1]].fetch(addrs[0], addrs[2]);
        if(result != null) {  // read hit
            hit++;
            return result;
        }
        
        miss++;   // read miss
        result = this.mem.fetch_direct(address);
        caches[addrs[1]].store(addrs[0], addrs[2], result);   // update cache
        return result;
    }

    /**
     * Store to cache if the address is cached.
     */
    public void store(int address, Element element) {
        int[] addrs = this.parseAddress(address);
        if(!caches[addrs[1]].cached(addrs[0], addrs[2])) {    // write-miss
            miss++;
            this.mem.store_direct(address, element);
            return;
        }
        hit++;
        caches[addrs[1]].store(addrs[0], addrs[2], element);  // write-back policy, don't need to update memory now
        return;
    }

    /**
     * Writeback from cache to memory, should only be called by CacheLine
     */
    public void writeback(int tag, int index, int offset, Element element) {
        int address = combineAddress(tag, index, offset);
        LOG.info("Writeback @{:04x}: {:04x}", address, element.value());
        this.mem.store_direct(address, element);
    }

    /**
     * Get the size of the cache
     */
    public int size() {
        return CACHE_LINES * CACHE_SIZE;
    }
}
