package com.csci6461.gw.simulator.ui;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;
import com.csci6461.gw.simulator.instr.Assembler;
import static com.csci6461.gw.simulator.util.StringOperations.*;
import static com.csci6461.gw.simulator.util.BitOperations.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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

    @FXML
    private TableColumn<MemoryTable, String> memoryDecimal;

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
    @FXML
    private TableColumn<RegisterTable, String> registerDecimal;

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
            memoryTableObservableList.add(i, new MemoryTable(j, memoryChunk.toString(), Integer.toString(memoryChunk.value())));
        }

        HashMap<String, Register> allRegisters =  register.getAllRegisters();
        int index = 0;
        for(String name : MachineRegisters.REG_NAMES) {
            String indexStr = Integer.toString(index);
            Register register = allRegisters.get(name);
            String registerBinary = register.toString();
            String registerDecimal = Integer.toString(register.value());
            registerTableObservableList.add(index, new RegisterTable(indexStr, name, registerBinary, registerDecimal));
            index += 1;
        }
    }

    /**
     * switch pane buttons configuration
     */
    // config the Program1 button
    @FXML
    private Button program1;
    // action on clicking the Program1 button
    @FXML
    public void runProgram1(){
        InputStream file = getClass().getResourceAsStream("/programs/program1.asm");

        try {
            String assembly = new String(readAllBytes(file));
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
    // config the Step button
    @FXML
    private Button stepButton;
    // action on clicking the step button
    @FXML
    private void step(){
        try {
            cpu.cycle();
        } catch(RuntimeException ex) {
            LOG.error("Single step error: " + ex.getMessage());
        }
        update();
    }
    // config the IPL button
    @FXML
    private Button ipl;
    // action on clicking the IPL button
    @FXML
    public void IPL(){
        initializeMemory();
        initializeRegister();
        LogPrinter.setTextFlow(logFlow);
        LogPrinter.setScrollPane(scrollPane);
        LOG.info("Machine initialized on IPL");
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
        memoryDecimal.setCellValueFactory(new PropertyValueFactory<MemoryTable, String>("memoryDecimal"));

        memoryBinary.setCellFactory(TextFieldTableCell.forTableColumn());
        memoryBinary.setOnEditCommit((TableColumn.CellEditEvent<MemoryTable, String> t) -> {
            MemoryTable registerChange = t.getTableView().getItems().get(t.getTablePosition().getRow());
            String newValue = t.getNewValue();
            int address = Integer.parseInt(registerChange.getMemoryId());
            memory.set_direct(address, newValue);
            LOG.info("Setting memory @ {} to {}.", address, Integer.parseInt(newValue, 2));
            update();
        });

        memoryDecimal.setCellFactory(TextFieldTableCell.forTableColumn());
        memoryDecimal.setOnEditCommit((TableColumn.CellEditEvent<MemoryTable, String> t) -> {
            MemoryTable registerChange = t.getTableView().getItems().get(t.getTablePosition().getRow());
            int newValue = Integer.parseInt(t.getNewValue());
            int address = Integer.parseInt(registerChange.getMemoryId());
            memory.set_direct(address, intToString(newValue, 16));
            LOG.info("Setting memory @ {} to {}.", address, newValue);
            update();
        });

        memory.initialize();
        for (int i = 0; i < 2048; i++) {
            Element memoryChunk = memory.fetch_direct(i);
            String j = String.valueOf(i);
            memoryTableObservableList.add(i, new MemoryTable(j, memoryChunk.toString(),Integer.toString(memoryChunk.value())));
        }
        memoryTableView.setItems(memoryTableObservableList);
    }

    // Register Initialization
    private void initializeRegister(){
        registerId.setCellValueFactory(new PropertyValueFactory<RegisterTable,String>("registerId"));
        registerName.setCellValueFactory(new PropertyValueFactory<RegisterTable,String>("registerName"));
        registerBinary.setCellValueFactory(new PropertyValueFactory<RegisterTable,String>("registerBinary"));
        registerBinary.setCellFactory(TextFieldTableCell.forTableColumn());
        registerBinary.setOnEditCommit((TableColumn.CellEditEvent<RegisterTable, String> t) -> {
            RegisterTable registerChange = t.getTableView().getItems().get(t.getTablePosition().getRow());
            int newValue = Integer.parseInt(t.getNewValue(), 2);
            String registerName = registerChange.getRegisterName();
            register.getAllRegisters().get(registerName).setByValue(newValue);
            LOG.info("Setting register {} to {}.", registerName, newValue);
            update();
        });

        registerDecimal.setCellValueFactory(new PropertyValueFactory<RegisterTable, String>("registerDecimal"));
        registerDecimal.setCellFactory(TextFieldTableCell.forTableColumn());
        registerDecimal.setOnEditCommit((TableColumn.CellEditEvent<RegisterTable, String> t) -> {
            RegisterTable registerChange = t.getTableView().getItems().get(t.getTablePosition().getRow());
            int newValue = Integer.parseInt(t.getNewValue());
            String registerName = registerChange.getRegisterName();
            register.getAllRegisters().get(registerName).setByValue(newValue);
            LOG.info("Setting register {} to {}.", registerName, newValue);
            update();
        });

        HashMap<String, Register> allRegisters =  register.getAllRegisters();
        int index = 0;
        for(String name : MachineRegisters.REG_NAMES) {
            String indexStr = Integer.toString(index);
            Register register = allRegisters.get(name);
            String registerBinary = register.toString();
            String registerDecimal = Integer.toString(register.value());
            registerTableObservableList.add(index, new RegisterTable(indexStr, name, registerBinary, registerDecimal));
            index += 1;
        }
        registerTableView.setItems(registerTableObservableList);
    }
}
