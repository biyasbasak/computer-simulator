package com.csci6461.gw.simulator.cpu.devices;

import com.csci6461.gw.simulator.cpu.Device;
import com.csci6461.gw.simulator.util.Element;
import com.csci6461.gw.simulator.ui.Controller;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition; 

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javafx.application.Platform;

public class Keyboard extends Device {
    private static Logger LOG = LogManager.getLogger("Device.Keyboard");

    private Deque<Integer> buffer = new ArrayDeque<Integer>();

    private Controller ctrler;

    public Keyboard(int devid, String name, Controller c) {
        super(devid, name);
        ctrler = c;
    }

    public void commit(Deque<Integer> buf) {
        while(buf.size() != 0) {
            buffer.addLast(buf.removeFirst());
        }
        return;
    }

    @Override
    public Element input() {
        while(buffer.size() == 0) {
            Platform.runLater(() -> {
                ctrler.update();
            });

            try {
                ctrler.getLock().lock();
                ctrler.getInputCond().await();
                ctrler.getLock().unlock();
            } catch(InterruptedException ex) {
                ex.printStackTrace();
                return Element.fromInt(0);
            }
        }

        Integer ch = buffer.removeFirst();
        Element elem = Element.fromInt(ch.intValue());
        return elem;
    }

    @Override
    public void output(Element elem) {
        LOG.info("Output to keyboard: {}", elem.uvalue());
        return;
    }
}
