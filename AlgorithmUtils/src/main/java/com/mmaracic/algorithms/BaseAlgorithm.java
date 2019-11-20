/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mmaracic.algorithms;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author FEANOR-ROG
 */
public abstract class BaseAlgorithm {

    public static void main(String[] args) throws IOException {
        try {
            List<File> inFiles = findResources(createFilter(".in"));
            List<File> outFiles = findResources(createFilter(".out"));
            List<File> classFiles = findResources(createFilter(".class"));
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            BaseAlgorithm algorithm = null;
            for (File c : classFiles) {
                String className = BaseAlgorithm.class.getPackageName() + "." + extractName(c);
                Class cl = Class.forName(className, true, classLoader);
                try {
                    if (BaseAlgorithm.class.isAssignableFrom(cl) && !BaseAlgorithm.class.equals(cl)) {
                        algorithm = (BaseAlgorithm) cl.getConstructor(new Class[]{}).newInstance(args);
                    }
                } catch (ClassCastException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (algorithm != null) {
                Map<String, File> outMap = outFiles.stream().collect(Collectors.toMap(f -> extractName(f), f -> f));
                List<String> correctTests = new ArrayList<>();
                List<String> incorrectTests = new ArrayList<>();
                for (File inFile : inFiles) {
                    File outFile = outMap.get(extractName(inFile));
                    if (outFile != null) {
                        try (FileReader fin = new FileReader(inFile);
                                FileReader fout = new FileReader(outFile)) {
                            Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.INFO, "Input file: "+inFile.getAbsolutePath()+" started processing");
                            Runtime runtime = Runtime.getRuntime();
                            long memBefore = runtime.freeMemory();
                            long before = System.currentTimeMillis();
                            boolean correct = algorithm.processTest(new Scanner(fin), new Scanner(fout));
                            if (correct){
                                correctTests.add(inFile.getName());
                            } else {
                                incorrectTests.add(inFile.getName());                                
                            }
                            long after = System.currentTimeMillis();
                            long memAfter = runtime.freeMemory();
                            Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.INFO, "Duration: "+(after-before)+" ms");
                            Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.INFO, "Used memory: "+(memAfter-memBefore)+" bytes");
                        } catch (Exception ex) {
                            incorrectTests.add(inFile.getName());
                            Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.SEVERE, "Input file: "+inFile.getAbsolutePath()+" processed with error: "+ex.getMessage());                                
                        } finally {
                            Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.SEVERE, "Input file: "+inFile.getAbsolutePath()+" processed");
                        }
                    } else {
                        incorrectTests.add(inFile.getName());
                        Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.SEVERE, "Input file: "+inFile.getAbsolutePath()+" doesnt have corresponding output");
                    }
                }
                Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.INFO, "Correct tests: "+correctTests.size());
                for(String correctTest: correctTests){
                    Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.INFO, correctTest);                    
                }
                Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.SEVERE, "Incorrect tests: "+incorrectTests.size());
                for(String incorrectTest: incorrectTests){
                    Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.SEVERE, incorrectTest);                    
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static FileFilter createFilter(String ending) {
        return (File f) -> {
            if (f.isDirectory()) {
                return true;
            } else {
                return f.getAbsolutePath().endsWith(ending);
            }
        };
    }

    private static String extractName(File f) {
        return f.getName().substring(0, f.getName().indexOf('.'));
    }

    private static List<File> findResources(FileFilter filter) throws IOException {
        List<File> files = new ArrayList<>();
        ClassLoader cl = BaseAlgorithm.class.getClassLoader();
        Enumeration<URL> resources = cl.getResources("");
        Iterator<URL> it = resources.asIterator();
        while (it.hasNext()) {
            URL url = it.next();
            try {
                File f = new File(url.toURI());
                files.addAll(findRecursively(f, filter));
            } catch (URISyntaxException ex) {
                Logger.getLogger(BaseAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return files;
    }

    private static List<File> findRecursively(File f, FileFilter filter) {
        return findRecursively(f, filter, new ArrayList<>());
    }

    private static List<File> findRecursively(File f, FileFilter filter, List<File> foundFiles) {
        if (f.isFile()) {
            foundFiles.add(f);
        } else {
            File[] files = f.listFiles(filter);
            for (File subf : files) {
                findRecursively(subf, filter, foundFiles);
            }
        }
        return foundFiles;
    }
    
    protected int readInteger(Scanner scanner){
        if (scanner.hasNextInt()){
            return scanner.nextInt();
        } else {
            throw new IllegalStateException("Integer not found.");
        }
    }
    
    protected List<Integer> readIntegerList(Scanner scanner, int noNumbers){
        List<Integer> numbers = new ArrayList<>();
        while(scanner.hasNextInt() && noNumbers>0){
            numbers.add(scanner.nextInt());
            noNumbers--;
        }
        if (noNumbers>0){
            throw new IllegalStateException("Not enough integers found. Missing "+noNumbers);
        }
        return numbers;
    }

    protected List<List<Integer>> readIntegerList(Scanner scanner, int noLists, int noListNumbers){
        List<List<Integer>> lists = new ArrayList<>();
        for(int i=0;i<noLists;i++){
            lists.add(readIntegerList(scanner, noListNumbers));
        }
        return lists;
    }

    protected abstract boolean processTest(Scanner inFile, Scanner outFile);
}
