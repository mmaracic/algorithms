/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmaracic.algorithms;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FEANOR-ROG
 * Kusalić299
 * Dynamic programming
 * Can be solved in O(1) spatial complexity without the array
 */
public class Mjenjacnica extends BaseAlgorithm{

    @Override
    protected boolean processTest(Scanner inFile, Scanner outFile) {
        int noExchanges = readInteger(inFile);
        double[] exchanges = readDoubleArray(inFile, noExchanges);
        double calcMaxAmount = determineMaxValue(1000, exchanges);
        double maxAmount = readDouble(outFile);
        if (Math.abs(calcMaxAmount-maxAmount)>0.01){
            Logger.getLogger(Mjenjacnica.class.getName()).log(Level.SEVERE, "MaxValue is not correct "+calcMaxAmount);
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 
     * @param initValue Initial value in euros
     * @param exchanges Exchange rate as number of dollars that one gets for one euro
     * @return 
     */
    double determineMaxValue(double initValue, double[] exchanges){
        double[] D = new double[exchanges.length];
        double[] E = new double[exchanges.length];
        E[0] = initValue;
        D[0] = initValue*exchanges[0];
        for(int i=1; i<exchanges.length; i++){
            E[i] = Math.max(E[i-1], D[i-1]/exchanges[i]);
            D[i] = Math.max(D[i-1], E[i-1]*exchanges[i]);
        }
        return E[exchanges.length-1];
    }
}
