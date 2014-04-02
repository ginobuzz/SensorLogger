package edu.buffalo.cse664.sensorlogger.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import edu.buffalo.cse664.sensorlogger.FileManager;

import android.content.Context;
import android.util.Log;

public class Zipper {

	private static final String TAG = "Zipper";
	private static final String[] FILE = FileManager.FILENAME;
	private File intDirectory;
	private File extDirectory;
	private String internal_path;
	private String external_path;
	private File zipFile;
	
	public Zipper(Context context){
		intDirectory = new File(context.getFilesDir(), FileConstants.DIRECTORY);
		intDirectory.mkdir();
		extDirectory = new File(context.getExternalCacheDir(), FileConstants.DIRECTORY);
		extDirectory.mkdirs();
		internal_path = intDirectory.getAbsolutePath() + File.separator;
		external_path = extDirectory.getAbsolutePath() + File.separator;
		zipFile = null;
	}
	
	public boolean zipFiles(){
		byte[] data = new byte[FileConstants.BUFFER];
		BufferedInputStream src = null; 
		zipFile = new File(external_path + FileConstants.ZIPFILE);
		
		try{ 
			FileOutputStream dst = new FileOutputStream(zipFile); 
			ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(dst)); 
			for(int i = 0; i < FILE.length; i++){
				Log.v(TAG, "Adding: " + FILE[i]); 
				File file = new File(internal_path + FILE[i]);
				FileInputStream fin = new FileInputStream(file); 
				src = new BufferedInputStream(fin, FileConstants.BUFFER);  
				ZipEntry entry = new ZipEntry(FILE[i].substring(FILE[i].lastIndexOf("/") + 1));
				zipOut.putNextEntry(entry);
				int count; 
		        while((count = src.read(data, 0, FileConstants.BUFFER)) != -1) { 
		        	zipOut.write(data, 0, count); 
			    } 
			    src.close(); 
			    file.delete();
			}
			zipOut.close();
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public File getZipFile(){
		// TODO: Check if rezip is needed.
		boolean result = true;
		if(zipFile == null) result = zipFiles();
		if(result)return zipFile;
		else return null;
	}
	
}
