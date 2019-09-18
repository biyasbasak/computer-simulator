package com.csci6461.gw.simulator.ui;

import com.csci6461.gw.simulator.memory.Memory;
import com.csci6461.gw.simulator.reg.MachineRegisters;
import com.csci6461.gw.simulator.reg.Register;
import com.csci6461.gw.simulator.util.Element;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.BitSet;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * This is the Controller Class
 * It connects all events between the GUI and back-ends operations
 * And the actions binding
 */
public class Controller implements Initializable {

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
    private Memory memory = new Memory();

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
    private MachineRegisters register = new MachineRegisters();

    // Initialize the simulator on starting
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMemory();
        initializeRegister();
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
            System.out.println(memoryChunk);
            // memoryTableObservableList.add();
        }
        //BitSet memoryChunk = memory.fetch(1);

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


    // config binary input Textfield
    @FXML
    private TextField binaryInput;

    // config Buttons
    @FXML
    private Button runBinaryButton;

    //
    @FXML
    public void runBinaryCode(){
        System.out.println(binaryInput.getText());
    }

    // config the log pane Text flow
    @FXML
    private TextFlow logFlow;

    public void printLog(String message){
        Text text = new Text(message);
        logFlow.getChildren().add(text);
    }
}
