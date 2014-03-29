package edu.buffalo.cse664.sensorlogger.view;

import android.content.ContentValues;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import edu.buffalo.cse664.sensorlogger.MainActivity;
import edu.buffalo.cse664.sensorlogger.database.Constants;

public class SurfaceLoggerView extends SurfaceView implements OnTouchListener {

	private static final String TAG = "SurfaceLogger";
	
	private static int height;
	private static int width;
	private static int dpi;
	
	
	public SurfaceLoggerView(Context context) {
		super(context);
		init(context);
	}
	
	public SurfaceLoggerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public SurfaceLoggerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	
	private void init(Context context){
		setOnTouchListener(this);
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		height = dm.heightPixels;
		width = dm.widthPixels;
		dpi = dm.densityDpi;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int act = event.getAction();
		if(act == MotionEvent.ACTION_DOWN || act == MotionEvent.ACTION_UP){
			new EventRecorder(event).start();
		}
		return false;
	}
	
	private class EventRecorder extends Thread{
		private MotionEvent event;
		
		public EventRecorder(MotionEvent e){
			event = e;
		}
		
		@Override
		public void run(){
			Log.d(TAG, "Recording new event.");
			ContentValues values = new ContentValues();
			values.put(Constants.COL_TIME, event.getEventTime());
			values.put(Constants.COL_X, event.getRawX());
			values.put(Constants.COL_Y, event.getRawY());
			values.put(Constants.COL_ACTION, event.getAction());
			values.put(Constants.COL_PRES, event.getPressure());
			values.put(Constants.COL_HEIGHT, height);
			values.put(Constants.COL_WIDTH, width);
			values.put(Constants.COL_DPI, dpi);
			
			synchronized(MainActivity.db_lock){
				if(MainActivity.db != null){
					MainActivity.db.intsertValues(Constants.TABLE_CLICKS, values);
				}
			}
			
		}
	}
	
}
