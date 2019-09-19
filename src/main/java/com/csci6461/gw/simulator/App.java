package com.csci6461.gw.simulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/simulator.fxml"));
        primaryStage.setTitle("Group 7 simulator");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Use CLI mode if there are arguments
        if(args.length > 0) {
            // TODO
            return;
        } else {
            launch(args);
        }
    }
}
