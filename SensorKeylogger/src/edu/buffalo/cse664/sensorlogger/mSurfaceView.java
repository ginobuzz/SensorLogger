package edu.buffalo.cse664.sensorlogger;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;


public class mSurfaceView extends SurfaceView implements Runnable, OnTouchListener {

	private static final String TAG = "mSurfaceView";
	private static final String FILENAME = "TouchEvents";
	private static final String[] COLUMNS = {"TIME", "X", "Y", "PRESSURE"};
	private static final int CIRCLE_RADIUS = 50;
	
	private Context context;
	private SurfaceHolder mHolder;
	private Thread mDrawThread;
	private float mX, mY;
	private Paint mPaint;
	private Random mRand;
	private boolean running = false;
	private BufferedFileWriter mWriter;
	
	
	public mSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		mHolder = getHolder();
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		mRand = new Random();
		mX = mY = -100;
		setOnTouchListener(this);
	}

	@Override
	public void run() {
		while(running){
			if(!mHolder.getSurface().isValid()) continue;
			//else...
			Canvas canvas = mHolder.lockCanvas();
			canvas.drawColor(Color.BLACK);
			canvas.drawCircle(mX, mY, CIRCLE_RADIUS, mPaint);
			mHolder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void resume(){
		mWriter = new BufferedFileWriter(context,FILENAME,COLUMNS);
		mDrawThread = new Thread(this);
		running = true;
		newDotLocation();
		mDrawThread.start();
	}
	
	public void pause(){
		running = false;
		mWriter.close();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final int padding = 10;
		boolean xDist = Math.abs(event.getX() - mX) < CIRCLE_RADIUS+padding;
		boolean yDist = Math.abs(event.getY() - mY) < CIRCLE_RADIUS+padding;
		if(xDist && yDist)
			newDotLocation();
			if(event.getAction() == MotionEvent.ACTION_DOWN)
				Log.d(TAG, "Recording new event");
				new EventRecorder(event).start();
		return false;
	}
	
	private void newDotLocation(){
		int width = getWidth();
		int height = getHeight();
		if(width < 2*CIRCLE_RADIUS) mX = CIRCLE_RADIUS;
		else mX = CIRCLE_RADIUS + mRand.nextInt(width  - (2*CIRCLE_RADIUS));
		if(height < 2*CIRCLE_RADIUS) mY = CIRCLE_RADIUS;
		else mY = CIRCLE_RADIUS + mRand.nextInt(height  - (2*CIRCLE_RADIUS));
	}
	
	private class EventRecorder extends Thread {
		
		public EventRecorder(final MotionEvent event){
			super(new Runnable(){
				@Override
				public void run() {
					if(mWriter == null) return;
					String[] values = new String[COLUMNS.length];
					values[0] = String.valueOf(event.getEventTime());
					values[1] = String.valueOf(event.getX());
					values[2] = String.valueOf(event.getY());
					values[3] = String.valueOf(event.getPressure());
					String eventvalue = BufferedFileWriter.arrayToString(values);
					mWriter.write(eventvalue);
				}
			});
		}
	}
	
}
