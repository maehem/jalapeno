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
public abstract class Operation {
    protected String name;
    
    public String getName() {
        return name;
    }
    
    public abstract void execute();

    @Override
    public String toString() {
        return getName();
    }
    
    
}
