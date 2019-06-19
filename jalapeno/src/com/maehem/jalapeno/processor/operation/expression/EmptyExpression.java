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
public class EmptyExpression implements Expression {
 
    @Override
    public Value evaluate(MachineState ms) {
        return new NumberValue(0);
    }
    
    @Override
    public String toString() {
        return "";
    }

    
}
