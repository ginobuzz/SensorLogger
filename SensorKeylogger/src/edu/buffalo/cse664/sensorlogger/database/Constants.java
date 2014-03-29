package edu.buffalo.cse664.sensorlogger.database;

public class Constants {

	/* Database Info */
	public static final String 	NAME			= "SensorKeyloggerDatabase";
	public static final int 	VERSION 		= 1;
	
	/* Table Names */
	public static final String 	TABLE_SENSORS 	= "Sensor_Events";
	public static final String 	TABLE_CLICKS  	= "Click_Events";
	
	/* Columns */
	public static final String 	COL_TIME 		= "TIMESTAMP";
	public static final String 	COL_X  			= "X";
	public static final String 	COL_Y  			= "Y";
	public static final String 	COL_Z  			= "Z";
	public static final String	COL_ACTION  	= "ACTION";
	public static final String	COL_ACC			= "ACCURACY";
	public static final String 	COL_PRES 		= "PRESSURE";
	public static final String 	COL_HEIGHT  	= "SCREEN_HEIGHT";
	public static final String 	COL_WIDTH  		= "SCREEN_WIDTH";
	public static final String	COL_DPI  		= "DPI";
	public static final String  COL_SENSOR		= "SENSOR";
	
	
	/* Create Statements*/
	public static final String CREATE_TABLES = "create table " + TABLE_CLICKS + "("+
			COL_TIME 	+ " REAL PRIMARY KEY, " +
			COL_X 		+ " REAL, " 			+ 
			COL_Y 		+ " REAL, " 			+
			COL_ACTION 	+ " INTEGER, " 			+
			COL_PRES 	+ " REAL, " 			+
			COL_HEIGHT	+ " INTEGER, "			+
			COL_WIDTH	+ " INTEGER, "			+
			COL_DPI		+ " INTEGER); create table " + TABLE_SENSORS + "(" +
			COL_TIME 	+ " REAL PRIMARY KEY, " +
			COL_SENSOR	+ " INTEGER, "			+
			COL_X 		+ " REAL, " 			+ 
			COL_Y 		+ " REAL, " 			+
			COL_Z 		+ " REAL, " 			+
			COL_ACC		+ " INTEGER);";
	
	/* Update Statements*/
	public static final String UPDATE_TABLES = "DROP TABLE IF EXISTS " + TABLE_CLICKS + "; " + 
			"DROP TABLE IF EXISTS " + TABLE_SENSORS;
}
