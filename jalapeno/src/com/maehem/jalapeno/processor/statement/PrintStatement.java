/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.statement;

import com.maehem.jalapeno.processor.MachineState;
import com.maehem.jalapeno.processor.operation.expression.Expression;
import java.io.BufferedReader;

/**
 *
 * @author mark
 */
/**
 * A "print" statement evaluates an expression, converts the result to a string,
 * and displays it to the user.
 */
public class PrintStatement implements Statement {
    private final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public int execute(MachineState ms) {
        System.out.println(expression.evaluate(ms).toString());
        return -1;
    }

    @Override
    public String toString() {
        return "Print Statement: " + expression.toString();
    }

    
}
