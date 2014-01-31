package com.cs481.mobilemapper.debug;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
			Log.i(CommandCenter.TAG, "Command success!");
			Log.i(CommandCenter.TAG, "Put to GPIO: " + gpio);
		}
	}
}
