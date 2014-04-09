package edu.buffalo.cse664.sensorlogger;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import edu.buffalo.cse664.sensorlogger.storage.StorageConsts;
import edu.buffalo.cse664.sensorlogger.storage.StorageWriter;

public class SensorRecorder implements SensorEventListener {
	
	private static final int DELAY = SensorManager.SENSOR_DELAY_NORMAL;
	
	private StorageWriter acc_writer;
	private StorageWriter gyr_writer;
	private StorageWriter rot_writer;
	private HandlerThread ht1, ht2, ht3;
	private SensorManager sManager;
	
	
	public SensorRecorder(Context context){
		sManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		acc_writer = new StorageWriter(context, StorageConsts.FILE_ACCEL);
		gyr_writer = new StorageWriter(context, StorageConsts.FILE_GYROS);
		rot_writer = new StorageWriter(context, StorageConsts.FILE_ROTAT);
		ht1 = new HandlerThread(StorageConsts.FILE_ACCEL);
		ht2 = new HandlerThread(StorageConsts.FILE_GYROS);
		ht3 = new HandlerThread(StorageConsts.FILE_ROTAT);
	}
	
	public synchronized void start(){
		Sensor s1, s2, s3;
		
		// Start Accelerometer Listener
		ht1.start(); Handler h1 = new Handler(ht1.getLooper());
		if((s1=sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER))!=null)
			sManager.registerListener(this, s1, DELAY, h1);
		
		// Start Gyroscope Listener
		ht2.start(); Handler h2 = new Handler(ht2.getLooper());
		if((s2=sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE))!=null)
			sManager.registerListener(this, s2, DELAY, h2);
		
		// Start Rotation Vector Listener
		ht3.start(); Handler h3 = new Handler(ht3.getLooper());
		if((s3=sManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR))!=null)
			sManager.registerListener(this, s3, DELAY, h3);
	}
	
	public synchronized void stop(){
		sManager.unregisterListener(this);
		ht1.quit();
		ht2.quit();
		ht3.quit();
		acc_writer.close();
		gyr_writer.close();
		rot_writer.close();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		long timestamp = System.currentTimeMillis();
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
			record(event, 2, timestamp);
		else if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE) 
			record(event, 3, timestamp);
		else if(event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR) 
			record(event, 4, timestamp);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	
	private void record(final SensorEvent event, final int id, final long timestamp){
		new Thread(new Runnable(){
			@Override
			public void run() {
				if(acc_writer == null || gyr_writer == null) return;
				String line = String.valueOf(timestamp) + ',' +
						String.valueOf(MainActivity.counter) + ',' + 
						String.valueOf(event.values[0]) + ',' +
						String.valueOf(event.values[1]) + ',' +
						String.valueOf(event.values[2]) + ',' +
						String.valueOf(event.accuracy);
				
				if(id == 2) acc_writer.write(line);
				if(id == 3) gyr_writer.write(line);
				if(id == 4) rot_writer.write(line);
			}
		}).start();
	}
	
	

}
