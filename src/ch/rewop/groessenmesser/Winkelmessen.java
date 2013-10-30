package ch.rewop.groessenmesser;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Winkelmessen extends Activity implements SurfaceHolder.Callback, Camera.PictureCallback, SensorEventListener {

	private Camera camera;
	private SurfaceHolder cameraViewHolder;
	
	private SensorManager sensorManager;
	private Sensor magnetSensor;
	private float[] messwert;
	private boolean first;
	private float winkel1;
	private float winkel2;
	
	private SurfaceView cameraView;
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(first){
				first = false;
				winkel1 = (float) Math.sqrt(messwert[1] * messwert[1]);
			} else {
				
				winkel2 = (float) (Math.sqrt(messwert[1] * messwert[1]))-winkel1;
				
				Intent intent = new Intent(this, Hoehemessen.class);
				intent.putExtra("winkel1", winkel1);
				intent.putExtra("winkel2", winkel2);
				startActivity(intent);
			}
			
			
			
			
			//Toast.makeText(this, ""+messwert[1], Toast.LENGTH_SHORT).show();
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_winkelmessen);
		
		try{
	        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	        magnetSensor = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);
	        }
	        catch(Exception e) {
	        	Log.d(SENSOR_SERVICE, e.getMessage());
	        	Toast.makeText(this, "No Orientation Sensor detected :0 - are you using an emulator?", Toast.LENGTH_LONG).show();
	        }    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.winkelmessen, menu);
		return true;
	}

	@Override
	protected void onResume(){
		super.onResume();
		cameraView = (SurfaceView) findViewById(R.id.kameraSicht);
		cameraViewHolder = cameraView.getHolder();
		cameraViewHolder.addCallback(this);
		
		sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
		first = true;
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		
		if(camera != null){
			camera.stopPreview();
			camera.release();
		}
    	sensorManager.unregisterListener(this);
	}

	@Override
	public void onPictureTaken(byte[] arg0, Camera arg1) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		camera.stopPreview();
		camera.setDisplayOrientation(90);
		
		try{
			camera.setPreviewDisplay(holder);
		} catch(IOException e){
			Log.d("", e.getMessage());
		}
		
		camera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		camera = Camera.open();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {	
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		messwert = event.values;
	}
}
