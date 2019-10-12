package com.csci6461.gw.simulator.cpu.devices;

import com.csci6461.gw.simulator.cpu.Device;
import com.csci6461.gw.simulator.util.Element;
import com.csci6461.gw.simulator.ui.Controller;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javafx.application.Platform;

public class Printer extends Device {
    private static Logger LOG = LogManager.getLogger("Device.Printer");

    private Controller ctrler;

    public Printer(int devid, String name, Controller c) {
        super(devid, name);
        ctrler = c;
    }

    @Override
    public Element input() {
        LOG.info("Reading from printer");
        return Element.fromInt(0);
    }

    @Override
    public void output(Element elem) {
        int value = elem.uvalue();  // prevent from using reference
        LOG.info("Writing to printer: {}", value);

        /*
        // It's too much of a burden....
        Platform.runLater(() -> {
            ctrler.appendToConsole(value);
        });
        */
        ctrler.appendToConsole(value);
        return;
    }
}

