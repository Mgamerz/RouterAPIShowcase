package com.cs481.mobilemapper.debug;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cs481.mobilemapper.CommandCenter;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;
import com.cs481.mobilemapper.responses.control.gpio.GPIO;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class DebugActivity extends SpiceActivity {
	
	private String password, ip;
	
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
	}
	
	
	// inner class of your spiced Activity
	private class GetRequestListener implements RequestListener<GPIO> {

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
