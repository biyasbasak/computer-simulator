package com.csci6461.gw.simulator.ui;

import javafx.beans.property.SimpleStringProperty;

public class MemoryTable {
    private final SimpleStringProperty memoryId;
    private final SimpleStringProperty memoryBinary;
    private final SimpleStringProperty memoryDecimal;
    private final SimpleStringProperty memoryFloating;

    public MemoryTable(String memoryId, String memoryBinary, String memoryDecimal, String memoryFloating) {
        super();
        this.memoryId = new SimpleStringProperty(memoryId);
        this.memoryBinary = new SimpleStringProperty(memoryBinary);
        this.memoryDecimal = new SimpleStringProperty(memoryDecimal);
        this.memoryFloating = new SimpleStringProperty(memoryFloating);
    }



    public String getMemoryId() {
        return memoryId.get();
    }

    public String getMemoryBinary() {
        return memoryBinary.get();
    }

    public String getMemoryDecimal(){
        return memoryDecimal.get();
    }
    public String getMemoryFloating(){ return memoryFloating.get();}
}
