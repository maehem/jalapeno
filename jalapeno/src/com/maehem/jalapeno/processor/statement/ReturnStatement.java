/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.statement;

import com.maehem.jalapeno.processor.MachineState;
import com.maehem.jalapeno.processor.operation.expression.Expression;
import com.maehem.jalapeno.processor.operation.expression.value.NumberValue;

/**
 *
 * @author mark
 */
/**
 * A "return" statement evaluates an expression, and puts it on the return value variable,
 * and displays it to the user.
 */
public class ReturnStatement implements Statement {
    private final Expression expression;
    private final Returnable parent;

    public ReturnStatement(Expression expression, Returnable parent) {
        this.expression = expression;
        this.parent = parent;
    }

    @Override
    public int execute(MachineState ms) {
        
        // set the MS return value;
        ms.getVariables().put(MachineState.RTN_VALUE_KEY, expression.evaluate(ms));
        System.out.println(ms.getVariables().getOrDefault(MachineState.RTN_VALUE_KEY, new NumberValue(0)));
        
        // return statement index for this function's END statement.        
        return ms.pop()+1;
    }

    @Override
    public String toString() {
        return "Return Statement: " + expression.toString();
    }

    
}
