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
 * Kusalić303
 * Dynamic programming
 */
public class Tunel extends BaseAlgorithm{
    
    enum LaneObject{
        CAR('x'),
        NONE('-');
        
        final private char c;

        private LaneObject(char c) {
            this.c = c;
        }
        
        static LaneObject fromChar(char c){
            for(LaneObject obj: LaneObject.values()){
                if (obj.c == c){
                    return obj;
                }
            }
            return null;
        }
    }

    @Override
    protected boolean processTest(Scanner inFile, Scanner outFile) {
        int noLines = readInteger(inFile);
        LaneObject[][] lanes = new LaneObject[noLines][5];
        inFile.nextLine();
        for(int i=0; i<noLines; i++){
            if (inFile.hasNextLine()){
                String line = inFile.nextLine();
                if(line.length()==5){
                    for(int j=0;j<line.length();j++){
                        lanes[i][j] = LaneObject.fromChar(line.charAt(j));
                    }
                } else {
                    Logger.getLogger(Tunel.class.getName()).log(Level.SEVERE, "Line size is not 5: "+line.length());
                }
            } else {
                Logger.getLogger(Tunel.class.getName()).log(Level.SEVERE, "Not enough lines "+i);
            }
        }
        int calcNoDeaths = countDeaths(lanes);
        Logger.getLogger(Tunel.class.getName()).log(Level.INFO, "Number of deaths: "+calcNoDeaths);
        int noDeaths = readInteger(outFile);
        if (calcNoDeaths != noDeaths){
            Logger.getLogger(Tunel.class.getName()).log(Level.SEVERE, "Number of deaths is not correct "+noDeaths);
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 
     * @param lanes Each row is state of lanes in a particular second
     * We search for a path through the lanes with minimum number of cars. We can move left, right or stay
     * @return 
     */
    int countDeaths(LaneObject[][] lanes){
        int[] previousState = new int[5];
        for(int i=0; i<previousState.length; i++){
            previousState[i]=0;
        }
        int[] newState = new int[5];

        int minDeaths = Integer.MAX_VALUE;
        for(int i=0; i<lanes.length; i++){
            for(int j=0; j<5; j++){
                newState[j] = Math.min(((j>0)?previousState[j-1]:Integer.MAX_VALUE), Math.min(previousState[j], ((j<4)?previousState[j+1]:Integer.MAX_VALUE)))
                        +((lanes[i][j] == LaneObject.CAR)?1:0);
            }
            minDeaths  = Integer.MAX_VALUE;
            for(int j=0; j<5; j++){
                minDeaths = Math.min(minDeaths, newState[j]);
                previousState[j] = newState[j];
            }
        }
        return minDeaths;
    }
}
