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
 * Kusalić317
 * Dynamic programming
 */
public class Stepenice extends BaseAlgorithm{

    @Override
    protected boolean processTest(Scanner inFile, Scanner outFile) {
        int noSteps = readInteger(inFile);
        int calcCrossings = determineNoCrossings(noSteps);
        int crossings = readInteger(outFile);
        if (calcCrossings!=crossings){
            Logger.getLogger(Stepenice.class.getName()).log(Level.SEVERE, "Number of crossings is not correct "+calcCrossings);
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Return different number of ascensions if one can combine stepping one or two steps
     * @param noSteps Number of stairs
     * @return Number of ascensions
     * N[i]=N[i-1]+N[i-2]; N[1]=1, N[2]=2
     */
    int determineNoCrossings(int noSteps){
        int[] N = new int[2];
        N[0]=1;//N[1]
        N[1]=2;//N[2]
        for(int i=3;i<=noSteps; i++){
            int newCrossings = N[0] + N[1];
            N[0]=N[1];
            N[1]=newCrossings;
        }
        return N[1];
    }
}
