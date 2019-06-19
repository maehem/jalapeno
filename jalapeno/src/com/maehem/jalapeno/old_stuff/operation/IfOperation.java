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
public class IfOperation extends Operation {
    public static final String MNEMONIC = "IF";
    private final Condition condition;
    private final OperationList consequent = new OperationList();
    private final OperationList alternative = new OperationList();

    public IfOperation(String line, Scanner input, int level) {
        //this.name = line.substring(line.lastIndexOf(MNEMONIC)).trim();
        this.condition = new Condition(line.substring(
                line.indexOf(MNEMONIC) + MNEMONIC.length(),
                line.indexOf("THEN")
            ).trim());
        
        
        //System.out.println("Condition is: " + condition.toString());
        
        
        // For now items after the THEN must be on separate lines.
        if ( consequent.ingest(input, level+1) ) {
            alternative.ingest(input, level+1);
        };
        // Read lines until END
        
    }

    @Override
    public void execute() {
        if (condition.isTrue()) {
            //System.out.println("Do the thing" );
            consequent.execute();
        } else {
            //System.out.println("Nothing to do." );    
            alternative.execute();
        }
    }
    
}
