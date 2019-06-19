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
 * A numeric value. Jasic uses doubles internally for all numbers.
 */
public class NumberValue implements Value {

    public NumberValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public double toNumber() {
        return value;
    }

    @Override
    public Value evaluate(MachineState ms) {
        return this;
    }

    private final double value;
}
