package edu.buffalo.cse664.sensorlogger;

import android.app.Activity;
import android.os.Bundle;

public class FinalActivity extends Activity {

	private FileManager manager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		manager = new FileManager(this, 1);
		
	}
	
	
}
