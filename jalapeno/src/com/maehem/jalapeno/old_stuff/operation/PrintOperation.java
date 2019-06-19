/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.operation;

import java.util.Scanner;

/**
 *
 * @author mark
 */
public class PrintOperation extends Operation {
    private String name;
    public static final String MNEMONIC = "PRINT";
    
    private String content;
    
    public PrintOperation(String line, Scanner input, int level ) {
        this.name = line.substring(
                line.indexOf("\"") + 1,
                line.lastIndexOf("\"")
        ).trim();
        
    }

    @Override
    public void execute() {
        System.out.println(name);
    }
}
