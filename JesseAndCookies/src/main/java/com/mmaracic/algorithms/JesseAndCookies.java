/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmaracic.algorithms;

import com.mmaracic.algorithms.BaseAlgorithm;
import java.util.List;
import java.util.Scanner;

/**
 * Hackerrank; Min Heap data structure
 * https://www.hackerrank.com/challenges/jesse-and-cookies/submissions
 *
 * @author mmaracic
 * 
 * test3 - 90 - correct 5
 * 13 47 74 12 89 74 18 38
 * 12 13 18 38 47 74 74 89 - 0
 * 18 38 38 47 74 74 89 - 1
 * 38 47 74 74 89 94 - 2
 * 74 74 89 94 132 - 3
 * 89 94 132 222 - 4
 * 132 222 277 - 5
 */
public class JesseAndCookies extends BaseAlgorithm {

    @Override
    protected boolean processTest(Scanner inFile, Scanner outFile) {
        int n = readInteger(inFile);
        int k = readInteger(inFile);
        List<Integer> a = readIntegerList(inFile, n);
        int calculatedR = cookies(k, a);
        int r = readInteger(outFile);
        if (r == calculatedR) {
            return true;
        } else {
            System.out.println("Output is " + calculatedR + " should be " + r);
            return false;
        }
    }

    public int cookies(int k, List<Integer> A) {
        if (A.size() < 1) {
            return -1;
        } else {
            createHeap(A);
            int count = 0;
            while (true) {
                System.out.println("Heap " + A);
                int minValue = A.get(0);
                if (minValue > k) {
                    break;
                } else if (minValue <= k && A.size() == 1) {
                    count = -1;
                    break;
                } else {
                    int firstMinValue = returnHead(A);
                    int secondMinValue = returnHead(A);
                    int newValue = firstMinValue + 2 * secondMinValue;
                    System.out.println("First " + firstMinValue + " second " + secondMinValue + " new " + newValue);
                    A.add(newValue);
                    bubbleChildUp(A, A.size() - 1);
                    count++;
                }
            }
            return count;
        }
    }

    public int returnHead(List<Integer> heap) {
        int head = heap.get(0);
        int last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            bubbleParentDown(heap, 0);
        }
        return head;
    }

    /**
     * Faster head construction with bubble down; Skiena pp 115
     * Loop needs to go backwards so it can also push leaf up
     */
    public void createHeap(List<Integer> heap) {
        if (heap.size() < 2) {
            return;
        } else {
            for (int i = heap.size()-1; i >=0 ; i--) {
                bubbleParentDown(heap, i);
            }
        }
    }

    public void bubbleParentDown(List<Integer> heap, int parentIndex) {

        int leftChildIndex = getLeftChildIndex(parentIndex);
        int rightChildIndex = getRightChildIndex(parentIndex);

        int parentValue = heap.get(parentIndex);
        Integer leftChildValue = (leftChildIndex < heap.size()) ? heap.get(leftChildIndex) : null;
        Integer rightChildValue = (rightChildIndex < heap.size()) ? heap.get(rightChildIndex) : null;

        //children can be equal; can pick any of the children in that case
        if (leftChildValue != null && leftChildValue < parentValue && (rightChildValue == null || leftChildValue <= rightChildValue)) {
            heap.set(parentIndex, leftChildValue);
            heap.set(leftChildIndex, parentValue);
            bubbleParentDown(heap, leftChildIndex);
        } else if (rightChildValue != null && rightChildValue < parentValue && (leftChildValue == null || rightChildValue <= leftChildValue)) {
            heap.set(parentIndex, rightChildValue);
            heap.set(rightChildIndex, parentValue);
            bubbleParentDown(heap, rightChildIndex);
        }
    }

    public void bubbleChildUp(List<Integer> heap, int leafIndex) {
        int leafValue = heap.get(leafIndex);

        int parentIndex = getParentIndex(leafIndex);
        if (parentIndex >= 0) {

            int parentValue = heap.get(parentIndex);

            if (parentValue > leafValue) {
                heap.set(leafIndex, parentValue);
                heap.set(parentIndex, leafValue);

                bubbleChildUp(heap, parentIndex);
            }
        }
    }

    public int getLeftChildIndex(int parentIndex) {
        return 2 * (parentIndex + 1) - 1;
    }

    public int getRightChildIndex(int parentIndex) {
        return 2 * (parentIndex + 1);
    }

    public int getParentIndex(int childIndex) {
        int modifiedIndex = (childIndex + 1);
        int parentIndex = modifiedIndex / 2;
        return parentIndex - 1;
    }
}
