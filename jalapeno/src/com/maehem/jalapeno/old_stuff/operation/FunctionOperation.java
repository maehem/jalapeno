/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.operation;

import com.maehem.jalapeno.processor.OperationList;
import java.util.Scanner;

/**
 *
 * @author mark
 */
public class FunctionOperation extends Operation {
    public static final String MNEMONIC = "FUNCTION";
    
    private final OperationList operations = new OperationList();
    
    public FunctionOperation(String line, Scanner input, int level) {
        this.name = line.substring(
                line.lastIndexOf(MNEMONIC)+MNEMONIC.length(),
                line.lastIndexOf("(")
        ).trim();
        
        operations.ingest(input, level+1);
    }

    @Override
    public void execute() {
        //System.out.println("Execute function: " + name);
        operations.execute();
    }
    
}
