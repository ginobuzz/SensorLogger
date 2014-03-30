package edu.buffalo.cse664.sensorlogger;

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
	private static final String[] COLUMNS = {"TIME", "X", "Y", "Z", "ACC"};
	
	private Context context;
	private SensorManager manager;
	private Sensor mSensor;
	private HandlerThread hThread;
	private Handler mHandler;
	private BufferedFileWriter mWriter;
	private String name;
	
	public SensorHandler(Context context, int sensor, String sensorName){
		this.context = context;
		manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		mSensor = manager.getDefaultSensor(sensor);
		name = sensorName;
	}
		
	public synchronized void startListening(){
		if(mSensor == null){
			Log.e(TAG, "Sensor is null.");
			return;
		}
		String filename = name+"Events";
		mWriter = new BufferedFileWriter(context, filename, COLUMNS);
		hThread = new HandlerThread(mSensor.getName()+"_Thread");
		hThread.start();
		mHandler = new Handler(hThread.getLooper());
		manager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL, mHandler);
		Log.d(TAG, "SensorHandler("+mSensor.getName()+"): STARTED");
	}
	
	public synchronized void stopListening(){
		manager.unregisterListener(this);
		hThread.quit();
		mWriter.close();
		mWriter = null;
		Log.d(TAG, "SensorHandler("+mSensor.getName()+"): STOPPED");
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		new EventRecorder(event).start();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	
	private class EventRecorder extends Thread{
		public EventRecorder(final SensorEvent event){
			super(new Runnable(){
				@Override
				public void run() {
					if(mWriter == null) return;
					String[] values = new String[COLUMNS.length];
					values[0] = String.valueOf(event.timestamp);
					values[1] = String.valueOf(event.values[0]);
					values[2] = String.valueOf(event.values[1]);
					values[3] = String.valueOf(event.values[2]);
					values[4] = String.valueOf(event.accuracy);
					String eventvalue = BufferedFileWriter.arrayToString(values);
					mWriter.write(eventvalue);
				}
			});
		}
	}
	
}
