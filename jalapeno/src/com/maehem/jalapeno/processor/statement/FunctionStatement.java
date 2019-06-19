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
public class FunctionStatement implements Statement, Returnable {

    private final String label;
    private EndStatement endStatement;
    private final Expression args;

    public FunctionStatement(String label, Expression args) {
        this.label = label;
        this.args = args;
    }

    @Override
    public int execute(MachineState ms) {
        
        // Functions don't execute directly. They are called by a FunctionCall.
        // So if we get here during execution, we simply skip just past the end statement.
        if ( endStatement == null ) {
            System.out.println("ERROR: No end statement found for function: " + label);
            return ms.getStatements().size(); // Cause program to end.
        }
        
        
        //  TODO: How to get a reference back to this function when end or return is called?
        return ms.getStatements().indexOf(endStatement)+1;
    }
    
    @Override
    public void setEndStatement( EndStatement es ) {
        this.endStatement = es;
    }
    
    @Override
    public EndStatement getEndStatement() {
        return endStatement;
    }

    public String getLabel() {
        return label;
    }
    
    @Override
    public String toString() {
        return "Function Statement: " + label + "( "+ args.toString() +" )";
    }

    
}
