package com.csci6461.gw.simulator.ui;

import javafx.beans.property.SimpleStringProperty;

public class MemoryTable {
    private final SimpleStringProperty memoryId;
    private final SimpleStringProperty memoryBinary;

    public MemoryTable(String memoryId, String memoryBinary) {
        super();
        this.memoryId = new SimpleStringProperty(memoryId);
        this.memoryBinary = new SimpleStringProperty(memoryBinary);
    }

    public String getMemoryId() {
        return memoryId.get();
    }

    public String getMemoryBinary() {
        return memoryBinary.get();
    }
}
