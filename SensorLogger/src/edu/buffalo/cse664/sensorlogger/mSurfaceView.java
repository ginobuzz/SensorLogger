package edu.buffalo.cse664.sensorlogger;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class mSurfaceView extends SurfaceView implements Runnable {

	public static final String TAG = "mSurfaceView";
	public static final int CIRCLE_RADIUS = 50;
	
	private SurfaceHolder mHolder;
	private Thread mDrawThread;
	public float mX, mY;
	private Paint mPaint;
	private Random mRand;
	private boolean running = false;
	private FileManager mWriter;
	public int count;
	
	public mSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder();
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		mRand = new Random();
		mX = mY = -100;
		count = 0;
	}

	@Override
	public void run() {
		while(running){
			if(!mHolder.getSurface().isValid()) continue;
			//else...
			Canvas canvas = mHolder.lockCanvas();
			canvas.drawColor(Color.LTGRAY);
			canvas.drawCircle(mX, mY, CIRCLE_RADIUS, mPaint);
			mHolder.unlockCanvasAndPost(canvas);
		}
	}
	
	public void resume(Context context){
		mWriter = new FileManager(context, FileManager.TOUCH);
		running = true;
		mDrawThread = new Thread(this);
		newDotLocation();
		mDrawThread.start();
	}
	
	public void pause(){
		running = false;
		this.setOnTouchListener(null);
		mWriter.close();
	}

	public void recordEvent(MotionEvent event){
		final int padding = 10;
		boolean xDist = Math.abs(event.getX() - mX) < CIRCLE_RADIUS + padding;
		boolean yDist = Math.abs(event.getY() - mY) < CIRCLE_RADIUS + padding;
		if(xDist && yDist){
			new Recorder(event);
		}
	}
	
	private void newDotLocation(){
		int width = getWidth();
		int height = getHeight();
		if(width < 2*CIRCLE_RADIUS) mX = CIRCLE_RADIUS;
		else mX = CIRCLE_RADIUS + mRand.nextInt(width  - (2*CIRCLE_RADIUS));
		if(height < 2*CIRCLE_RADIUS) mY = CIRCLE_RADIUS;
		else mY = CIRCLE_RADIUS + mRand.nextInt(height  - (2*CIRCLE_RADIUS));
	}
	
	
	private class Recorder {
		public Recorder(final MotionEvent event){
			new Thread(new Runnable(){
				@Override
				public void run() {
					++count;
					newDotLocation();
					if(mWriter == null) return;
					String line = String.valueOf(event.getEventTime()) + ',' +
							String.valueOf(event.getX()) + ',' +
							String.valueOf(event.getY()) + ',' +
							String.valueOf(event.getPressure());
					mWriter.write(line);
				}
			}).start();
		}
	}
	
}
