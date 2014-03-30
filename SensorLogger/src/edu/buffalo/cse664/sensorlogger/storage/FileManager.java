package edu.buffalo.cse664.sensorlogger.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.telephony.TelephonyManager;

public class FileManager {
	
	/* Directory Name */
	public static final String DIRECTORY = "Events";
	
	/* Filenames */
	public static final String[] FILENAME = {"meta","TouchEvents","AccelerometerEvents","GyroscopeEvents"};
	
	/* Identifiers */
	public static final int META = 0;
	public static final int TOUCH = 1;
	public static final int ACCEL = 2;
	public static final int GYRO = 3;
	

	private Context context;
	private BufferedWriter[] mWriter = new BufferedWriter[4]; 
	private String IMEI;
	
	
	public FileManager(Context context){
		this.context = context;
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonyManager.getDeviceId();
		for(int i = 0; i < 4; i++){
			openFile(i);
		}
	}
	
	private void createFile(int fileId){
		if(fileId == META){
			
		}
		else{
			
		}
	}
	
	private void openFile(int fileId){
		File file = new File(context.getFilesDir(), DIRECTORY+ File.separator +FILENAME[fileId]);
		mWriter[fileId] = null;
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			mWriter[fileId] = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!file.exists()) createFile(fileId);
	}
	
	
	public void write(int fileId, String line){
		
	}
	
	
	

}
