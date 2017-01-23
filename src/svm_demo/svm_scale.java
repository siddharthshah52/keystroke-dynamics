package svm_demo;

import java.io.*;
import java.util.ArrayList;

class svm_scale
{
	private String line = null;
	private double lower = -1.0;
	private double upper = 1.0;
	private double y_lower;
	private double y_upper;
	private boolean y_scaling = false;
	private double[] feature_max;
	private double[] feature_min;
	private double[] ranges;
	private double y_max = -Double.MAX_VALUE;
	private double y_min = Double.MAX_VALUE;
	private long num_nonzeros = 0;
	private long new_num_nonzeros = 0;
	public ArrayList<double[]> inputList;
	public double[][] input;
	public int inputSize,featureSize;

	private static void exit_with_help()
	{
		System.out.print(
		 "Usage: svm-scale [options] data_filename\n"
		+"options:\n"
		+"-l lower : x scaling lower limit (default -1)\n"
		+"-u upper : x scaling upper limit (default +1)\n"
		+"-y y_lower y_upper : y scaling limits (default: no y scaling)\n"
		+"-s save_filename : save scaling parameters to save_filename\n"
		+"-r restore_filename : restore scaling parameters from restore_filename\n"
		);
		System.exit(1);
	}

	private double output(int index, double value)
	{
		if(value == feature_min[index])
			value = lower;
		else if(value == feature_max[index])
			value = upper;
		else
			value = lower + (upper-lower) * 
				(value-feature_min[index])/
				(feature_max[index]-feature_min[index]);

		return value;
	}
	
	private double output_new(int index, double value) {
		value = (value - feature_min[index]) / ranges[index];
		return value;
	}

	private String readline(BufferedReader fp) throws IOException
	{
		line = fp.readLine();
		return line;
	}
	
	public void prepare(ArrayList<double[]> inpList, int featSize,double low, double upp) 
	{
		inputList = inpList;
		featureSize = featSize;
		lower = low;
		upper = upp;
		y_scaling = false;

		if(!(upper > lower))
		{
			System.err.println("inconsistent lower/upper specification");
			System.exit(1);
		}

		/* assumption: min index of attributes is 1 */
		/* pass 1: find out max index of attributes */

		try {
			feature_max = new double[featureSize];
			feature_min = new double[featureSize];
			ranges = new double[featureSize];
		} catch(OutOfMemoryError e) {
			System.err.println("can't allocate enough memory");
			System.exit(1);
		}

		for(int i=0;i<featureSize;i++)
		{
			feature_max[i] = -Double.MAX_VALUE;
			feature_min[i] = Double.MAX_VALUE;
		}

		/* pass 2: find out min/max value */
		for(double[] sample : inputList) {
			for(int col = 0; col<featureSize; col++) {
				feature_max[col] = Math.max(feature_max[col], sample[col]);
				feature_min[col] = Math.min(feature_min[col], sample[col]);
			}
		}
		
		for(int i=0; i<featureSize; i++) {
			ranges[i] = feature_max[i] - feature_min[i];
		}
		
	}

	public void scale(ArrayList<double[]> inpList)
	{
		inputList = inpList;
		/* pass 2.5: save/restore feature_min/feature_max */

		/* pass 3: scale */
		for(int row = 0; row<inputList.size(); row++) {
			double[] sample = inputList.get(row);
			for(int col = 0; col<featureSize; col++) {
				sample[col] = output_new(col,sample[col]);
			}
			inputList.set(row, sample);
		}
		/*while(readline(fp) != null)
		{
			int next_index = 1;
			double target;
			double value;

			StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");
			target = Double.parseDouble(st.nextToken());
			output_target(target);
			while(st.hasMoreElements())
			{
				index = Integer.parseInt(st.nextToken());
				value = Double.parseDouble(st.nextToken());
				for (i = next_index; i<index; i++)
					output(i, 0);
				output(index, value);
				next_index = index + 1;
			}

			for(i=next_index;i<= max_index;i++)
				output(i, 0);
			System.out.print("\n");
		}
		if (new_num_nonzeros > num_nonzeros)
			System.err.print(
			 "WARNING: original #nonzeros " + num_nonzeros+"\n"
			+"         new      #nonzeros " + new_num_nonzeros+"\n"
			+"Use -l 0 if many original feature values are zeros\n");

		fp.close();
		*/
		
	}
}
