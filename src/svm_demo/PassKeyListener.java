package svm_demo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PassKeyListener implements KeyListener,ActionListener{
	private JTextField inputField;
	private long clock;
	public boolean isClean;
	public double[] sampleTimeStampArray;
	public String password;
	private ArrayList<SampleDataElement> sampleList;
	public PassKeyListener(JTextField input,String passwd)
	{
		inputField = input;
		password = passwd;
		sampleList = new ArrayList<SampleDataElement>();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(inputField.getText().length() == 0) {
			//outputArea.append("Starting time measurement \n");
			sampleTimeStampArray = new double[2*password.length()];
			sampleList.clear();
			clock = System.nanoTime();
		}
		
		long timeStamp = getRelativeTimeInNano(System.nanoTime());
		//outputArea.append("KeyPressedEvent for " + e.getKeyChar() + " on " + timeStamp + "\n");
		char ch = e.getKeyChar();
		//String outputChar= "";
		if(Character.isAlphabetic(ch) || Character.isDigit(ch)) {
			//outputChar = "" + ch;
			sampleList.add(new SampleDataElement(true,ch,timeStamp));
		}else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			//outputChar = "SHIFT";
		}else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			//outputChar = "BACKSPACE";
			isClean = false;
		}
		//keyList.add("Pressed Code :\t" +e.getKeyCode() +"\tPressed Key :\t" + outputChar + "\tTimestamp :\t" +  timeStamp);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		//outputArea.append("KeyReleasedEvent for " + e.getKeyCode() + " on " + getRelativeTimeInNano(System.nanoTime()) + "\n");
		long timeStamp = getRelativeTimeInNano(System.nanoTime());
		//outputArea.append("KeyReleasedEvent for " + e.getKeyChar() + " on " + timeStamp + "\n");
		char ch = e.getKeyChar();
		String outputChar= "";
		if(Character.isAlphabetic(ch) || Character.isDigit(ch)) {
			//outputChar = "" + ch;
			sampleList.add(new SampleDataElement(false,ch,timeStamp));
		}else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			//outputChar = "SHIFT";
		}else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			//outputChar = "BACKSPACE";
		}
		//keyList.add("Released Code :\t" +e.getKeyCode() +"\tReleased Key :\t" + outputChar + "\tTimestamp :\t" +  timeStamp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void resetClock() {
		clock = 0;
	}
	
	private long getRelativeTimeInNano(long currentNanoTime) {
		return currentNanoTime - clock;
	}
	
	public double[] getSampleTimeStamps() {
		copyToArrayRecursive(0, 0);
		return sampleTimeStampArray;
	}
	
	private void copyToArrayRecursive(int startPos,int posToFillIn) {
		if(startPos == password.length()) {
			return;
		} else {
			char c = password.charAt(startPos);
			boolean pressRecorded = false;
			for(int i = 0; i<sampleList.size(); i++) {
				if(sampleList.get(i).character==c) {
					if(sampleList.get(i).pressed) {
						sampleTimeStampArray[posToFillIn] = sampleList.get(i).timestamp;
						sampleList.remove(i);
						i--;
						posToFillIn++;
					}else {
						sampleTimeStampArray[posToFillIn] = sampleList.get(i).timestamp;
						sampleList.remove(i);
						posToFillIn++;
						copyToArrayRecursive(startPos+1, posToFillIn);
						return;
					}
				}
			}
		}
	}
	
}
