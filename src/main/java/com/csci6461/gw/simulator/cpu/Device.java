package com.csci6461.gw.simulator.cpu;

import com.csci6461.gw.simulator.util.Element;

public abstract class Device {
    private int device_id;
    private String device_name;

    public Device(int device_id, String device_name) {
        this.device_id = device_id;
        this.device_name = device_name;
    }

    /**
     * Device input
     */
    public abstract Element input();

    /**
     * Device output 
     */
    public abstract void output(Element elem);

    /**
     * Device check
     */
    public abstract boolean check();

    public int devid() {
        return device_id;
    }

    public String name() {
        return device_name;
    }
}

