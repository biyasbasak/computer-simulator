package com.csci6461.gw.simulator.cpu.devices;

import com.csci6461.gw.simulator.cpu.Device;
import com.csci6461.gw.simulator.util.Element;

import java.util.ArrayDeque;
import java.util.Deque;

public class Keyboard extends Device {
    private Deque<Integer> buffer = new ArrayDeque<Integer>();

    public Keyboard(int devid, String name) {
        super(devid, name);
    }

    public void commit(Deque<Integer> buf) {
        buf.forEach((Integer a) -> {
            buffer.addLast(a);
        });
        return;
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
