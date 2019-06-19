/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.operation.expression;

import com.maehem.jalapeno.processor.MachineState;
import com.maehem.jalapeno.processor.operation.expression.value.Value;

/**
 *
 * @author mark
 */
    public interface Expression {
        /**
         * Expression classes implement this to evaluate the expression and
         * return the value.
         * 
         * @return The value of the calculated expression.
         */
        //Value evaluate(Map<String, Value> variables);
        Value evaluate(MachineState ms);
        
        
    }
