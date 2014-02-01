package com.cs481.mobilemapper.debug;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.mobilemapper.CommandCenter;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;
import com.cs481.mobilemapper.responses.control.gpio.GPIO;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class DebugActivity extends SpiceActivity {
	
	private String password, ip;
	private GPIO gpio;
	ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);
	}

	@Override
	public void onStart(){
		super.onStart();
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		password = extras.getString("password");
		ip = extras.getString("ip");
		
		TextView header = (TextView) findViewById(R.id.debug_header);
		header.setText(ip+" - "+password);
		
		//Read GPIO config
		readGPIOConfig();
		
	}
	
	private void readGPIOConfig(){
		// perform the request.
		com.cs481.mobilemapper.responses.control.gpio.GetRequest request = new com.cs481.mobilemapper.responses.control.gpio.GetRequest(
				ip, password);
		String lastRequestCacheKey = request.createCacheKey();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Reading GPIO Configuration");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
		
		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ONE_MINUTE, new GPIOGetRequestListener());
	}
	
	public void onLED1GSwitch(View v){
		Log.i(CommandCenter.TAG, "LED1 SWITCH");
		boolean on = ((Switch) v).isChecked();
		if (gpio!=null){
			//int current_state = gpio.getData().getLed_ex1_g();
			gpio.getData().setLed_power((on) ? 1 :0);
			
			// perform the request.
			com.cs481.mobilemapper.responses.control.gpio.PutRequest request = new com.cs481.mobilemapper.responses.control.gpio.PutRequest(
					ip, password, gpio);
			String lastRequestCacheKey = request.createCacheKey();
	
			spiceManager.execute(request, lastRequestCacheKey,
					DurationInMillis.ALWAYS_EXPIRED, new GPIOPutRequestListener());
		}
	}
	
	// inner class of your spiced Activity
	private class GPIOGetRequestListener implements RequestListener<GPIO> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			progressDialog.dismiss();
			Log.i(CommandCenter.TAG, "Failed to read GPIO!");
			Toast.makeText(DebugActivity.this, "Failed to read GPIO configuration", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(GPIO gpio) {
			// update your UI
			progressDialog.dismiss();
			Log.i(CommandCenter.TAG, "Get GPIO Object: " + gpio);
			setGPIO(gpio);
			TextView rail = (TextView) findViewById(R.id.gpio_power);
			rail.setText(Integer.toString(gpio.getData().getLed_power()));
		}
	}
	
	public void setGPIO(GPIO gpio){
		this.gpio = gpio;
	}
	
	// inner class of your spiced Activity
	private class GPIOPutRequestListener implements RequestListener<GPIO> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			Log.i(CommandCenter.TAG, "Command failure!");
		}

		@Override
		public void onRequestSuccess(GPIO gpio) {
			// update your UI
			if (gpio.getData().getException() == null){
				Log.i(CommandCenter.TAG, "Command success!");
				Log.i(CommandCenter.TAG, "Put to GPIO: " + gpio);
			} else {
				Toast.makeText(DebugActivity.this, "GPIO: Server returned exception.", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		   // handle item selection
		   switch (item.getItemId()) {
		      case R.id.debug_refresh:
		    	  readGPIOConfig();
		         return true;
		      default:
		    	  return super.onOptionsItemSelected(item);
		   }
		}
}
