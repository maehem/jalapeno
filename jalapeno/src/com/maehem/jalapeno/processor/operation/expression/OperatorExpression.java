/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.operation.expression;

import com.maehem.jalapeno.processor.MachineState;
import com.maehem.jalapeno.processor.operation.expression.value.NumberValue;
import com.maehem.jalapeno.processor.operation.expression.value.StringValue;
import com.maehem.jalapeno.processor.operation.expression.value.Value;

/**
 *
 * @author mark
 */
/**
 * An operator expression evaluates two expressions and then performs some
 * arithmetic operation on the results.
 */
public class OperatorExpression implements Expression {

    private final Expression left;
    private final char operator;
    private final Expression right;

    public OperatorExpression(Expression left, char operator,
            Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Value evaluate(MachineState ms) {
        Value leftVal = left.evaluate(ms);
        Value rightVal = right.evaluate(ms);

        switch (operator) {
            case '=':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber()
                            == rightVal.toNumber()) ? 1 : 0);
                } else {
                    return new NumberValue(leftVal.toString().equals(
                            rightVal.toString()) ? 1 : 0);
                }
            case '+':
                // Addition if the left argument is a number, otherwise do
                // string concatenation.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue(leftVal.toNumber()
                            + rightVal.toNumber());
                } else {
                    return new StringValue(leftVal.toString()
                            + rightVal.toString());
                }
            case '-':
                return new NumberValue(leftVal.toNumber()
                        - rightVal.toNumber());
            case '*':
                return new NumberValue(leftVal.toNumber()
                        * rightVal.toNumber());
            case '/':
                return new NumberValue(leftVal.toNumber()
                        / rightVal.toNumber());
            case '<':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber()
                            < rightVal.toNumber()) ? 1 : 0);
                } else {
                    return new NumberValue((leftVal.toString().compareTo(
                            rightVal.toString()) < 0) ? 1 : 0);
                }
            case '>':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber()
                            > rightVal.toNumber()) ? 1 : 0);
                } else {
                    return new NumberValue((leftVal.toString().compareTo(
                            rightVal.toString()) > 0) ? 1 : 0);
                }
        }
        throw new Error("Unknown operator.");
    }

    @Override
    public String toString() {
        return left.toString() + " " + operator + " " + right.toString();
    }

}
