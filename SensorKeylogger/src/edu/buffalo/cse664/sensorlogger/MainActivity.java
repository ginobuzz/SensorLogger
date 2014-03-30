package edu.buffalo.cse664.sensorlogger;

import android.app.Activity;
import android.content.ContentValues;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.Menu;
import edu.buffalo.cse664.sensorlogger.database.DatabaseHandler;
import edu.buffalo.cse664.sensorlogger.view.SurfaceLoggerView;

public class MainActivity extends Activity {

	public static DatabaseHandler db;
	private SurfaceLoggerView surface;
	private SensorHandler sensorAcc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		surface = (SurfaceLoggerView)findViewById(R.id.surfacelogger);
		sensorAcc = new SensorHandler(this, Sensor.TYPE_ACCELEROMETER);
	}

	@Override
	public void onStart(){
		super.onStart();
		synchronized(this){
			db = new DatabaseHandler(this);
		}
		sensorAcc.startListening();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		synchronized(this){
			db.close();
			db = null;
		}
		sensorAcc.stopListening();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static boolean insertToDatabase(String table, ContentValues values){
		if(db == null) return false;
		db.intsertValues(table, values);
		return true;
	}
	
}
