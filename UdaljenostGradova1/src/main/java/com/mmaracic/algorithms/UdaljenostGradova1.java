/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmaracic.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FEANOR-ROG
 * Kusalić240
 * https://www.hackerrank.com/challenges/dijkstrashortreach/problem
 * Samples and code Modified to fit hackerrank format
 * Dijkstra basic implementation
 */
public class UdaljenostGradova1 extends BaseAlgorithm{

    @Override
    protected boolean processTest(Scanner inFile, Scanner outFile) {
        int noCities = readInteger(inFile);
        int noRoads = readInteger(inFile);
        int initCity = readInteger(inFile);
        int cityIndexCorrection = readInteger(inFile);
        int[][] roads = readIntegerArray(inFile, noRoads, 3);
        for(int[] road: roads){
            road[0]+=cityIndexCorrection;
            road[1]+=cityIndexCorrection;
        }
        int[] paths = calculateShortestPath(noCities, roads, initCity);
        int[] correctPaths = readIntegerArray(outFile, noCities-1);
        if (correctPaths.length != paths.length){
            Logger.getLogger(UdaljenostGradova1.class.getName()).log(Level.SEVERE, "Length of distances not correct: "+paths.length);
            return false;
        } else {
            for(int i=0; i<paths.length; i++){
                if (paths[i]!=correctPaths[i]){
                    Logger.getLogger(UdaljenostGradova1.class.getName()).log(Level.SEVERE, "Distance at index "+i+" not correct "+paths[i]);
                    return false;
                }
            }
            return true;
        }
    }
    
    /**
     * Assumption - all cities are reachable
     * @param n Number of cities
     * @param edges List of roads; each road is represented by a triplet (source_city, destination_city, road_length)
     * @param s Starting city index
     * @return distance from the initial city to all other cities
     */
    int[] calculateShortestPath(int n, int[][] edges, int s){
        int[] distances = new int[n];
        List<Boolean> visited = new ArrayList();
        List<List<List<Integer>>> cities = new ArrayList<>();
        for(int i=0; i<n; i++){
            distances[i]=Integer.MAX_VALUE;
            visited.add(Boolean.FALSE);
            cities.add(new ArrayList<List<Integer>>());
        }
        for(int[] road: edges){
            int source = road[0];
            int destination = road[1];
            int distance = road[2];
            cities.get(source-1).add(Arrays.asList(destination-1, distance));
            cities.get(destination-1).add(Arrays.asList(source-1, distance));
        }
        distances[s-1]=0;
        visit(s-1, cities, visited, distances);
        int[] otherDistances = new int[n-1];
        for(int i=0, j=0; i<distances.length; i++){
            if (i != s-1){
                otherDistances[j] = (distances[i]<Integer.MAX_VALUE)?distances[i]:-1;
                j++;
            }
        }
        System.out.println(Arrays.toString(otherDistances));
        return otherDistances;
    }
    
    void visit(int cityIndex, List<List<List<Integer>>> cities, List<Boolean> visited, int[] distances){
        visited.set(cityIndex, Boolean.TRUE);
        int cityDistance = distances[cityIndex];
        List<List<Integer>> roads = cities.get(cityIndex);
        for (List<Integer> road: roads){
            int targetCity = road.get(0);
            int newTargetDistance = cityDistance + road.get(1);
            if (distances[targetCity]>newTargetDistance){
                distances[targetCity] = newTargetDistance;
            }
        }
        for (List<Integer> road: roads){
            int targetCity = road.get(0);
            if (!visited.get(targetCity)){
                visit(targetCity, cities, visited, distances);
            }            
        }
    }
    
}
