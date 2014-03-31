package edu.buffalo.cse664.sensorlogger;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;

public class SensorRecorder implements SensorEventListener {
	
	private Context context;
	private FileManager accelWriter;
	private FileManager gyroWriter;
	private HandlerThread ht1, ht2;
	private SensorManager sManager;
	
	
	public SensorRecorder(Context context){
		this.context = context;
		sManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
	}
	
	public synchronized void start(){
		accelWriter = new FileManager(context, FileManager.ACCELEROMETER);
		gyroWriter = new FileManager(context, FileManager.GYROROSCOPE);
		ht1 = new HandlerThread("AccelerometerThread");
		ht2 = new HandlerThread("GyroscopeThread");
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
		accelWriter.close();
		gyroWriter.close();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			recordEvent(event, FileManager.ACCELEROMETER);
		else
			recordEvent(event, FileManager.GYROROSCOPE);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
	
	
	private void recordEvent(final SensorEvent event, final int id){
		new Thread(new Runnable(){
			@Override
			public void run() {
				if(accelWriter == null || gyroWriter == null) return;
				String line = String.valueOf(event.timestamp) + ',' +
						String.valueOf(event.values[0]) + ',' +
						String.valueOf(event.values[1]) + ',' +
						String.valueOf(event.values[2]) + ',' +
						String.valueOf(event.accuracy);
				if(id == FileManager.ACCELEROMETER)
					accelWriter.write(line);
				if(id == FileManager.GYROROSCOPE)
					gyroWriter.write(line);
			}
		}).start();
	}
	
	

}
