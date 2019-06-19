/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.statement;

import com.maehem.jalapeno.processor.MachineState;
import com.maehem.jalapeno.processor.operation.expression.value.NumberValue;
import com.maehem.jalapeno.processor.operation.expression.value.StringValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author mark
 */
    /**
     * An "input" statement reads input from the user and stores it in a
     * variable.
     */
    public class InputStatement implements Statement {
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader buffer = new BufferedReader(converter);
        
        public InputStatement(String name) {
            this.name = name;
        }
        
        @Override
        public int execute(MachineState ms) {
            try {
                String input = buffer.readLine();
                
                // Store it as a number if possible, otherwise use a string.
                try {
                    double value = Double.parseDouble(input);
                    ms.getVariables().put(name, new NumberValue(value));
                } catch (NumberFormatException e) {
                    ms.getVariables().put(name, new StringValue(input));
                }
            } catch (IOException e1) {
                // HACK: Just ignore the problem.
            }
            
            return -1;
        }

        private final String name;

    }

