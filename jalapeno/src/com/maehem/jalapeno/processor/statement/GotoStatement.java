/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.statement;

import com.maehem.jalapeno.processor.MachineState;

/**
 *
 * @author mark
 */
/**
 * A "goto" statement jumps execution to another place in the program.
 */
public class GotoStatement implements Statement {

    private final String label;

    public GotoStatement(String label) {
        this.label = label;
    }

    @Override
    public int execute(MachineState ms) {
        if (ms.getLabels().containsKey(label)) {
            return ms.getLabels().get(label);
        }

        return -1;
    }

}
