/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor;

import com.maehem.jalapeno.processor.operation.expression.value.Value;
import com.maehem.jalapeno.processor.statement.FunctionCallStatement;
import com.maehem.jalapeno.processor.statement.FunctionStatement;
import com.maehem.jalapeno.processor.statement.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author mark
 */
public class MachineState {

    public static final String RTN_VALUE_KEY = "RTN_VALUE";
    
    private int currentStatementIndex = 0;
    
    private List<Statement> statements = null;
    private final LinkedHashMap<String, Integer> labels = new LinkedHashMap<>();
    private final Map<String, Value> variables = new HashMap<>();
    private final Stack stack = new Stack();

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
        if (getNamedFunction("_INIT") == null) {
            System.out.println("Warning!  _INIT() method was not defined.");
        }
        
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public LinkedHashMap<String, Integer> getLabels() {
        return labels;
    }

    public Map<String, Value> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Statements:\n");
        statements.forEach((s) -> {
            sb.append("\t").append(s.toString()).append("\n");
        });

        sb.append("\nLabels:\n");
        labels.forEach((t, u) -> {
            sb.append("\t").append(t).append("\n");
        });

        sb.append("\nStatements to execute: ").append(statements.size());

        return sb.toString();
    }

    public boolean canContinue() {
        return getCurrentStatementIndex() < getStatements().size();
    }
    
    public Statement getCurrentStatement() {
        return statements.get(currentStatementIndex);
    }
    
    public int getCurrentStatementIndex() {
        return currentStatementIndex;
    }
    
    public void restart() {
        currentStatementIndex = 0;
    }

    protected void tick() {
        currentStatementIndex++;
    }

    public void setCurrentStatementIndex(int newIndex) {
        currentStatementIndex = newIndex;
    }

    public void push() {
        push(0);
    }
    
    public void push(int relativeIndex ) {
        //System.out.println("push");
        stack.push(currentStatementIndex+relativeIndex);        
    }
    
    public int pop() {
        //System.out.println("pop");
        return (int) stack.pop();
    }

    public FunctionStatement getNamedFunction(String name) {
        for (Statement s : statements) {
            if ( s instanceof FunctionStatement && ((FunctionStatement)s).getLabel().equals(name ) ) {
                return (FunctionStatement) s;
            }            
        }
        
        return null;
    }

    void run() {
        while (getCurrentStatementIndex() < getStatements().size()) {
            int result = getCurrentStatement().execute(this);
            if (result == -1) {
                tick();
            } else {
                setCurrentStatementIndex(result);
            }
        }
    }
    
    public void runFunction( FunctionCallStatement s) {
        // Capture current statement index.
        int endIndex = getCurrentStatementIndex() +1;
        int execute = s.execute(this); // push current line on stack
        setCurrentStatementIndex(execute);
        
        // Run until captured statement index + 1 (END of function).
        while (getCurrentStatementIndex() < getStatements().size()) {
            int result = getCurrentStatement().execute(this);
            if (result == -1) {
                tick();
            } else if ( result == endIndex ) {
                // END reached
                System.out.println("end of function: " + s.getName());
                return;
            } else {
                setCurrentStatementIndex(result);
            }
        }
        
        
        // Need to intercept the END statement.
    }

}
