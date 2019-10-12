package com.csci6461.gw.simulator.ui;

import com.csci6461.gw.simulator.cpu.CPU;
import com.csci6461.gw.simulator.memory.Cache;
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
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock; 

/**
 * This is the Controller Class
 * It connects all events between the GUI and back-ends operations
 * And the actions binding
 */
public class Controller implements Initializable {

    private static Logger LOG = LogManager.getLogger("UI.Controller");

    private CPU cpu = new CPU(this);
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
     * This is the cache tableview configuration
     */
    // config the Cache table
    @FXML
    private TableView<CacheTable> cacheTableView;
    // config the cache table columns
    @FXML
    private TableColumn<CacheTable, String> cacheIndex;
    @FXML
    private TableColumn<CacheTable, String> cacheTag;
    @FXML
    private TableColumn<CacheTable, String> cacheOffset;
    @FXML
    private TableColumn<CacheTable, String> cacheBinary;
    // create a observablelist object to present cache data
    private ObservableList<CacheTable> cacheTableObservableList = FXCollections.observableArrayList();
    // get a cache object, for initialization and further usage
    private Cache cache = memory.getCache();

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

    private Deque<Integer> bufferQ = new ArrayDeque<Integer>();

    private Deque<Integer> obufferQ = new ArrayDeque<Integer>();

    private Deque<String> logQ = new ArrayDeque<String>();

    private ReentrantLock lock = new ReentrantLock();

    private Condition input_cond = lock.newCondition();

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
     *  This is the console pane configuration
     */
    @FXML
    private TextArea console;
    
    @FXML
    private Button test;

    @FXML
    private void testareainput() {
        String text = console.getText();
        System.out.println(text.toString());
    }

    public CPU getCPU() {
        return cpu;
    }

    public Condition getInputCond() {
        return input_cond;
    }

    public ReentrantLock getLock() {
        return lock;
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

    public void update() {
        registerTableView.getItems().clear();
        memoryTableView.getItems().clear();

        // update memory
        for (int i = 0; i < 2048; i++) {
            Element memoryChunk = memory.fetch_direct(i);
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
        
        // flush output buffer
        String t = console.getText();
        while(obufferQ.size() != 0) {
            t += (char)obufferQ.removeFirst().intValue();
        }
        console.setText(t);
        console.positionCaret(t.length());

        // flush log buffer
        //System.out.println(logQ.size());
        while(logQ.size() >= 200) {     // remove redundant logs
            logQ.removeFirst();
        }
        while(logQ.size() != 0) {
            Text message = new Text(logQ.removeFirst());
            if(logFlow.getChildren().size() == 200) {
                logFlow.getChildren().remove(0);
            }
            logFlow.getChildren().add(message);
        }
        logQ.clear();
        //System.out.println(logFlow.getChildren().size());
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
        class CPUTask implements Runnable {
            Controller ctrl;
            public CPUTask(Controller c) { ctrl = c; }
            public void run() {
                ctrl.getCPU().step();
                Platform.runLater(() -> {
                    ctrl.update();
                });
            }
        }

        try {
            Thread t = new Thread(new CPUTask(this));
            t.start();
        } catch(RuntimeException ex) {
            LOG.error("Single step error: " + ex.getMessage());
        } catch(Exception ex) {
            LOG.error("Threading error: " + ex.getMessage());
        }
    }

    @FXML
    private void runSim() {
        class CPURunTask implements Runnable {
            Controller ctrl;
            public CPURunTask(Controller c) { ctrl = c; }
            public void run() {
                ctrl.getCPU().run();
                Platform.runLater(() -> {
                    ctrl.update();
                });
            }
        }

        try {
            Thread t = new Thread(new CPURunTask(this));
            t.start();
        } catch(RuntimeException ex) {
            LOG.error("Run error: " + ex.getMessage());
        } catch(Exception ex) {
            LOG.error("Threading error: " + ex.getMessage());
        }
    }

    // config the IPL button
    @FXML
    private Button ipl;
    // action on clicking the IPL button
    @FXML
    public void IPL(){
        memory.initialize();
        register.initialize();
        cpu.initialize();
        LOG.info("Machine initialized on IPL");
    }

    /**
     * Initialization on starting the application
     */
    // Initialize the simulator on starting
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        memoryTableView.setItems(memoryTableObservableList);

        memoryDecimal.setCellFactory(TextFieldTableCell.forTableColumn());
        memoryDecimal.setOnEditCommit((TableColumn.CellEditEvent<MemoryTable, String> t) -> {
            MemoryTable registerChange = t.getTableView().getItems().get(t.getTablePosition().getRow());
            int newValue = Integer.parseInt(t.getNewValue());
            int address = Integer.parseInt(registerChange.getMemoryId());
            memory.set_direct(address, intToString(newValue, 16));
            LOG.info("Setting memory @ {} to {}.", address, newValue);
            update();
        });

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
        registerTableView.setItems(registerTableObservableList);

        /* Simulate terminal behaviour */
        console.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch(keyEvent.getCode()) {
                    case UP:
                    case DOWN:
                    case PAGE_UP:
                    case PAGE_DOWN:
                    case HOME:
                    case END:
                    case LEFT:
                    case RIGHT:
                    case TAB:
                        keyEvent.consume();
                        return;
                }
            }
        });

        console.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                console.positionCaret(console.getText().length());
                return;
            }
        });

        console.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    if(bufferQ.size() == 0) {
                        keyEvent.consume();
                    } else {
                        bufferQ.removeLast();
                    }
                } else if(keyEvent.getCode() == KeyCode.ENTER) {
                    bufferQ.add(10);    // new-line
                    lock.lock();
                    cpu.commit(bufferQ);
                    input_cond.signalAll();
                    lock.unlock();
                }
            }
        });

        console.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    return;
                }

                String ch = keyEvent.getCharacter();
                if(ch.length() != 0) {
                    if(ch.charAt(0) != '\r') {
                        bufferQ.addLast((int)ch.charAt(0));
                    }
                }
            }
        });

        LogPrinter.setTextFlow(logFlow);
        LogPrinter.setScrollPane(scrollPane);
        LogPrinter.setController(this);
        LOG.info("initialize successful");
        update();
    }

    public void appendToConsole(Integer ch) {
        obufferQ.addLast(ch);
        return;
    }

    public void appendToLog(String msg) {
        logQ.addLast(msg);
        return;
    }
    
    // cache initialization
    public void initializeCache(){
        cacheIndex.setCellValueFactory(new PropertyValueFactory<CacheTable,String >("Index"));
        cacheTag.setCellValueFactory(new PropertyValueFactory<CacheTable,String >("Tag"));
        cacheOffset.setCellValueFactory(new PropertyValueFactory<CacheTable,String >("Offset"));
        cacheBinary.setCellValueFactory(new PropertyValueFactory<CacheTable,String >("binary"));
    }
}
