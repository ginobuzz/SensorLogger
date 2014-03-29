package edu.buffalo.cse664.sensorlogger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import edu.buffalo.cse664.sensorlogger.database.DatabaseHandler;
import edu.buffalo.cse664.sensorlogger.view.SurfaceLoggerView;

public class MainActivity extends Activity {

	public static Object db_lock = new Object();
	public static DatabaseHandler db;
	private SurfaceLoggerView surface;
	private SensorHandler sensorHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		surface = (SurfaceLoggerView)findViewById(R.id.surfacelogger);
		sensorHandler = new SensorHandler(this);
	}

	@Override
	public void onStart(){
		super.onStart();
		synchronized(db_lock){
			db = new DatabaseHandler(this);
		}
		sensorHandler.startListening();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		synchronized(db_lock){
			db.close();
			db = null;
		}
		sensorHandler.stopListening();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
}
