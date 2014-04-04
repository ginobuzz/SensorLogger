package edu.buffalo.cse664.sensorlogger;

import edu.buffalo.cse664.sensorlogger.storage.EventWriter;
import edu.buffalo.cse664.sensorlogger.storage.FileConstants;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;

public class SensorRecorder implements SensorEventListener {
	
	private EventWriter acc_writer;
	private EventWriter gyr_writer;
	private HandlerThread ht1, ht2;
	private SensorManager sManager;
	
	
	public SensorRecorder(Context context){
		sManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		acc_writer = new EventWriter(context, FileConstants.FILENAME[2]);
		gyr_writer = new EventWriter(context, FileConstants.FILENAME[3]);
		ht1 = new HandlerThread("AccelerometerThread");
		ht2 = new HandlerThread("GyroscopeThread");
	}
	
	public synchronized void start(){
		ht1.start();
		ht2.start();
		Handler h1 = new Handler(ht1.getLooper());
		Handler h2 = new Handler(ht2.getLooper());
		Sensor s1, s2;
		if((s1=sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER))!=null)
			sManager.registerListener(this, s1, SensorManager.SENSOR_DELAY_NORMAL, h1);
		
		if((s2=sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE))!=null)
			sManager.registerListener(this, s2, SensorManager.SENSOR_DELAY_NORMAL, h2);
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
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)record(event, FileManager.ACCELEROMETER);
		else record(event, FileManager.GYROROSCOPE);
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
				
				if(id == FileManager.ACCELEROMETER)
					acc_writer.write(line);
				
				if(id == FileManager.GYROROSCOPE)
					gyr_writer.write(line);
			}
		}).start();
	}
	
	

}
