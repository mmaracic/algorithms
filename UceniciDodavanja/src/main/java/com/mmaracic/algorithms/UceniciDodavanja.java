/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmaracic.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FEANOR-ROG
 * Kusalić str 234
 * Calculates connected components of the graph using flood fill bfs
 * If the condition was before function call excess short calls could be avoided
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
            Logger.getLogger(UceniciDodavanja.class.getName()).log(Level.WARNING, "Needed: "+correctComponents+" got: "+components);
            return false;
        }
    }
    
    int calculateConnectedComponents(int noStudents, List<List<Integer>> links){
        boolean[] visited = new boolean[noStudents];
        for(int i=0; i<noStudents; i++){
            visited[i]=false;
        }
        List<List<Integer>> nodes = new ArrayList<>();
        for(int i=0; i<noStudents; i++){
            nodes.add(new ArrayList<>());
        }
        for (List<Integer> link: links){
            Integer node1 = link.get(0)-1;
            Integer node2 = link.get(1)-1;
            nodes.get(node1).add(node2);
            nodes.get(node2).add(node1);
        }
        int noComponents = 0;
        for(int i=0; i<noStudents; i++){
            int newComponent = visitNode(i, visited, nodes);
            noComponents+=newComponent;
        }
        return noComponents;
    }
    
    int visitNode(int node, boolean[] visited, List<List<Integer>> nodes){
        if (!visited[node]){
            visited[node] = true;
            List<Integer> neighbours = nodes.get(node);
            for(Integer neighbour: neighbours){
                visitNode(neighbour, visited, nodes);
            }
            return 1;
        } else {
            return 0;
        }       
    }
}
