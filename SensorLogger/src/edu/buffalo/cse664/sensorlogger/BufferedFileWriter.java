package edu.buffalo.cse664.sensorlogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;

public class BufferedFileWriter {

	public static final String directory = "Events";
	
	private File mFile;
	private BufferedWriter mWriter;
	
	public BufferedFileWriter(Context context, String filename, String[] columns){
		mFile = new File(context.getFilesDir(), directory + File.separator + filename);
		
		mWriter = null;
		try {
			FileWriter fw = new FileWriter(mFile.getAbsoluteFile());
			mWriter = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(!mFile.exists()) write(arrayToString(columns));
	}
	
	public synchronized boolean write(String s){
		if(mWriter == null) return false;
		try {
			mWriter.write(s);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public synchronized void close(){
		if(mWriter == null) return;
		try {
			mWriter.flush();
			mWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mWriter = null;
	}
	
	public static String arrayToString(String[] arr){
		StringBuilder result = new StringBuilder();
		for(String string : arr) {
			result.append(string);
			result.append(",");
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1): "";
	}
	
}
