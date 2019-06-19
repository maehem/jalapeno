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
// Abstract syntax tree (AST) ----------------------------------------------
// These classes define the syntax tree data structures. This is how code is
// represented internally in a way that's easy for the interpreter to
// understand.
//
// HACK: Unlike most real compilers or interpreters, the logic to execute
// the code is baked directly into these classes. Typically, it would be
// separated out so that the AST us just a static data structure.
public interface Statement {

    /**
     * Statements implement this to actually perform whatever behavior the
     * statement causes. "print" statements will display text here, "goto"
     * statements will change the current statement, etc.
     */
    public int execute(MachineState ms);

}
