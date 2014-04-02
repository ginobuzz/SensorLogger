package edu.buffalo.cse664.sensorlogger.storage;

public class FileConstants {

	/* Directory Name */
	public static final String DIRECTORY = "SensorLogger";
	
	/* Collected Data Filenames */
	public static final String[] FILENAME = { 
		"Metadata",
		"TouchEvents", 
		"AccelerometerEvents", 
		"GyroscopeEvents"
	};
	
	/* Zip Filename */
	public static final String ZIPFILE = "data.zip";
	
	/* Buffer for Zipfile */
	public static final int BUFFER = 2048; 
	
}
