package com.csci6461.gw.simulator.cpu.devices;

import com.csci6461.gw.simulator.cpu.Device;
import com.csci6461.gw.simulator.util.Element;

public class Keyboard extends Device {
    public Keyboard(int devid, String name) {
        super(devid, name);
    }

    @Override
    public Element input() {
        return null;
    }

    @Override
    public void output(Element elem) {
        return;
    }
}
