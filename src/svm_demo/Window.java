package svm_demo;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;

public class Window {
	public JFrame frame;
	public JPanel panel;
	public JTextField decidePass,inputPass,testPass;
	public JTextArea analysisOutput;
	public JScrollPane outputScrollPane;
	public JButton resetButton,runSvmButton,importSamplesButton,exportSamplesButton;
	public JLabel decidePassLabel,addSampleLabel,numberOfSamplesRecorded,testPassLabel,resultLabel;
	public JTable table;
	public ArrayList<double[]> timeStampList;
	public PassKeyListener passKeyListener,passKeyTestListener;
	public String password;
	public int samplesRecorded = 0;
	public SvmManager svmManager;
	public MeanModel meanModel;
	public RatioModel ratioModel;
	public svm_scale svmScaler;
	
	public void start()
	{
		timeStampList = new ArrayList<double[]>();
		
		frame = new JFrame("Analysis");
		frame.setSize(650, 250);
		frame.setResizable(false);
		
		panel = new JPanel();
		
		decidePassLabel = new JLabel("Enter the password to test:");
		decidePass = new JTextField();
		decidePass.setColumns(20);
		decidePass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(decidePass.getText().length()>0) {
					password = decidePass.getText();
					
					passKeyListener = new PassKeyListener(inputPass,password);
					passKeyListener.isClean = true;
					inputPass.addKeyListener(passKeyListener);
					
					decidePass.setEnabled(false);
					//setDecidePassButton.setEnabled(false);
				}
			}
			
		});
		
		addSampleLabel = new JLabel("Type password here for sample:");
		
		inputPass = new JTextField("Type here...");
		inputPass.setColumns(20);
		inputPass.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
		        inputPass.setText("");
		      }
			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		inputPass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputPass.getText().equals(password) && passKeyListener.isClean) {
					inputPass.setText("Type here...");
					//COMPUTING DIFFERENCES HERE!
					/*double[] tempArray = passKeyListener.getSampleTimeStamps();
					for(int i = tempArray.length-1; i>0; i--) {
						tempArray[i] = tempArray[i]-tempArray[i-1];
					}
					timeStampList.add(tempArray);*/
					timeStampList.add(passKeyListener.getSampleTimeStamps());
					samplesRecorded++;
					numberOfSamplesRecorded.setText(samplesRecorded + " samples recorded");
					log("Sample recorded successfully!");
					for ( double d : timeStampList.get(timeStampList.size()-1)) {
						System.out.print(d + ",");
					}
					System.out.println();
					if(samplesRecorded>=10) {
						runSvmButton.setEnabled(true);
					}
					inputPass.setText("");
				} else {
					inputPass.setText("");
					//JOptionPane.showMessageDialog(null, "OOPS! There was a spelling mistake while typing. Please try again.");\
					log("OOPS! There was a spelling mistake while typing. Please try again.");
					passKeyListener.isClean = true;
				}
			}
		});
		
		numberOfSamplesRecorded = new JLabel("No samples recorded.");
		
		resetButton = new JButton("Discard this sample and clear");
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				inputPass.setText("Type here...");
				//JOptionPane.showMessageDialog(null, "Sample discarded! Ready to accept fresh sample.");
				log("Sample discarded successfully!");
			}
		});
		
		testPass = new JTextField();
		testPass.setColumns(20);
		testPass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<double[]> testDataList = new ArrayList<double[]>();
				//COMPUTING DIFFERENCES HERE!
				/*double[] tempArray = passKeyTestListener.getSampleTimeStamps();
				for(int i = tempArray.length-1; i>0; i--) {
					tempArray[i] = tempArray[i]-tempArray[i-1];
				}
				testDataList.add(tempArray);*/
				testDataList.add(passKeyTestListener.getSampleTimeStamps());
				System.out.println("testdata:");
				for(double d : testDataList.get(0)) {
					System.out.print(d + ",");
				}
				System.out.println();
				
				log("Scaling data...");
				svmScaler.scale(testDataList);
				System.out.println("After scaling : ");
				for(double d : testDataList.get(0)) {
					System.out.print(d + ",");
				}
				System.out.println();
				log("Checking authenticity...");
				if (testPass.getText().equals(password) && passKeyTestListener.isClean) {
					//try {
					//PrintWriter pw = new PrintWriter(new FileWriter("data.txt"));
					//for(double nu = 0.1; nu<1; nu+=0.1) {
					//	for(double gamma = 0.01; gamma<1; gamma+=0.05) {
					//		svmManager.parameter.nu = nu;
					//		svmManager.parameter.gamma = gamma;
					//		svmManager.train();
					//		System.out.print("nu = " + nu + " ");
							try {
								boolean svmResult = svmManager.test(testDataList.get(0));
								log("SVM verdict : " + svmResult);
								boolean meanResult = meanModel.test(testDataList.get(0));
								log("MeanModel verdict : " + meanResult);
								boolean ratioResult = ratioModel.test(testDataList.get(0));
								log("RatioModel verdict : " + ratioResult);
								System.out.println("SVM result : " + svmResult + " Mean Result : " + meanResult + " Ratio Result " + ratioResult);
								if( svmResult || meanResult || ratioResult) {
									resultLabel.setText("Result : Authentic");
									log("Authentic");
									//pw.print(nu + "," + gamma + "Authentic\t");
								}else {
									resultLabel.setText("Result : Attack!");
									log("Attack");
									//pw.print(nu + "," + gamma + "Attack\t");
								}
							}catch(IllegalArgumentException ex) {
								ex.printStackTrace();
							}
					//	}
					//	pw.print("\n");
					//}
					//pw.flush();
					//pw.close();
					//}catch(Exception ex) {
					//	ex.printStackTrace();
					//}
				}else {
					resultLabel.setText("Result : Error!");
					log("Error!");
				}
				testPass.setText("");
				passKeyTestListener.isClean = true;
			}
		});
		
		runSvmButton = new JButton("Learn samples and test");
		runSvmButton.setEnabled(false);
		runSvmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				log("Scaling values...");
				svmScaler = new svm_scale();
				svmScaler.prepare(timeStampList,2*password.length(),-1, 1);
				svmScaler.scale(timeStampList);
				System.out.println("After scaling :");
				for(double[] dArray : timeStampList) {
					for(double d : dArray) {
						System.out.print(d + ",");
					}
					System.out.println();
				}
				
				log("Creating new SVM instance...");
				svmManager = new SvmManager(timeStampList);
				//svmManager.parameter.gamma = 1/(2*(double)password.length());
				System.out.println("Gamma = " + svmManager.parameter.gamma );
				//svmManager.parameter.gamma = 0.8;
				log("Training SVM...");
				svmManager.train();
				log("SVM Trained !");
				passKeyTestListener = new PassKeyListener(testPass,password);
				passKeyTestListener.isClean = true;
				testPass.addKeyListener(passKeyTestListener);
				
				meanModel = new MeanModel(timeStampList,100,80);
				ratioModel = new RatioModel(timeStampList,50);
			}
		});
		
		testPassLabel = new JLabel("Type password here to test : ");
		
		resultLabel = new JLabel("Result : ");
		
		analysisOutput = new JTextArea();
		analysisOutput.setSize(600,100);
		analysisOutput.setEditable(false);
		outputScrollPane = new JScrollPane(analysisOutput);
		outputScrollPane.setPreferredSize(new Dimension (600,100));
		
		importSamplesButton = new JButton("Import samples");
		importSamplesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser= new JFileChooser();
				int choice = chooser.showOpenDialog(chooser);
				if (choice != JFileChooser.APPROVE_OPTION) return;
				File chosenFile = chooser.getSelectedFile();
				log("Opening file...");
				timeStampList.clear();
				samplesRecorded = 0;
				try {
					BufferedReader br = new BufferedReader(new FileReader(chosenFile));
					String fileLine = "";
					fileLine = br.readLine();
					String[] timeStamps = fileLine.split(",");
					int features = timeStamps.length;
					double[] dArray = new double[features];
					while(fileLine!= null) {
						timeStamps = fileLine.split(",");
						if(timeStamps.length != features) {
							log("Error : features mismatch among samples. Terminating");
							return;
						}
						for(int i = 0; i<timeStamps.length; i++) {
							dArray[i] = Double.valueOf(timeStamps[i]);
						}
						timeStampList.add(dArray);
						samplesRecorded++;
						fileLine = br.readLine();
					}
					br.close();
					numberOfSamplesRecorded.setText(samplesRecorded + " samples recorded");
					password = chosenFile.getName();
					decidePass.setText(password);
					passKeyListener = new PassKeyListener(inputPass,password);
					passKeyListener.isClean = true;
					inputPass.addKeyListener(passKeyListener);
					decidePass.setEnabled(false);
					
					if(samplesRecorded>=10) {
						runSvmButton.setEnabled(true);
					}
					
				}catch(Exception ex) {
					log(ex.getMessage());
				}
				log("Samples imported successfully...");
			}
		});
		
		exportSamplesButton = new JButton("Export samples");
		exportSamplesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String fileName = JOptionPane.showInputDialog("Enter file name (It should be same as the password) :");
				try {
					PrintWriter pw = new PrintWriter(new FileWriter(fileName));
					for(double[] dArray : timeStampList) {
						for(int i = 0; i < dArray.length-1 ; i++) {
							pw.print((long) dArray[i]+ ",");
						}
						pw.println((long)dArray[dArray.length-1]);
					}
					pw.flush();
					pw.close();
					log("Samples stored in " + fileName);
				}catch(Exception ex) {
					log(ex.getMessage());
				}
			}
		});
		
		
		
		log("Waiting for password to test...");
		
		panel.add(decidePassLabel);
		panel.add(decidePass);
		//panel.add(setDecidePassButton);
		panel.add(addSampleLabel);
		panel.add(inputPass);
		panel.add(numberOfSamplesRecorded);
		//panel.add(clearButton);
		panel.add(resetButton);
		panel.add(runSvmButton);
		panel.add(testPassLabel);
		panel.add(testPass);
		//panel.add(testPassButton);
		panel.add(resultLabel);
		panel.add(outputScrollPane);
		panel.add(importSamplesButton);
		panel.add(exportSamplesButton);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void log(String logLine) {
		analysisOutput.append(logLine + "\n");
		analysisOutput.setCaretPosition(analysisOutput.getDocument().getLength());
	}
}
