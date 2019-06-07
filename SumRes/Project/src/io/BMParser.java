package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.jgrapht.Graph;
import org.jgrapht.io.ExportException;

import algDecDia.*;
import node.*;
public class BMParser {
	
	public static int BMParser(String filePath, ADD algDD) throws IOException {
		BufferedReader cin = new BufferedReader (new FileReader(filePath));
//		ADD algDD = new ADD();
		int nVars;
		int nCons;
		int zero = algDD.getTNode(0.0);
		int one = algDD.getTNode(1.0);
		
		int objective = 0;
		ArrayList<Integer> alCons = new ArrayList<Integer>();
		boolean firstLine = true;
		boolean moreLine = true;
		do {
			String line = cin.readLine();
			if(line == null) {
				moreLine = false;
			}else {
				System.out.println("Aaaaaa");
				if(line.contains("*") && firstLine) {
					String[] split = line.split(" ");
					nVars = Integer.parseInt(split[2]);
					nCons = Integer.parseInt(split[4]);
					//id of x1 = 2;
					for(int i = 1; i <= nVars; i++) {
						algDD.getINode("x"+i, zero, one);
					}
					firstLine = false;
				}else if (line.contains("*")) {
					
				}else if (line.contains("min") || line.contains("max")) {
					String[] split = line.split(" ");
					//ArrayList<String> op;
					ArrayList<Double> coef = new ArrayList<Double>();
					ArrayList<Integer> varId = new ArrayList<Integer>();
					ArrayList<Integer> termId = new ArrayList<Integer>();
					for(String s : split) {
						if (s.contains("+")|| s.contains("-")) {
							coef.add(Double.parseDouble(s));
						}else if (s.contains("x") && !s.contains("m")) {
							varId.add(Integer.parseInt(s.substring(1))+1);
						}
					}
					for(int i = 0; i < coef.size(); i++) {
						termId.add(algDD.apply(algDD.getTNode(coef.get(i)),varId.get(i),"Mult"));
					}
					objective = termId.get(0);
					for (int i = 1; i< termId.size(); i++) {
						objective = algDD.apply(objective, termId.get(i), "Plus");
					}
				}else {
					System.out.println(line);
					String[] split = line.split(" ");
					ArrayList<Double> coef = new ArrayList<Double>();
					ArrayList<Integer> varId = new ArrayList<Integer>();
					ArrayList<Integer> termId = new ArrayList<Integer>();
					String op = null;
					double RHS = 0;
					for(String s : split) {
						if ((s.contains("+")|| s.contains("-") || split[0] == s) && !(split[split.length - 2] == s)) {
							coef.add(Double.parseDouble(s));
						}else if (s.contains("x") && !s.equals("max")) {
							varId.add(Integer.parseInt(s.substring(1))+1);
						}else if (s.equals(";")) {
							
						}else if (s.contains("=") || s.contains("<") || s.contains(">")) {
							op = s;
						}else if (split[split.length - 2] == s){
							RHS = Double.parseDouble(s);
						}
					}
					System.out.println(coef.size());
					System.out.println(varId.size());
					for(int i = 0; i < coef.size(); i++) {
						termId.add(algDD.apply(algDD.getTNode(coef.get(i)),varId.get(i),"Mult"));
					}
					int equation = termId.get(0);
					for (int i = 1; i< termId.size(); i++) {
						equation = algDD.apply(equation, termId.get(i), "Plus");
					}
					int cons = algDD.reduceThreshold(equation, op, RHS);
					alCons.add(cons);
				}
			}
		} while (moreLine);
		
		cin.close();
		
		int finalResult = objective;
		
		for(int i : alCons) {
			finalResult = algDD.apply(finalResult, i, "Plus"); 
		}
		return finalResult;
	}
//	public static void main(String[] args) throws IOException, ExportException {
//		ADD algDD = new ADD();
//		int result = BMParser.BMParser("files/testcase1.txt", algDD);
//		String targetDirectory = "testresults/graph/";
//		new File(targetDirectory).mkdirs();
//		Graph g = algDD.getGraph(algDD.reduce(result));
//		algDD.toFile(g, targetDirectory, "Trial1.dot");
//	}
}
