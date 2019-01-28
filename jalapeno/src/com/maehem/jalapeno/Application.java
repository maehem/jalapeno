/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author mark
 */
public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws Exception {
        String version = System.getProperty("java.version");
        Label l = new Label("Hello FX Running on Java " + version);
        Scene s = new Scene(l, 300, 200);
        stage.setScene(s);
        stage.show();
        
    }
    
    public static void main(String[] args) {
        launch();
    }
}
