package com.csci6461.gw.simulator;

import com.csci6461.gw.simulator.cpu.CPU;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {
    private static Logger LOG = LogManager.getLogger("App.Main");

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/simulator.fxml"));
        primaryStage.setTitle("Group 7 simulator");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Use CLI mode if there are arguments
        LOG.debug("Starting application...");
        if(args.length > 0) {
            // TODO
            return;
        } else {
            launch(args);
        }
    }
}
