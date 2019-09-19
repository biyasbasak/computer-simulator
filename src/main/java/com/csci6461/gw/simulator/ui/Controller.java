package com.csci6461.gw.simulator.ui;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;
import com.csci6461.gw.simulator.instr.Assembler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * This is the Controller Class
 * It connects all events between the GUI and back-ends operations
 * And the actions binding
 */
public class Controller implements Initializable {

    private static Logger LOG = LogManager.getLogger("UI.Controller");

    private CPU cpu = new CPU();
    /**
     * This is the Memory tableview configuration
     */
    // config the Memory table
    @FXML
    private TableView<MemoryTable> memoryTableView;
    // config the id column of Memory table
    @FXML
    private TableColumn<MemoryTable, String> memoryId;
    // config the binary column of Memory table
    @FXML
    private TableColumn<MemoryTable, String> memoryBinary;
    // creat a "observablelist" object to present memory data
    private ObservableList<MemoryTable> memoryTableObservableList = FXCollections.observableArrayList(
            );
    // get a memory object, for initialization and further usage
    private Memory memory = cpu.getMemory();
    /**
     * This is the Memory tableview configuration
     */
    @FXML
    private TableView<RegisterTable> registerTableView;
    @FXML
    private TableColumn<RegisterTable, String> registerId;
    @FXML
    private TableColumn<RegisterTable, String> registerName;
    @FXML
    private TableColumn<RegisterTable, String> registerBinary;

    private ObservableList<RegisterTable> registerTableObservableList = FXCollections.observableArrayList(
            );
    private MachineRegisters register = cpu.getRegisters();

    /**
     * This is the log text field configuration
     */
    // config the log pane Text flow
    @FXML
    private TextFlow logFlow;
    // config the log scroll pane to make the log text scroll
    @FXML
    private ScrollPane scrollPane;
    // config the log pane's clear button
    @FXML
    private Button clear;
    // action on clicking the clear button
    @FXML
    public void clearLog(){
        LogPrinter.clear();
    }

    /**
     * binary instruction input configuration
     */
    // config the "Run Binary" button
    @FXML
    private Button runBinaryButton;
    // config the binary input text field
    @FXML
    private TextField binaryInput;
    // action on clicking the "Run Binary" button
    @FXML
    public void runBinaryCode(){
        LOG.info(binaryInput.getText());
    }

    private void update() {
        registerTableView.getItems().clear();
        memoryTableView.getItems().clear();
        
        // update memory
        for (int i = 0; i < 2048; i++) {
            Element memoryChunk = memory.fetch(i);
            String j = String.valueOf(i);
            memoryTableObservableList.add(i, new MemoryTable(j, memoryChunk.toString()));
        }

        HashMap<String, Register> allRegisters =  register.getAllRegisters();
        int index = 0;
        for(String name : MachineRegisters.REG_NAMES) {
            String indexStr = Integer.toString(index);
            Register register = allRegisters.get(name);
            String registerBinary = register.toString();
            registerTableObservableList.add(index, new RegisterTable(indexStr, name, registerBinary));
            index += 1;
        }
    }

    /**
     * Test pane buttons configuration
     */
    // config the Program1 button
    @FXML
    private Button program1;
    // action on clicking the Program1 button
    @FXML
    public void runProgram1(){
        InputStream file = getClass().getResourceAsStream("/programs/program1.asm");

        try {
            String assembly = new String(file.readAllBytes());
            Assembler asm = new Assembler();
            String[] program = asm.assemble(assembly);
            cpu.loadProgram(program);
        } catch(IOException ex) {
            LOG.error("Error reading program1.asm: {}", ex.getMessage());
            ex.printStackTrace();
        }
        LOG.info("Program1 loaded.");

        update();
    }

    @FXML
    private Button stepButton;

    @FXML
    private void step(){
        cpu.cycle();

        update();
    }

    /**
     * Initialization on starting the application
     */
    // Initialize the simulator on starting
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMemory();
        initializeRegister();
        LogPrinter.setTextFlow(logFlow);
        LogPrinter.setScrollPane(scrollPane);
        LOG.info("initialize successful");
    }

    // Memory Initialization
    private void initializeMemory(){
        memoryId.setCellValueFactory(new PropertyValueFactory<MemoryTable, String>("memoryId"));
        memoryBinary.setCellValueFactory(new PropertyValueFactory<MemoryTable, String>("memoryBinary"));
        memory.initialize();
        for (int i = 0; i < 2048; i++) {
            Element memoryChunk = memory.fetch(i);
            String j = String.valueOf(i);
            memoryTableObservableList.add(i, new MemoryTable(j, memoryChunk.toString()));
        }
        memoryTableView.setItems(memoryTableObservableList);
    }

    // Register Initialization
    private void initializeRegister(){
        registerId.setCellValueFactory(new PropertyValueFactory<RegisterTable,String>("registerId"));
        registerName.setCellValueFactory(new PropertyValueFactory<RegisterTable,String>("registerName"));
        registerBinary.setCellValueFactory(new PropertyValueFactory<RegisterTable,String>("registerBinary"));
        HashMap<String, Register> allRegisters =  register.getAllRegisters();
        int index = 0;
        for(String name : MachineRegisters.REG_NAMES) {
            String indexStr = Integer.toString(index);
            Register register = allRegisters.get(name);
            String registerBinary = register.toString();
            registerTableObservableList.add(index, new RegisterTable(indexStr, name, registerBinary));
            index += 1;
        }
        registerTableView.setItems(registerTableObservableList);
    }
}
