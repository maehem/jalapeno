/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.operation.expression.value;

import com.maehem.jalapeno.processor.MachineState;
import java.util.Map;

/**
 *
 * @author mark
 */
/**
 * A string value.
 */
public class StringValue implements Value {

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public double toNumber() {
        return Double.parseDouble(value);
    }

    @Override
    public Value evaluate(MachineState ms) {
        return this;
    }

    private final String value;
}
