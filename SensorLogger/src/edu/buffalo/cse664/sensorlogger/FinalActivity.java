package edu.buffalo.cse664.sensorlogger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

public class FinalActivity extends Activity {
	
	private static final int BUFFER = 2048; 
	
	private FileManager manager;
	private Button bt;
	private TextView tv; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);
		manager = new FileManager(this, 1);
		
		tv = (TextView)findViewById(R.id.tv1);
		
		bt = (Button)findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	public void onStart(){
		super.onStart();
		zip();
	}
	
	private void post(){
		tv.setText("Sending results.");
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
		complete();
	}
	
	private void complete(){
		tv.setText("Results have been sent.");
		
	}
	
	public void zip() { 
		tv.setText("Zipping results.");
		
		final String zipFile = "zippedEvents.zip";
		final String[] files = FileManager.FILENAME;
		
		File dir = new File(getFilesDir(), FileManager.DIRECTORY);
		if(!dir.exists()){
			Log.e("Zip", "Directory not found.");
			return;
		}
		File exDir = new File(getExternalCacheDir(), "SensorLogger");
		exDir.mkdirs();
		
		Log.d("Zip", "Directory found.");
		
		
		
		String path = dir.getAbsolutePath() + File.separator;
		String exPath = exDir.getAbsolutePath() + File.separator;
		
		
	    try  { 
	      BufferedInputStream origin = null; 
	      FileOutputStream dest = new FileOutputStream(exPath + zipFile); 
	      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest)); 
	      byte data[] = new byte[BUFFER]; 
	 
	      for(int i=0; i < files.length; i++) { 
	        Log.v("Compress", "Adding: " + files[i]); 
	        FileInputStream fi = new FileInputStream(path + files[i]); 
	        origin = new BufferedInputStream(fi, BUFFER); 
	        ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1)); 
	        out.putNextEntry(entry); 
	        int count; 
	        while ((count = origin.read(data, 0, BUFFER)) != -1) { 
	          out.write(data, 0, count); 
	        } 
	        origin.close(); 
	      } 
	      out.close(); 
	    } catch(Exception e) { 
	      e.printStackTrace(); 
	    } 
	  } 
	
}
