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
 * An if then statement jumps execution to another place in the program, but
 * only if an expression evaluates to something other than 0.
 */
public class WhileStatement implements Statement, Returnable {

    private final Expression condition;
    private EndStatement endStatement;

    public WhileStatement(Expression condition) {
        this.condition = condition;
    }

    @Override
    public int execute(MachineState ms) {
        double value = condition.evaluate(ms).toNumber();
        if (value != 0) {
            // Push current statement onto end's stack. 
            ms.push(-1); // re-evaluate after end of loop.
        } else {
            // Break loop and jump to end+1
            return endStatement.execute(ms);
        }

        return -1;
    }

    @Override
    public String toString() {
        return "While Statement: while (" + condition + ")";
    }

    @Override
    public void setEndStatement(EndStatement es) {
        this.endStatement = es;
    }

    @Override
    public EndStatement getEndStatement() {
        return endStatement;
    }

}
