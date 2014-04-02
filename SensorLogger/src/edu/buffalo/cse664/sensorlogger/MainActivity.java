package edu.buffalo.cse664.sensorlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity implements OnTouchListener {

	private static final int MAX_TOUCH = 15;
	
	private mSurfaceView surface;
	private SensorRecorder sensorRecorder;
	private Intent finalIntent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		surface = (mSurfaceView)findViewById(R.id.surfacelogger);
		surface.setOnTouchListener(this);
		sensorRecorder = new SensorRecorder(this);
		finalIntent = new Intent(this, FinalActivity.class);
	}

	@Override
	public void onStart(){
		super.onStart();
		sensorRecorder.start();
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
		finish();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		sensorRecorder.stop();
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
			startActivity(finalIntent);
			finish();
		}
		return false;
	}
	
}
