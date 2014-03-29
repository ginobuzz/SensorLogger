package edu.buffalo.cse664.keylogger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends Activity {

	public Button[] button;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Initialize buttons
		String packageName = getPackageName();
		for(int i = 0; i<30; i++){
			String name = "bt" + (i+1);
			int id = getResources().getIdentifier(name, "id", packageName);
			button[i] = (Button)findViewById(id);
			button[i].setTag(i);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
