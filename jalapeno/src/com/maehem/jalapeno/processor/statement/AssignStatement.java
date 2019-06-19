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
    /**
     * An assignment statement evaluates an expression and stores the result in
     * a variable.
     */
    public class AssignStatement implements Statement {
        public AssignStatement(String name, Expression value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public int execute(MachineState ms) {
            System.out.println("Exceute " + toString());
            ms.getVariables().put(name, value.evaluate(ms));
            
            return -1;
        }

        private final String name;
        private final Expression value;

    @Override
    public String toString() {
        return "Assignment Statement: " + name  + " = " + value.toString();
    }
        
        
    }
    
