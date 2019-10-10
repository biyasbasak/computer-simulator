package com.csci6461.gw.simulator.cpu.devices;

import com.csci6461.gw.simulator.cpu.Device;
import com.csci6461.gw.simulator.util.Element;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Printer extends Device {
    private static Logger LOG = LogManager.getLogger("Device.Printer");

    public Printer(int devid, String name) {
        super(devid, name);
    }

    @Override
    public Element input() {
        return Element.fromInt(0);
    }

    @Override
    public void output(Element elem) {
        return;
    }
}
