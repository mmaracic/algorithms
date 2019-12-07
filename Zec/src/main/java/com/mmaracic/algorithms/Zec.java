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
 * Kusalić318
 * Dynamic programming
 */
public class Zec extends BaseAlgorithm{
    
    enum FieldObject{
        ROCK('x'),
        NONE('.');
        
        final private char c;

        private FieldObject(char c) {
            this.c = c;
        }
        
        static FieldObject fromChar(char c){
            for(FieldObject obj: FieldObject.values()){
                if (obj.c == c){
                    return obj;
                }
            }
            return null;
        }
    }

    @Override
    protected boolean processTest(Scanner inFile, Scanner outFile) {
        int noRows = readInteger(inFile);
        int noColumns = readInteger(inFile);
        FieldObject[][] lanes = new FieldObject[noRows][noColumns];
        inFile.nextLine();
        for(int i=0; i<noRows; i++){
            if (inFile.hasNextLine()){
                String line = inFile.nextLine();
                if(line.length()==noColumns){
                    for(int j=0;j<line.length();j++){
                        lanes[i][j] = FieldObject.fromChar(line.charAt(j));
                    }
                } else {
                    Logger.getLogger(Zec.class.getName()).log(Level.SEVERE, "Line size is not 5: "+line.length());
                }
            } else {
                Logger.getLogger(Zec.class.getName()).log(Level.SEVERE, "Not enough lines "+i);
            }
        }
        int calcNoDeaths = countDeaths(lanes);
        Logger.getLogger(Zec.class.getName()).log(Level.INFO, "Number of paths: "+calcNoDeaths);
        int noDeaths = readInteger(outFile);
        if (calcNoDeaths != noDeaths){
            Logger.getLogger(Zec.class.getName()).log(Level.SEVERE, "Number of paths is not correct "+calcNoDeaths);
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Return the number of paths%1000000 that a rabbit can take when walking through a field only down and right.
     * The rabbit cannot cross rocks
     * @param lanes Field array
     * @return Number of paths
     */
    int countDeaths(FieldObject[][] lanes){
        int[][] paths = new int[lanes.length][lanes[0].length];
        paths[0][0]=1;
        for(int i=0; i<lanes.length; i++){
            for(int j=0;j<lanes[0].length; j++){
                if (i==0 && j==0);
                else {
                    if (lanes[i][j].equals(FieldObject.ROCK)){
                        paths[i][j] = 0;
                    } else {
                        paths[i][j] = ((i==0)?0:paths[i-1][j]) + 
                            ((j==0)?0:paths[i][j-1]);
                    }
                }
            }
        }
        return paths[lanes.length-1][lanes[0].length-1]%1000000;
    }
}
