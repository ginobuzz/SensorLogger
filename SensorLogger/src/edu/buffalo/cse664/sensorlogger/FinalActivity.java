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
		
		tv.setText("Zipping results.");
		zipper = new Zipper(this);
		File zipfile = zipper.getZip();
		
		if(zipfile!=null){
			tv.setText("Sending results.");
			NetworkClient client = new NetworkClient();
			client.postFile(zipfile.getAbsolutePath());
			tv.setText("Results have been sent.");
		}else {
			Log.e(TAG, "Zipfile is null.");
			tv.setText("FAILED TO ZIP");
		}
		
		bt.setText("Exit");
		bt.setClickable(true);
	}
	

	
}
