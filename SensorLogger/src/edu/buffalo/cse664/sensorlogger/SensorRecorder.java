package edu.buffalo.cse664.sensorlogger;

import edu.buffalo.cse664.sensorlogger.storage.StorageWriter;
import edu.buffalo.cse664.sensorlogger.storage.StorageConsts;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;

public class SensorRecorder implements SensorEventListener {
	
	private static final int DELAY = SensorManager.SENSOR_DELAY_NORMAL;
	
	private StorageWriter acc_writer;
	private StorageWriter gyr_writer;
	private HandlerThread ht1, ht2;
	private SensorManager sManager;
	
	
	public SensorRecorder(Context context){
		sManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		acc_writer = new StorageWriter(context, StorageConsts.FILE_ACCEL);
		gyr_writer = new StorageWriter(context, StorageConsts.FILE_GYROS);
		ht1 = new HandlerThread(StorageConsts.FILE_ACCEL);
		ht2 = new HandlerThread(StorageConsts.FILE_GYROS);
	}
	
	public synchronized void start(){
		Sensor s1, s2;
		// Start Accelerometer Listener
		ht1.start(); Handler h1 = new Handler(ht1.getLooper());
		if((s1=sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER))!=null)
			sManager.registerListener(this, s1, DELAY, h1);
		// Start Gyroscope Listener
		ht2.start(); Handler h2 = new Handler(ht2.getLooper());
		if((s2=sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE))!=null)
			sManager.registerListener(this, s2, DELAY, h2);
	}
	
	public synchronized void stop(){
		sManager.unregisterListener(this);
		ht1.quit();
		ht2.quit();
		acc_writer.close();
		gyr_writer.close();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)record(event, 2);
		else record(event, 3);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
	
	private void record(final SensorEvent event, final int id){
		new Thread(new Runnable(){
			@Override
			public void run() {
				if(acc_writer == null || gyr_writer == null) return;
				
				String line = String.valueOf(event.timestamp) + ',' +
						String.valueOf(event.values[0]) + ',' +
						String.valueOf(event.values[1]) + ',' +
						String.valueOf(event.values[2]) + ',' +
						String.valueOf(event.accuracy);
				
				if(id == 2) acc_writer.write(line);
				if(id == 3) gyr_writer.write(line);
			}
		}).start();
	}
	
	

}
