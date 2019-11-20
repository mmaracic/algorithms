/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmaracic.algorithms;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author FEANOR-ROG
 * Calculates connected components of the graph using flood fill bfs
 * 
 */
public class UceniciDodavanja extends BaseAlgorithm{

    @Override
    protected boolean processTest(Scanner inFile, Scanner outFile) {
        int noStudents = readInteger(inFile);
        int noLinks = readInteger(inFile);
        List<List<Integer>> links = readIntegerList(inFile, noLinks, 2);
        int components = calculateConnectedComponents(noStudents, links);
        int correctComponents = readInteger(outFile);
        if (components == correctComponents){
            return true;
        } else {
            return false;
        }
    }
    
    int calculateConnectedComponents(int noStudents, List<List<Integer>> links){
        return 0;
    }
}
