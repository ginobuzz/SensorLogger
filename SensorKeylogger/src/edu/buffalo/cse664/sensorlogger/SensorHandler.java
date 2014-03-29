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
import edu.buffalo.cse664.sensorlogger.database.Constants;

public class SensorHandler implements SensorEventListener {

	private static final String TAG = "SensorHandler";
	
	private SensorManager manager;
	private Sensor mAccelerometer;
	private Sensor mGyroscope;
	private HandlerThread handler_accel;
	private HandlerThread handler_gyro;
	
	
	public SensorHandler(Context context){
		manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGyroscope = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		handler_accel = new HandlerThread("AccelerometerThread");
		handler_gyro  = new HandlerThread("GyroscopeThread");
	}

	public void startListening(){
		handler_accel.start();
		handler_gyro.start();
		Handler handlerAccel = new Handler(handler_accel.getLooper());
		Handler handlerGyro  = new Handler(handler_gyro.getLooper());
		manager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL, handlerAccel);
		manager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL, handlerGyro);
		Log.d(TAG, "SensorHandler: Started.");
	}
	
	public void stopListening(){
		manager.unregisterListener(this);
		handler_accel.getLooper().quit();
		handler_accel.getLooper().quit();
		Log.d(TAG, "SensorHandler: Stopped.");
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
			values.put(Constants.COL_TIME, event.timestamp);
			values.put(Constants.COL_SENSOR, event.sensor.getType());
			values.put(Constants.COL_X, event.values[0]);
			values.put(Constants.COL_Y, event.values[1]);
			values.put(Constants.COL_Z, event.values[2]);
			values.put(Constants.COL_ACC, event.accuracy);
			
			synchronized(MainActivity.db_lock){
				if(MainActivity.db != null){
					MainActivity.db.intsertValues(Constants.TABLE_SENSORS, values);
				}
			}
		}
	}
	
}
