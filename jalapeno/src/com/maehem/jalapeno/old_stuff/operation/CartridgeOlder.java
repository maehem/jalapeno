/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor;

import com.maehem.jalapeno.processor.operation.FunctionOperation;
import com.maehem.jalapeno.processor.operation.Operation;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mark
 */
public class CartridgeOlder extends OperationList {

    private final String name;
    public static String INIT_MNEMONIC = "_INIT";
    public static String UPDATE_MNEMONIC = "_UPDATE";
    public static String DRAW_MNEMONIC = "_DRAW";

    int mark = 0;

    private Operation _update;
    private Operation _init;
    private Operation _draw;
    
    //private ArrayList<Operation> lineElements;
    private CartridgeOlder(String name) {
        this.name = name;
    }

    public static CartridgeOlder load(String name, Scanner input, int level) {
        CartridgeOlder cart = new CartridgeOlder(name);

        cart.ingest(input, level);

        cart.stream().filter((operation) -> ( operation instanceof FunctionOperation )).forEachOrdered((operation) -> {
            if ( operation.getName().equals(INIT_MNEMONIC) ) {
                cart.setInit( operation );
            } else if (operation.getName().equals(UPDATE_MNEMONIC) ) {
                cart.setUpdate( operation );
            } else if (operation.getName().equals(DRAW_MNEMONIC) ) {
                cart.setDraw( operation ); 
            }
        });
        return cart;
    }

    public void run() {
        System.out.println("Running " + name);
        if ( _update == null ) {
            System.out.println("_UPDATE function not defined. Cannot Run.");
            return;
        }
        
        if ( _draw == null ) {
            System.out.println("_DRAW function not defined. Cannot Run.");
            return;
        }
        
        if ( _init != null ) _init.execute();
        
        // Call init once
        // start the loop at 30 fps.
        //   call init
        // Tie in draw to java.repaint()
        new Timer().schedule(
                new TimerTask() {

            @Override
            public void run() {
                if (mark == 0) {
                    System.out.println("update");
                }
                mark++;
                if (mark > 29) {
                    mark = 0;
                }
                _update.execute();
                // 
            }

        }, 15, 33);  // 33mS or 30fps.
    }

    private void setInit(Operation operation) {
        this._init = operation;
    }

    private void setUpdate(Operation operation) {
        this._update = operation;
    }

    private void setDraw(Operation operation) {
        this._draw = operation;
    }

}
