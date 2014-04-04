package edu.buffalo.cse664.sensorlogger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import edu.buffalo.cse664.sensorlogger.storage.EventWriter;
import edu.buffalo.cse664.sensorlogger.storage.FileConstants;

public class IntroActivity extends Activity {
	
	private String IMEI = null;
	private boolean standing = false;
	private boolean hasCase = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		
		final CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox1);
		final CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox2);
		final Intent mainActivityIntent = new Intent(this, MainActivity.class);
		
		// Get IMEI
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonyManager.getDeviceId();
		
		// Set Listeners
		findViewById(R.id.button1).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				saveMetadata();
				startActivity(mainActivityIntent);
				finish();
			}
		});
		cb1.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				standing = isChecked;
			}
		});
		cb2.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton v, boolean isChecked) {
				hasCase = isChecked;
			}
		});
	}

	private void saveMetadata(){
		EventWriter writer = new EventWriter(this, FileConstants.FILENAME[0]);
		String time = String.valueOf(System.currentTimeMillis());
		String imei = String.valueOf(IMEI);
		String s = ((standing)? "1" : "0");
		String c = (hasCase)? "1" : "0";
		String line = time + ',' + imei + ',' + s + ',' + c;
		writer.write(line);
		writer.close();
	}
	
}
