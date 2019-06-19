/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.operation;

/**
 *
 * @author mark
 */
public class Condition {
    private final String condString;
    
    public Condition( String condString ) {
        this.condString = condString;
    }
    
    public boolean isTrue() {
        // Evaluate
        return false;
    }

    @Override
    public String toString() {
        return condString;
    }
    
    
}
