package edu.buffalo.cse664.sensorlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity implements OnTouchListener {

	private static final int MAX_TOUCH = 5;
	
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
	}
	
	@Override
	public void onResume(){
		super.onResume();
		surface.resume(this);
		sensorRecorder.start();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		surface.pause();
		sensorRecorder.stop();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		finish();
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
			startActivity(finalIntent);
			finish();
		}
		return false;
	}
	

}
