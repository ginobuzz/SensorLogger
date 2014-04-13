package edu.buffalo.cse664.sensorlogger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchListener {

	private static final int MAX_TOUCH = 20;
	
	public static volatile int counter = 0;
	
	private TouchRecorder surface;
	private SensorRecorder sensorRecorder;
	private Intent finalIntent;
	private TextView tv_counter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		surface = (TouchRecorder)findViewById(R.id.surfacelogger);
		tv_counter = (TextView)findViewById(R.id.tv_counter);
		surface.setOnTouchListener(this);
		sensorRecorder = new SensorRecorder(this);
		finalIntent = new Intent(this, FinalActivity.class);
		counter = 0;
	}

	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		surface.start();
		sensorRecorder.start();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		surface.stop();
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
		surface.onTouch(v, event);
		counter = surface.getCount();
		tv_counter.setText("Number Pressed: " + counter);
		if(counter >= MAX_TOUCH){
			startActivity(finalIntent);
			finish();
		}
		return false;
	}
	
	
	

}
