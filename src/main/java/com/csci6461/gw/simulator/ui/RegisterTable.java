package com.csci6461.gw.simulator.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

public class RegisterTable{
    private final SimpleStringProperty registerId;
    private final SimpleStringProperty registerName;
    private final SimpleStringProperty registerBinary;
    private final SimpleStringProperty registerDecimal;
    private final SimpleStringProperty registerFloating;

    public RegisterTable(String registerId, String registerName, String registerBinary, String registerDecimal, String registerFloating) {
        this.registerId = new SimpleStringProperty(registerId);
        this.registerName = new SimpleStringProperty(registerName);
        this.registerBinary = new SimpleStringProperty(registerBinary);
        this.registerDecimal = new SimpleStringProperty(registerDecimal);
        this.registerFloating = new SimpleStringProperty(registerFloating);
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

    public String getRegisterDecimal(){ return registerDecimal.get();}

    public String getRegisterFloating(){ return registerFloating.get();}
}