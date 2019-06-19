/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno;

import com.maehem.jalapeno.processor.Cartridge;
import java.util.Scanner;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author mark
 */
public class Application extends javafx.application.Application {
    Canvas sharpCanvas = createCanvasGrid(1280, 1280, true);
    //Canvas blurryCanvas = createCanvasGrid(600, 300, false);

    @Override
    public void start(Stage stage) throws Exception {
        String version = System.getProperty("java.version");
        Label l = new Label("Jalapeño Running on Java " + version);
        //VBox root = new VBox(5, sharpCanvas ); //, blurryCanvas);

        Scene s = new Scene(l, 300, 200);
        //Scene s = new Scene(root);
        //primaryStage.setScene(new Scene(root));
        stage.setScene(s);
        stage.show();


        String fileName = "examples/moving-ball.p8";
        //String fileName = "examples/func-print.p8";
        //Scanner input = new Scanner( getClass().getResourceAsStream(fileName));

        Cartridge cart = new Cartridge();
        //Cartridge cart = Cartridge.load(fileName, input, 0);
        //Cartridge cart = Cartridge.load(fileName, input, 0);
        
        cart.interpret(Cartridge.readFile(getClass().getResourceAsStream(fileName)));
        

    }
    
    private Canvas createCanvasGrid(int width, int height, boolean sharp) {
        Canvas canvas = new Canvas(width, height);
//        GraphicsContext gc = canvas.getGraphicsContext2D() ;
//        gc.setLineWidth(1.0);
//        for (int x = 0; x < width; x+=10) {
//            double x1 ;
//            if (sharp) {
//                x1 = x + 0.5 ;
//            } else {
//                x1 = x ;
//            }
//            gc.moveTo(x1, 0);
//            gc.lineTo(x1, height);
//            gc.stroke();
//        }
//
//        for (int y = 0; y < height; y+=10) {
//            double y1 ;
//            if (sharp) {
//                y1 = y + 0.5 ;
//            } else {
//                y1 = y ;
//            }
//            gc.moveTo(0, y1);
//            gc.lineTo(width, y1);
//            gc.stroke();
//        }
        return canvas ;
    }

    

    public static void main(String[] args) {
        launch();
    }
}
