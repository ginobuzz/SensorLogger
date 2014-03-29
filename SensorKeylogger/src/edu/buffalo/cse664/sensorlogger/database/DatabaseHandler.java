package edu.buffalo.cse664.sensorlogger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final String TAG = "DatabaseHandler";
	
	public DatabaseHandler(Context context) {
		super(context, Constants.NAME, null, Constants.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Constants.CREATE_TABLES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Constants.UPDATE_TABLES);
	    onCreate(db);
	}

	public synchronized void intsertValues(String table, ContentValues values){
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(table, null, values);
		Log.d(TAG, "Row insert into: " + table);
		db.close();
	}
	
}
