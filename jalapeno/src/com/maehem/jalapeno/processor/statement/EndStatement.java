/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.statement;

import com.maehem.jalapeno.processor.MachineState;

/**
 *
 * @author mark
 */
public class EndStatement implements Statement {

    private final Returnable parent;

    public EndStatement(Returnable parent) {
        this.parent = parent;
        
        parent.setEndStatement(this);
    }

    @Override
    public int execute(MachineState ms) {
        System.out.println("Execute End for: " + parent.toString() );
        // Pop stack and go back to where func was callee from.
        return ms.pop()+1;
        //return -1;  // Return the popped statement number to go back to.
    }

    @Override
    public String toString() {
        return "End Statement: for function [" + parent.toString() + "]";
    }
    
    
}
