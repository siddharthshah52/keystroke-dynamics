package svm_demo;

import java.util.*;

public class RatioModel {
	public ArrayList<double[]> data;
	public int featureCount;
	public double[] ratioArray;
	public double tolerancePercentage;
	
	public RatioModel(ArrayList<double[]> d,double tol) {
		data = d;
		tolerancePercentage = tol;
		featureCount = data.get(0).length;
		
		ratioArray = new double[featureCount/2 - 1];
		
		for(int i = 0; i<ratioArray.length; i++) {
			ratioArray[i] = 0;
		}
		
		for(double[] sample : data) {
			int k = 0;
			for (int j = 0; k<ratioArray.length; j+=2) {
				if(sample[j+2] != 0) {
					ratioArray[k++] += sample[j]/sample[j+2];
				}else {
					k++;
				}
			}
		}
		
		for(int i = 0; i<ratioArray.length; i++) {
			ratioArray[i] /= data.size();
		}
	}
	
	public boolean test(double[] testArray) {
		int withinLimits = 0;
		double[] testRatioArray = new double[featureCount/2 - 1];
		int k = 0;
		for (int j = 0; k<testRatioArray.length; j+=2) {
			if(testArray[j+2] != 0) {
				testRatioArray[k++] = testArray[j]/testArray[j+2];				
			}else {
				k++;
			}
		}
		
		for(int i = 0; i<testRatioArray.length; i++) {
			double error = (ratioArray[i] - testRatioArray[i]) / ratioArray[i] * 100;
			System.out.print("Feature " + i + " & " + (i+1) + " : " + error);
			//if ((error<=0 && Math.abs(error)<=leftTolerancePercentage) || (error>0 && error<=rightTolerancePercentage)) {
			if(Math.abs(error)<=tolerancePercentage) {
				withinLimits++;
				System.out.println(": Accept");
			}else {
				System.out.println(": Reject");
			}
			
		}
		if(withinLimits>=featureCount/4) {
			return true;
		}
		return false;
	}
	
}
