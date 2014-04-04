package edu.buffalo.cse664.sensorlogger.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.util.Log;
import edu.buffalo.cse664.sensorlogger.FileManager;

public class Zipper {

	private static final String TAG = "Zipper";
	
	private static final String[] FILE = FileManager.FILENAME;
	private byte[] buffer = new byte[FileConstants.BUFFER];
	private File dir;
	private ZipOutputStream zipper;
	
	
	public Zipper(Context context){
		dir = FileUtils.getExternalDirectory(context);
	}
	
	public File getZip(){
		File zip = newfile();
	
		try {
			FileOutputStream dst = new FileOutputStream(zip.getAbsoluteFile());
			zipper = new ZipOutputStream(new BufferedOutputStream(dst)); 
		} catch (FileNotFoundException e1) {
			Log.e(TAG, "Failed to open output stream.");
			return null;
		} 
		
		if(!zipFile(FILE[0])){Log.e(TAG, "Failed to zip metadata");}
		if(!zipFile(FILE[1])){Log.e(TAG, "Failed to zip touch");}
		if(!zipFile(FILE[2])){Log.e(TAG, "Failed to zip accel");}
		if(!zipFile(FILE[3])){Log.e(TAG, "Failed to zip gyro");}
		
		try {
			zipper.close();
		} catch (IOException e) {
			Log.e(TAG, "Failed to close zipper.");
			return null;
		}

		Log.d(TAG, "Zip size = " + String.valueOf(zip.length()));
		if(zip.exists()) Log.d(TAG, "File exists.");
		return zip;
	}
	
	private File newfile(){
		File file = new File(dir, FileConstants.ZIPFILE);
		if(file.exists()){ 
			Log.d(TAG, "Existing zipfile found");
			if(file.delete()) Log.d(TAG, "Old file deleted.");
		}
		return file;
	}
	
	private File getEventFile(String filename){
		File file = new File(dir, filename);
		return file;
	}

	
	private boolean zipFile(String filename){
		String tag = TAG + ": " + filename;
		File file = getEventFile(filename);
		FileInputStream fis = null;
		BufferedInputStream origin = null;
		int count;
		
		
		try {
			fis = new FileInputStream(file);
			origin = new BufferedInputStream(fis, FileConstants.BUFFER);
		} catch (FileNotFoundException e) {
			Log.e(TAG + ": " + file.getName(), "Failed to get input stream.");
		} 
		
		
		if(zipper==null){
			Log.e(tag, "Zipper is null.");
			return false;
		}
		
		Log.d(tag, "Zipping.");
		
		ZipEntry entry = new ZipEntry(filename);
		try {
			zipper.putNextEntry(entry);
		} catch (IOException e) {
			Log.e(tag, "Failed to add zip entry.");
			return false;
		}
		
		try {
			while((count = origin.read(buffer, 0, FileConstants.BUFFER)) != -1){
				zipper.write(buffer, 0, count);
			}
		} catch (IOException e) {
			Log.e(tag, "Failed read origin.");
			return false;
		}
		
		try {
			zipper.flush();
			fis.close();
			origin.close();
		} catch (IOException e) {
			Log.e(tag, "Failed to finish.");
		}
		
		return true;
	}
	
	
}
