package svm_demo;

import libsvm.*;
import java.io.*;
import java.util.*;

public class Main {
	static BufferedReader br;
	static int trainSamples,testSamples;
	static int features;
	
	public static void temp() {
/*		try {
			
			svm_node[][] testSet = new svm_node[testSamples][features];
			sampleIndex = 0;
			featureIndex = 0;
			for(ArrayList<Double> sample : data) {
				featureIndex = 0;
				for(double value : sample) {
					testSet[sampleIndex][featureIndex] = new svm_node();
					testSet[sampleIndex][featureIndex].index = featureIndex+1;
					testSet[sampleIndex][featureIndex].value = value;
					featureIndex++;
				}
				sampleIndex++;
			}
			for(int i = 0; i<testSamples; i++) {
					double d = svm.svm_predict(model, testSet[i]);

					if((int)d > 0) {System.out.println("Auth");}
					else {System.out.println("Not Auth " + d);}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}*/
	}
	
	public static void main(String[] args) {
		Window w = new Window();
		w.start();
	}

}
