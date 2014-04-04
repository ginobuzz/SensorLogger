package edu.buffalo.cse664.sensorlogger.storage;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class FileUtils {
	
	public static final String TAG = "FileUtils";
	
	
	public static byte[] fileToByteArray(File file){
		int length = (int)file.length();
		Log.d(TAG, "Size = " + length);
		byte[] fileData = new byte[(int) file.length()];
		DataInputStream dis;
		try {
			dis = new DataInputStream(new FileInputStream(file));
			dis.readFully(fileData);
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
			//TODO: LOG
		} catch (IOException e) {
			//TODO: LOG
			e.printStackTrace();
			return null;
		}
		return fileData;
	}
	
	public static File getExternalDirectory(Context context){
		File dir = new File(context.getExternalCacheDir(), FileConstants.DIRECTORY);
		dir.mkdirs();
		return dir;
	}
	
	
	

}
