package edu.buffalo.cse664.sensorlogger.network;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import edu.buffalo.cse664.sensorlogger.storage.StorageUtils;

public class NetworkClient {

	public static final String TAG = "NetworkClient";
	
	private HttpClient mClient;
	private HttpPost mRequest;
	private HttpEntity mEntity;
	
	
	public NetworkClient(){}
	
	
	public void postFile(String filepath){
		
		// Find zip file.
		File file = new File(filepath);
		if(!file.exists()){
			Log.e(TAG, "[POST Failed] No file found.");
			return;
		}
		
		// Create entity.
		byte[] data = StorageUtils.fileToByteArray(file);
		Log.d(TAG, "Size: " + data.length);
		mEntity = new ByteArrayEntity(data);
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				post();
			}
			
		}).start();
		
		
	}
	
	private void post(){
		mClient = new DefaultHttpClient();
		mRequest = new HttpPost(NetworkConstants.URL);
		mRequest.setEntity(mEntity);
		
		// Execute request.
		HttpResponse response = null;
		try {
			response = mClient.execute(mRequest);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "HTTP Protocol Error.");
			return;
		} catch (IOException e) {
			Log.e(TAG, "HTTP Connection Aborted.");
			return;
		}
		Log.d(TAG, "Response: " + getStringResponse(response));
	}
	
	private static String getStringResponse(HttpResponse response){
		InputStream in = null;
		try {
			in = response.getEntity().getContent();
		} catch (IllegalStateException e) {
			Log.e(TAG, "Entity not repeatable");
			return null;
		} catch (IOException e) {
			Log.e(TAG, "InputStream could not be created.");
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		
		try{
			while((line=reader.readLine()) != null){
				builder.append(line);
			}
		} catch(IOException e){
			Log.e(TAG, "Reader encountered I/O error.");
			return null;
		}
		
		return builder.toString();
	}

	
}
