package ch.rewop.groessenmesser;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Hoehemessen extends Activity {
	private float winkel1;
	private float winkel2;
	private float gamma;
	private TextView alpha;
	private TextView beta;
	private TextView b;
	private long result;
	private Double abstand;
	private EditText a;
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hoehenmessen, menu);
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_hoehenmessen);  
		
		Bundle extras = getIntent().getExtras();
		winkel1 = extras.getFloat("winkel1");
		winkel2 = extras.getFloat("winkel2");
		gamma = 180 - (winkel1+winkel2);
		Toast.makeText(this, ""+winkel1, Toast.LENGTH_SHORT).show();
		
		alpha = (TextView)findViewById(R.id.alpha);
		alpha.setText(""+(int)winkel1);
		beta = (TextView)findViewById(R.id.beta);
		beta.setText(""+(int)winkel2);
		b = (TextView)findViewById(R.id.b);
		a = (EditText)findViewById(R.id.a);
		a.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				abstand = Double.parseDouble(a.getText().toString());
				result = Math.round((abstand/Math.tan(Math.toRadians((double)winkel1)))+(abstand/Math.tan(Math.toRadians((double)gamma))));
				b.setText(""+result);
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
		});
		
	}

}
