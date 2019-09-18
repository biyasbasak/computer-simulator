package com.csci6461.gw.simulator.ui;

import javafx.beans.property.SimpleStringProperty;

public class RegisterTable{
    private final SimpleStringProperty registerId;
    private final SimpleStringProperty registerName;
    private final SimpleStringProperty registerBinary;

    public RegisterTable(String registerId, String registerName, String registerBinary) {
        this.registerId = new SimpleStringProperty(registerId);
        this.registerName = new SimpleStringProperty(registerName);
        this.registerBinary = new SimpleStringProperty(registerBinary);
    }

    public String getRegisterId() {
        return registerId.get();
    }

    public String getRegisterName() {
        return registerName.get();
    }

    public String getRegisterBinary() {
        return registerBinary.get();
    }
}