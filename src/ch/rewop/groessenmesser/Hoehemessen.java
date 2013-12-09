package ch.rewop.groessenmesser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Hoehemessen extends Activity {
	private static final int SCAN_QR_CODE_REQUEST_CODE = 0;
	
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
		MenuItem menuItem = menu.add("In Logbuch eintragen");
		menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, SCAN_QR_CODE_REQUEST_CODE);
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == SCAN_QR_CODE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				String qrCode = intent.getStringExtra("SCAN_RESULT");
				sendlog(qrCode);
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_hoehenmessen);  
		
		Bundle extras = getIntent().getExtras();
		winkel1 = extras.getFloat("winkel1");
		winkel2 = extras.getFloat("winkel2");
		gamma = 180 - (winkel1+winkel2);
		
		alpha = (TextView)findViewById(R.id.alpha);
		alpha.setText(""+(int)winkel1);
		beta = (TextView)findViewById(R.id.beta);
		beta.setText(""+(int)winkel2);
		b = (TextView)findViewById(R.id.b);
		a = (EditText)findViewById(R.id.a);
		a.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				try{
					abstand = Double.parseDouble(a.getText().toString());
					result = Math.round((abstand/Math.tan(Math.toRadians((double)winkel1)))+(abstand/Math.tan(Math.toRadians((double)gamma))));
					b.setText(""+result);
				}catch(Exception e){
					Log.e(INPUT_SERVICE, e.toString());
					b.setText("");
				}
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
	
	//eintrag in logbuch
		private void sendlog(String qrCode) {
			Intent intent = new Intent("ch.appquest.intent.LOG");
			 
			if (getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
				Toast.makeText(this, "Logbook App not Installed", Toast.LENGTH_LONG).show();
			return;
			}
			
			TextView codeText = (TextView) findViewById(R.id.b);
			String code = codeText.getText().toString();
			 
			intent.putExtra("ch.appquest.taskname", "REWOP.Groessen-Messer");
			intent.putExtra("ch.appquest.logmessage", qrCode + ": " + code);
			 
			startActivity(intent);
		}

}
