package edu.buffalo.cse664.sensorlogger.network;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import edu.buffalo.cse664.sensorlogger.storage.StorageUtils;

public class NetworkClient {

	public static final String TAG = "NetworkClient";
	
	private ConnectivityManager manager;
	
	
	public NetworkClient(Context context){
		manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	public void post(String filepath){
		if(isNetworkAvailable()) new AsyncPost().execute(filepath);
		else Log.e(TAG, "Network unavailable.");
	}
	
	
	private boolean isNetworkAvailable() {
	    NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	
	private class AsyncPost extends AsyncTask<String,Void,String> {

		@Override
		protected String doInBackground(String... path) {
			String responseText = null;
			
            File file = new File(path[0]);
            if(!file.exists()){
            	Log.e(TAG, "Post Failed. No file found.");
    			return null;
            }

            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(NetworkConstants.URL);
    		byte[] data = StorageUtils.fileToByteArray(file);
    		Log.d(TAG, "Size: " + data.length);
    		request.setEntity(new ByteArrayEntity(data));
    		
    		// Execute post.
    		try {
				HttpResponse response = client.execute(request);
				HttpEntity entity = response.getEntity();
				responseText = EntityUtils.toString(entity);
				Log.d(TAG, responseText);
			} catch (Exception e) {
				Log.e(TAG, "HTTP Connection Aborted.");
			}
    		
			return responseText;
		}
	}
	

}
