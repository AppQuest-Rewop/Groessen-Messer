package ch.rewop.groessenmesser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class CustomView extends SurfaceView {

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		Paint pinsel = new Paint();
		pinsel.setColor(Color.GREEN);
		pinsel.setStrokeWidth(3);
		
		canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, pinsel);
	}
}
