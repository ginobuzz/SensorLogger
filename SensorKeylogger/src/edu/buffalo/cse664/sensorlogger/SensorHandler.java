package edu.buffalo.cse664.sensorlogger;

import android.content.ContentValues;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class SensorHandler implements SensorEventListener {

	private static final String TAG = "SensorHandler";
	
	private SensorManager manager;
	private Sensor mSensor;
	private HandlerThread hThread;
	private Handler mHandler;
	
	
	public SensorHandler(Context context, int sensor){
		manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		mSensor = manager.getDefaultSensor(sensor);
	}

	public synchronized void startListening(){
		if(mSensor == null){
			Log.e(TAG, "Sensor is null.");
			return;
		}
		hThread = new HandlerThread(mSensor.getName()+"_Thread");
		hThread.start();
		mHandler = new Handler(hThread.getLooper());
		manager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL, mHandler);
		Log.d(TAG, "SensorHandler("+mSensor.getName()+"): STARTED");
	}
	
	public synchronized void stopListening(){
		manager.unregisterListener(this);
		hThread.quit();
		Log.d(TAG, "SensorHandler("+mSensor.getName()+"): STOPPED");
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		new EventRecorder(event).start();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	private class EventRecorder extends Thread{
		private SensorEvent event;
		
		public EventRecorder(SensorEvent e){
			event = e;
		}
		
		@Override
		public void run(){
			ContentValues values = new ContentValues();
			//values.put(Constants.COL_TIME, event.timestamp);
			//values.put(Constants.COL_SENSOR, event.sensor.getType());
			///values.put(Constants.COL_X, event.values[0]);
			//values.put(Constants.COL_Y, event.values[1]);
			//values.put(Constants.COL_Z, event.values[2]);
			//values.put(Constants.COL_ACC, event.accuracy);
			//if(!MainActivity.insertToDatabase(Constants.TABLE_SENSORS, values))
			//	stopListening();
			//return;
		}
	}
	
}
