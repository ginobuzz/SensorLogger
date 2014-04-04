package edu.buffalo.cse664.sensorlogger;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import edu.buffalo.cse664.sensorlogger.storage.StorageConsts;
import edu.buffalo.cse664.sensorlogger.storage.StorageUtils;
import edu.buffalo.cse664.sensorlogger.storage.StorageWriter;

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
		File dir = StorageUtils.getExternalDirectory(this);
		File file = new File(dir, StorageConsts.FILE_METADATA);
		file.delete();
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		StorageWriter writer = new StorageWriter(this, StorageConsts.FILE_METADATA);
		StringBuilder builder = new StringBuilder();
		builder.append(String.valueOf(System.currentTimeMillis()) + ',');// Time
		builder.append(String.valueOf(IMEI) + ',');// IMEI
		builder.append(((standing)? "1" : "0") + ',');// Standing = 1 
		builder.append(((hasCase)? "1" : "0") + ',');// Has case = 1 
		builder.append(android.os.Build.MODEL + ',');// Model
		builder.append(metrics.densityDpi + ',');
		builder.append(metrics.heightPixels + ',');
		builder.append(metrics.widthPixels);
		
		writer.write(builder.toString());
		writer.close();
	}
	
}
