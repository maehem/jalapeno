/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.operation.expression;

import com.maehem.jalapeno.processor.MachineState;
import com.maehem.jalapeno.processor.operation.expression.value.NumberValue;
import com.maehem.jalapeno.processor.operation.expression.value.Value;
import com.maehem.jalapeno.processor.statement.FunctionCallStatement;

/**
 *
 * @author mark
 *
 * A function call expression evaluates based on return value of call..
 */
public class FunctionCallExpression implements Expression {

    private final String name;
    //private final Expression args;
    private final FunctionCallStatement statement;

    public FunctionCallExpression(String name) {
        this(name, null);
    }

    public FunctionCallExpression(String name, Expression args) {
        this.name = name;
        //this.args = args;
        this.statement = new FunctionCallStatement(name, args);
    }

    @Override
    public Value evaluate(MachineState ms) {
        System.out.println("FunctionCallExpression evaluate: " + statement.toString());
        // Check if functions contains this named call.
        //FunctionStatement f = ms.getNamedFunction(name);
        //if (statement != null) {
        //int execute = statement.execute(ms);
        //ms.setCurrentStatementIndex(execute);
        // ms.run(); //end of statement?
        ms.runFunction(statement);  // Will set return value key
        return ms.getVariables().getOrDefault(MachineState.RTN_VALUE_KEY, new NumberValue(0));
        //}
        //return new NumberValue(0);
    }

    @Override
    public String toString() {
        return statement.toString();
    }

}
