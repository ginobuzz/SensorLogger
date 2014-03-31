package edu.buffalo.cse664.sensorlogger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity implements OnTouchListener {

	private static final int MAX_TOUCH = 15;
	
	private mSurfaceView surface;
	private SensorRecorder sensorRecorder;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		surface = (mSurfaceView)findViewById(R.id.surfacelogger);
		surface.setOnTouchListener(this);
		sensorRecorder = new SensorRecorder(this);
		//sensorAcc = new SensorHandler(this, Sensor.TYPE_ACCELEROMETER, "Accelerometer");
		//sensorGyr = new SensorHandler(this, Sensor.TYPE_GYROSCOPE, "Gyroscope");
	}

	@Override
	public void onStart(){
		super.onStart();
		sensorRecorder.start();
		//sensorAcc.startListening();
		//sensorGyr.startListening();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		surface.resume(this);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		surface.pause();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		sensorRecorder.stop();
		//sensorAcc.stopListening();
		//sensorGyr.stopListening();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		surface.recordEvent(event);
		if(surface.count > MAX_TOUCH){
			surface.pause();
			sensorRecorder.stop();
			//QUIT
		}
		return false;
	}
	
}
