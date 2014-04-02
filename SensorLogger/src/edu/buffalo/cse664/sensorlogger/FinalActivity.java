package edu.buffalo.cse664.sensorlogger;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.buffalo.cse664.sensorlogger.storage.Zipper;

public class FinalActivity extends Activity {

	private Zipper zipper;
	private Button bt;
	private TextView tv; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);
		zipper = new Zipper(this);
		
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
		File zipfile = zipper.getZipFile();
		if(zipfile!=null){
			tv.setText("Sending results.");
			post(zipfile);
		}
		tv.setText("Results have been sent.");
		bt.setText("Exit");
		bt.setClickable(true);
	}
	
	private void post(File file){
		final String URL = "www.google.com";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(URL);
		// Set Entity
		
		try {
			HttpResponse response = client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
