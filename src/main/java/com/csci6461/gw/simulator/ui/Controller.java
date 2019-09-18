package com.csci6461.gw.simulator.ui;

import com.csci6461.gw.simulator.memory.Memory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.BitSet;
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

    // Initialize the simulator on starting
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeMemory();
    }

    // Memory Initialization
    private void initializeMemory(){
        memoryId.setCellValueFactory(new PropertyValueFactory<MemoryTable, String>("memoryId"));
        memoryBinary.setCellValueFactory(new PropertyValueFactory<MemoryTable, String>("memoryBinary"));

        memory.initialize();

        for (int i = 0; i < 2048; i++) {
            BitSet memoryChunk = memory.fetch(i);
            String j = String.valueOf(i);
            memoryTableObservableList.add(i, new MemoryTable(j, "0000000000000000"));
            System.out.println(memoryChunk);
            // memoryTableObservableList.add();
        }
        //BitSet memoryChunk = memory.fetch(1);

        memoryTableView.setItems(memoryTableObservableList);
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

    private void printLog(){

    }





}
