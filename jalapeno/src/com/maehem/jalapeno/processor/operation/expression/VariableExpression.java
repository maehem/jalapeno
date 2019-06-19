/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.operation.expression;

import com.maehem.jalapeno.processor.MachineState;
import com.maehem.jalapeno.processor.operation.expression.value.NumberValue;
import com.maehem.jalapeno.processor.operation.expression.value.Value;

/**
 *
 * @author mark
 */
    /**
     * A variable expression evaluates to the current value stored in that
     * variable.
     */
    public class VariableExpression implements Expression {
        private final String name;

        public VariableExpression(String name) {
            this.name = name;
        }
        
        @Override
        public Value evaluate(MachineState ms) {
            if (ms.getVariables().containsKey(name)) {
                return ms.getVariables().get(name);
            }
            return new NumberValue(0);
        }

    @Override
    public String toString() {
        return name;
    }
        
        
    }
    
