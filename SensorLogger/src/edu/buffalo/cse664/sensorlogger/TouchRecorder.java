package edu.buffalo.cse664.sensorlogger;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import edu.buffalo.cse664.sensorlogger.storage.StorageConsts;
import edu.buffalo.cse664.sensorlogger.storage.StorageWriter;


public class TouchRecorder extends SurfaceView implements Runnable, OnTouchListener {

	public static final String TAG = "mSurfaceView";
	public static final int CIRCLE_RADIUS = 50;
	public static final int CIRCLE_PADDING = 10;
	public static final int COLOR_CIRCLE = Color.parseColor("#63AFFF");
	public static final int COLOR_BACKGROUND = Color.parseColor("#F2F2F2");
	public static final int VIBRATE_DURATION = 20;
	
	private float x_pos = -100;
	private float y_pos = -100;
	private float x_max = 0;
	private float y_max = 0;
	
	private Integer count;
	private Thread drawThread;
	private SurfaceHolder holder;
	private Paint paint;
	private Random rand;
	private StorageWriter writer;
	private Vibrator vib;
	private boolean running = false;
	
	
	public TouchRecorder(Context context, AttributeSet attrs) {
		super(context, attrs);
		writer = new StorageWriter(context, StorageConsts.FILE_TOUCH);
		vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(COLOR_CIRCLE);
		rand = new Random();
	}

	public synchronized void start(){
		running = true;
		count = 0;
		holder = getHolder();
		drawThread = new Thread(this);
		drawThread.start();
		redraw();
	}
	
	public synchronized void stop(){
		running = false;
		try {
			drawThread.join(2000);
		} catch (InterruptedException e) {
			drawThread.interrupt();
		}
		writer.close();
	}
	
	public int getCount(){
		synchronized(count){
			return count.intValue();
		}
	}
	
	@Override
	public void run() {
		while(running){
			if(!holder.getSurface().isValid()) continue;
			Canvas canvas = holder.lockCanvas();
			canvas.drawColor(COLOR_BACKGROUND);
			canvas.drawCircle(x_pos, y_pos, CIRCLE_RADIUS, paint);
			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	@Override
    protected void onSizeChanged(int l, int w, int oldl, int oldw)
    {        
        super.onSizeChanged(l, w, oldl, oldw);
        if(l > oldl) x_max = l;
        if(w > oldw) y_max = w;
        redraw();
    }
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		super.onTouchEvent(event);
		double x_len = Math.pow(x_pos - event.getX(), 2);
		double y_len = Math.pow(y_pos - event.getY(), 2);
		double distance = Math.sqrt(x_len + y_len);
		if(distance < CIRCLE_RADIUS + CIRCLE_PADDING){
			synchronized(count){
				vib.vibrate(VIBRATE_DURATION);
				new RecorderThread(count.intValue(), event).start();
				++count;
				redraw();
			}
		}
		return false;
	}
	
	private void redraw(){
		final int diameter = 2 * CIRCLE_RADIUS;
		if(x_max > diameter) x_pos = CIRCLE_RADIUS + rand.nextInt((int)x_max - diameter);
		if(y_max > diameter) y_pos = CIRCLE_RADIUS + rand.nextInt((int)y_max - diameter);
	}
	
	private class RecorderThread implements Runnable {
		
		private int count;
		private MotionEvent event;
		
		public RecorderThread(int count, MotionEvent event){
			this.count = count;
			this.event = event;
		}

		public void start(){
			new Thread(this).start();
		}
		
		@Override
		public void run() {
			Log.d(TAG, "RECORDING");
			if(writer == null) return;
			String line = String.valueOf(event.getEventTime()) + ',' +
					String.valueOf(count) + ',' +
					String.valueOf(event.getX()) + ',' +
					String.valueOf(event.getY()) + ',' +
					String.valueOf(event.getPressure());
			writer.write(line);
		}
		
		
	}

}
