package edu.buffalo.cse664.sensorlogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class FileManager {
	
	/* Debugging Tag */
	public static final String TAG = "FileWriter";
	
	/* Directory Name */
	public static final String DIRECTORY = "SensorLogger/Events";
	
	/* Filenames */
	public static final String[] FILENAME = {"metadata",
		"TouchEvents", "AccelerometerEvents", "GyroscopeEvents"};
	
	/* Identifiers */
	public static final int METADATA = 0;
	public static final int TOUCH = 1;
	public static final int ACCELEROMETER = 2;
	public static final int GYROROSCOPE = 3;
	
	
	private BufferedWriter mWriter = null;
	private int fid;
	private File dir;
	private File file;
	
	public FileManager(Context context, int fileID){
		fid = fileID;
		dir = new File(context.getFilesDir() + File.separator + DIRECTORY);
		dir.mkdirs();
		file = new File(dir, FILENAME[fid]);
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			mWriter = new BufferedWriter(fw);
			Log.d(TAG + "(" + fid + ")", "Writer opened.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write(final String line){
		if(mWriter==null)return;
		synchronized(mWriter){
			try {
				mWriter.write(line + '\n');
				Log.d(TAG + "(" + fid + ")", "Write.");
			} catch (IOException e) {
				Log.e(TAG + "(" + fid + ")", "Failed to write to file.");
			}
		}
		return;
	}
	
	public synchronized void close(){
		try {
			if(mWriter==null)return;
			synchronized(mWriter){
				mWriter.flush();
				mWriter.close();
				Log.d(TAG + "(" + fid + ")", "Writer closed");
			}
		} catch (IOException e) {
			Log.e(TAG + "(" + fid + ")", "Failed to close writer.");
		}
	}
	
}
