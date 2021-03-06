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
public class CircFillOperation extends Operation {
    private String name;
    public static final String MNEMONIC = "CIRCFILL";
    
    public CircFillOperation(String line, Scanner input, int level) {
        this.name = line.substring(line.lastIndexOf(MNEMONIC)).trim();
    }

    @Override
    public void execute() {
        System.out.println("Draw a filled Circle....");
    }
    
}
