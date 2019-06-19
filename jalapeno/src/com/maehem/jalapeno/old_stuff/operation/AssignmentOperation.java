/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.operation;

import java.util.ArrayList;

/**
 *
 * @author mark
 */
public class AssignmentOperation extends Operation {
    private String name;
    private String value;

    public AssignmentOperation(String name, String value) {
        this.name = name;
        this.value = value;
    }

//    public Assignment(String line, int level) {
//        String[] split = line.split("=");
//        name = split[0].trim();
//        value = split[1].trim();
//    }

    @Override
    public String toString() {
        return name + " = " + value;
    }

    @Override
    public void execute() {
        
    }

    public static ArrayList<AssignmentOperation> parse(String line) {
         ArrayList<AssignmentOperation> parsedList = new ArrayList();
        //VariableList vars = new VariableList();
        int i1 = 0;
        int i2;

        do {
            i2 = line.indexOf("=", i1);

            String varName = line.substring(i1, i2).strip();
            i1 = i2 + 1;  // New start is after the previous equal sign.

            String varVal = line.substring(i1).trim();
            int vi = varVal.indexOf(' ');  // After the var's value, -1 if end of line.
            if ( vi < 0 ) {
                vi = varVal.length();
            }
            i2 = i1 + vi;
            varVal = varVal.substring(0, vi).trim();
            parsedList.add(new AssignmentOperation(varName, varVal));
            i1 = i2 + 1;
            // Check that we didn't set the next index past the line length.
            if ( i1 >= line.length() ) {
                i1 = line.length()-1;
            }
        } while (line.substring(i1).contains("="));  // More left
        
        return parsedList;
        
    }
   
    
    
}
