/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor;

import com.maehem.jalapeno.processor.operation.AssignmentOperation;
import com.maehem.jalapeno.processor.operation.CircFillOperation;
import com.maehem.jalapeno.processor.operation.CommentOperation;
import com.maehem.jalapeno.processor.operation.ElseOperation;
import com.maehem.jalapeno.processor.operation.EndOperation;
import com.maehem.jalapeno.processor.operation.FunctionOperation;
import com.maehem.jalapeno.processor.operation.IfOperation;
import com.maehem.jalapeno.processor.operation.Operation;
import com.maehem.jalapeno.processor.operation.PrintOperation;
import com.maehem.jalapeno.processor.operation.RectFillOperation;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author mark
 */
public class OperationList extends ArrayList<Operation> {

    //this.lineElements = new ArrayList();
    protected static String INDENT = "    ";

    private final VariableList vars = new VariableList();

    public boolean ingest(Scanner input, int level) {
        boolean end = false;

        while (!end && input.hasNextLine()) {
            String line = input.nextLine().trim();
            System.out.println(line);

            if (line.startsWith(EndOperation.MNEMONIC)) {
                printAll();
                return false;
            } else if (line.startsWith(ElseOperation.MNEMONIC)) {
                printAll();
                return true;
            } else if (line.startsWith(FunctionOperation.MNEMONIC + " ")) {
                System.out.println(indent(level) + "Ingest " + FunctionOperation.MNEMONIC);
                add(new FunctionOperation(line, input, level));
            } else if (line.startsWith(IfOperation.MNEMONIC + " ")) {
                System.out.println(indent(level) + "Ingest " + IfOperation.MNEMONIC);
                add(new IfOperation(line, input, level));
            } else if (line.startsWith(PrintOperation.MNEMONIC)) {
                System.out.println(indent(level) + "Ingest " + PrintOperation.MNEMONIC);
                add(new PrintOperation(line, input, level));
            } else if (line.startsWith(CommentOperation.MNEMONIC)) {
                System.out.println(indent(level) + "Ingest " + CommentOperation.MNEMONIC);
                add(new CommentOperation(line, input, level));
            } else if (line.startsWith(RectFillOperation.MNEMONIC)) {
                System.out.println(indent(level) + "Ingest " + RectFillOperation.MNEMONIC);
                add(new RectFillOperation(line, input, level));
            } else if (line.startsWith(CircFillOperation.MNEMONIC)) {
                System.out.println(indent(level) + "Ingest " + CircFillOperation.MNEMONIC);
                add(new CircFillOperation(line, input, level));
            } else if (line.contains("=")) {
                System.out.println(indent(level) + "Ingest Assignment");
                ArrayList<AssignmentOperation> parse = AssignmentOperation.parse(line);
                addAll(parse);
                //add( new Assignment(line, level));
                //vars.parse(line);
                //System.out.println(vars);
            }
        }

        printAll();

        return false;
    }

    public void execute() {
        forEach((operation) -> {
            operation.execute();
        });

    }

    protected static String indent(int level) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append(INDENT);
        }

        return indent.toString();
    }

    private void printAll() {
        this.forEach((op) -> {
            System.out.println(op.getClass().getSimpleName() + " " + op.toString());
        });

    }
}
