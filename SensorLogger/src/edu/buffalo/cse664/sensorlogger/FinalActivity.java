package edu.buffalo.cse664.sensorlogger;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.buffalo.cse664.sensorlogger.network.NetworkClient;
import edu.buffalo.cse664.sensorlogger.storage.StorageUtils;
import edu.buffalo.cse664.sensorlogger.storage.Zipper;

public class FinalActivity extends Activity {

	public static final String TAG = "FinalActivity";
	
	private Zipper zipper;
	private Button bt;
	private TextView tv; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);
		tv = (TextView)findViewById(R.id.tv1);
		bt = (Button)findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		bt.setClickable(false);
		bt.setText("Processing..");
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		File zip = getZippedResults();
		if(zip==null) tv.setText("Failed to zip file.");
		else sendResults(zip);
		
		bt.setText("Exit");
		bt.setClickable(true);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		StorageUtils.clearCache(this);
	}
	
	private File getZippedResults(){
		tv.setText("Zipping results.");
		zipper = new Zipper(this);
		File zip = zipper.getZip();
		if(zip == null){
			Log.e(TAG, "Failed to zip file.");
			return null;
		}
		else if(!zip.exists()){
			Log.e(TAG, "Zip file does not exist.");
			return null;
		}
		return zip;
	}
	
	private void sendResults(File file){
		tv.setText("Sending results.");
		NetworkClient client = new NetworkClient(this);
		client.post(file.getAbsolutePath());
		tv.setText("Results have been sent.");
	}
	
}
