package edu.buffalo.cse664.sensorlogger;

import android.app.Activity;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private mSurfaceView surface;
	private SensorHandler sensorAcc;
	private SensorHandler sensorGyr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		surface = (mSurfaceView)findViewById(R.id.surfacelogger);
		sensorAcc = new SensorHandler(this, Sensor.TYPE_ACCELEROMETER, "Accelerometer");
		sensorGyr = new SensorHandler(this, Sensor.TYPE_GYROSCOPE, "Gyroscope");
	}

	@Override
	public void onStart(){
		super.onStart();
		sensorAcc.startListening();
		sensorGyr.startListening();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		surface.resume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		surface.pause();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		sensorAcc.stopListening();
		sensorGyr.stopListening();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void recordMetadata(){
		
	}

	
}
