/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmaracic.algorithms;

import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FEANOR-ROG
 * Kusalić301
 * Dynamic programming
 * Find a path that gives max sum
 */
public class Piramida extends BaseAlgorithm{

    @Override
    protected boolean processTest(Scanner inFile, Scanner outFile) {
        int pyramidDepth = readInteger(inFile);
        int[][] pyramid = new int[pyramidDepth][pyramidDepth];
        for(int i=0; i<pyramid.length; i++){
            for(int j=0; j<=i; j++){
                pyramid[i][j]=readInteger(inFile);
            }
        }
        int[] calcPath = determineMaxPath(pyramid);
        Logger.getLogger(Piramida.class.getName()).log(Level.INFO, "Path: "+Arrays.toString(calcPath));
        int[] correctPath = readIntegerArray(outFile, pyramidDepth);
        if (calcPath.length != correctPath.length){
            Logger.getLogger(Piramida.class.getName()).log(Level.SEVERE, "Length of paths not correct: "+calcPath.length);
            return false;
        } else {
            for(int i=0; i<correctPath.length; i++){
                if (correctPath[i]!=calcPath[i]){
                    Logger.getLogger(Piramida.class.getName()).log(Level.SEVERE, "Distance at index "+i+" not correct "+calcPath[i]);
                    return false;
                }
            }
            return true;
        }
    }
    
    /**
     * 
     * @param pyramid Pyramid structure (upper matrix empty throw which we move down or down right)
     * maxValue = max(max((up+current)|(upper-left+current)))
     * @return Maximum value path
     */
    int[] determineMaxPath(int[][] pyramid){
        int[] maxPath = new int[pyramid.length];
        int[][][] calculation = new int[pyramid.length][pyramid.length][2];
        calculation[0][0][0]=pyramid[0][0];
        int maxValue = pyramid[0][0];
        int maxColumn = 0;
        for(int i=1; i<pyramid.length; i++){
            for(int j=0; j<=i; j++){
                int current = pyramid[i][j];
                int upper = current + ((i>j)?calculation[i-1][j][0]:0);
                int upperLeft = current + ((j>0)?calculation[i-1][j-1][0]:0);
                if (upper>upperLeft){
                    calculation[i][j][0]=upper;
                    calculation[i][j][1]=0;
                } else {
                    calculation[i][j][0]=upperLeft;
                    calculation[i][j][1]=1;                    
                }
                if (calculation[i][j][0]>maxValue){
                    maxValue = calculation[i][j][0];
                    maxColumn = j;
                }
            }
        }
        Logger.getLogger(Piramida.class.getName()).log(Level.INFO, "Calculation: "+Arrays.deepToString(calculation));
        for(int i=pyramid.length-1; i>=0; i--){
            maxPath[i] = pyramid[i][maxColumn];
            int direction = calculation[i][maxColumn][1];
            if (direction == 1){
                maxColumn -= 1; 
            }
        }
        return maxPath;
    }
}
