/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmaracic.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FEANOR-ROG
 * Kusalić240
 * https://www.hackerrank.com/challenges/dijkstrashortreach/problem
 * Samples and code Modified to fit hackerrank format
 * Dijkstra basic implementation
 * Watch for multiple paths between two cities
 * Always look for the unprocessed node closest to the INITIAL node
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
        System.out.println(Arrays.toString(paths));
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
        Set<Integer> visited = new HashSet();
        List<Map<Integer, Integer>> cities = new ArrayList<>();
        Map<Integer, Integer> distances = new HashMap();
        //initial values
        for(int i=0; i<n; i++){
            if (i != s-1){
                distances.put(i, Integer.MAX_VALUE);
            } else {
                distances.put(i, 0);
            }
            visited.add(i);
            cities.add(new HashMap<>());
        }
        //processing edges
        for(int[] road: edges){
            int source = road[0];
            int destination = road[1];
            int distance = road[2];
            Map<Integer, Integer> sourceCity = cities.get(source-1);
            //taking care for multiple paths between two nodes, take only shortest
            if (!sourceCity.containsKey(destination-1) ||
                    (sourceCity.containsKey(destination-1) && sourceCity.get(destination-1)>distance)){
                sourceCity.put(destination-1, distance);
            }
            Map<Integer, Integer> destinationCity = cities.get(destination-1);
            if (!destinationCity.containsKey(source-1) ||
                    (destinationCity.containsKey(source-1) && destinationCity.get(source-1)>distance)){
                destinationCity.put(source-1, distance);
            }
        }
       
        //dijkstra processing
        visit(s-1, cities, visited, distances);
        
        //skipping initial node and setting unreachable value
        int[] otherDistances = new int[n-1];
        for(int i=0, j=0; i<n; i++){
            if (i != s-1){
                otherDistances[j] = (distances.get(i)<Integer.MAX_VALUE)?distances.get(i):-1;
                j++;
            }
        }
        return otherDistances;
    }
    
    void visit(int cityIndex, List<Map<Integer, Integer>> cities, Set<Integer> visited, Map<Integer, Integer> distances){
        visited.remove(cityIndex);
        int cityDistance = distances.get(cityIndex);
        Map<Integer, Integer> roads = cities.get(cityIndex);
        for (Entry<Integer, Integer> road: roads.entrySet()){
            int targetCity = road.getKey();
            int oldTargetDistance = distances.get(targetCity);
            int newTargetDistance = cityDistance + road.getValue();
            if (oldTargetDistance>newTargetDistance){
                distances.put(targetCity, newTargetDistance);
            }
        }
        int shortestDistance = Integer.MAX_VALUE;
        int nearestUnvisitedCity = -1;
        for (Integer unvisitedCity: visited){
            int unvisitedDistance = distances.get(unvisitedCity);
            if (unvisitedDistance<shortestDistance && visited.contains(unvisitedCity)){
                shortestDistance = unvisitedDistance;
                nearestUnvisitedCity = unvisitedCity;
            }            
        }
        if (nearestUnvisitedCity!=-1 && shortestDistance != Integer.MAX_VALUE){
            visit(nearestUnvisitedCity, cities, visited, distances);
        }
    }
    
}
