/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.statement;

import com.maehem.jalapeno.processor.MachineState;
import com.maehem.jalapeno.processor.operation.expression.Expression;

/**
 *
 * @author mark
 */
public class FunctionCallStatement implements Statement {

    private final String name;
    private final Expression args;
    //private final Stack calledFrom = new Stack();

    public FunctionCallStatement( String name, Expression args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public int execute(MachineState ms) {
        System.out.println("Excecute " + toString());
        
        // Push current statement onto end's stack. 
        ms.push();
        
        // Set up args.
        // Args here are values that get plugged into the matching args of
        // the funciton definition.  Those args 
        
        FunctionStatement f = ms.getNamedFunction(name);
        if ( f == null ) {
            System.out.println("Function: " + name + " not defined!");
            return ms.getStatements().size(); // Set excecution to end.
        }
        
        return ms.getStatements().indexOf(f)+1;        
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "FunctionCall Statement: " + name + "(" + args + ")";
    }
}
