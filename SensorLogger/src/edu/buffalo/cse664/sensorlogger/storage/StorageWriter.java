package edu.buffalo.cse664.sensorlogger.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class StorageWriter {

	public static final String TAG = "Writer";
	private BufferedWriter mWriter = null;
	private Boolean mWriteLock;
	private File file;
	
	public StorageWriter(Context context, String name){
		File dir = StorageUtils.getExternalDirectory(context);
		file = new File(dir, name);
		mWriteLock = false;
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			mWriter = new BufferedWriter(fw);
			mWriteLock = true;
			Log.d(TAG+": "+file.getName(), "OPENED FILE");
		} catch (IOException e) {
			Log.e(TAG+": "+name, "Failed to open output stream");
		}
	}
	
	public void write(String line){
		synchronized(mWriteLock){
			if(!mWriteLock) return;
			try {mWriter.write(line); mWriter.newLine();} 
			catch (IOException e) {Log.e(TAG, "Failed to write to: "+file.getAbsolutePath());}
		}
	}
	
	public synchronized void close(){
		synchronized(mWriteLock){
			mWriteLock = false;
			try {
				if(mWriter == null) return;
				mWriter.flush();
				mWriter.close();
				Log.d(TAG+": "+file.getName(), "CLOSED FILE");
				mWriter = null;
			}
			catch (IOException e) {Log.e(TAG, "Failed to close: " +file.getAbsolutePath());}
		}
	}
	
}
