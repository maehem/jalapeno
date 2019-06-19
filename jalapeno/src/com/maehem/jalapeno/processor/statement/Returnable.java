/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maehem.jalapeno.processor.statement;

/**
 *
 * @author mark
 */
public interface Returnable {
    public void setEndStatement( EndStatement es );
    
    public EndStatement getEndStatement();
}
