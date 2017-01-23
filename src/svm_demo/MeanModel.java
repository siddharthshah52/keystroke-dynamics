package svm_demo;
import java.util.*;

public class MeanModel {
	public ArrayList<double[]> data;
	public double[] mean;
	public int featureCount;
	public double leftTolerancePercentage,rightTolerancePercentage;
	public MeanModel(ArrayList<double[]> d,double lTol, double rTol) {
		data = d;
		featureCount = data.get(0).length;
		leftTolerancePercentage = lTol;
		rightTolerancePercentage = rTol;
		mean = new double[featureCount];
		
		for(int i = 0; i<mean.length; i++) {
			mean[i] = 0;
		}
		for(double[] dSample : data) {
			for(int i = 0; i<dSample.length; i++) {
				mean[i] += dSample[i];
			}
		}
		for(int i = 0; i<mean.length; i++) {
			mean[i] /= data.size();
		}
	}
	
	public boolean test(double[] testArray) {
		int withinLimits = 0;
		for(int i = 0; i<testArray.length; i++) {
			double error = (mean[i] - testArray[i]) / mean[i] * 100;
			System.out.print("Feature " + i +  " : " + error);
			if ((error<=0 && Math.abs(error)<=leftTolerancePercentage) || (error>0 && error<=rightTolerancePercentage)) {
			//if((Math.abs(mean[i] - testArray[i]) / mean[i]) * 100<=tolerancePercentage) {
				withinLimits++;
				System.out.println(": Accept");
			}else {
				System.out.println(": Reject");
			}
			
		}
		if(withinLimits>=featureCount/2) {
			return true;
		}
		return false;
	}
}
