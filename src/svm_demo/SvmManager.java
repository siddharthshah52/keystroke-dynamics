package svm_demo;

import libsvm.*;

import java.util.*;

public class SvmManager {
	public int trainSamples, testSamples, features;
	public svm_parameter parameter;
	public svm_problem problem;
	public svm_model model;
	public ArrayList<double[]> data;
	
	public SvmManager(ArrayList<double[]> data) {
		this.data = data;
		trainSamples = data.size();
		features = data.get(0).length;			//number of features is number of features of first sample
		parameter = new svm_parameter();
		parameter.svm_type = svm_parameter.ONE_CLASS;	//-s 2
		parameter.kernel_type = svm_parameter.RBF;		//-t 2
		parameter.degree = 3;
		parameter.gamma = 1/((double)features);
		parameter.coef0 = 0;
		parameter.nu = 0.03;
		parameter.cache_size = 40;
		parameter.C = 1;
		parameter.eps = 1e-3;
		parameter.p = 0.1;
		parameter.shrinking = 1;
		parameter.probability = 0;
		parameter.nr_weight = 0;
		parameter.weight_label = new int[0];
		parameter.weight = new double[0];
		
		problem = new svm_problem();
		problem.l = trainSamples;
		problem.y = new double[problem.l];
		problem.x = new svm_node[problem.l][features];
		int sampleIndex = 0,featureIndex = 0;
		for(double[] sample : data) {
			featureIndex = 0;
			for(double value : sample) {
				problem.x[sampleIndex][featureIndex] = new svm_node();
				problem.x[sampleIndex][featureIndex].index = featureIndex+1;
				problem.x[sampleIndex][featureIndex].value = value;
				featureIndex++;
			}
			problem.y[sampleIndex] = 1;
			sampleIndex++;
		}
	}
	
	public void train() {
		model = svm.svm_train(problem,parameter);
	}
	
	public boolean test(double[] testArray) throws IllegalArgumentException{
		if(testArray.length != features) {
			throw new IllegalArgumentException("test features does not match train features");
		}
		svm_node[] node_array = new svm_node[features];
		int i = 0; 
		for(double d : testArray) {
			node_array[i] = new svm_node();
			node_array[i].index = i+1;
			node_array[i].value = d;
			i++;
		}
		double d = svm.svm_predict(model,node_array);
		if(d>0) {
			return true;
		}
		return false;
	}
	
	
}
