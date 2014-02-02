 package com.cs481.mobilemapper;

import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cs481.mobilemapper.debug.DebugActivity;
import com.cs481.mobilemapper.responses.status.wlan.Wlan;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class FirstRunActivity extends SpiceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firstrun);
	}

	// inner class of your spiced Activity
	private class GetRequestListener implements RequestListener<Wlan> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			Log.i(CommandCenter.TAG, "Command failure!");
		}

		@Override
		public void onRequestSuccess(Wlan wlan) {
			// update your UI
			Log.i(CommandCenter.TAG, "Command success!");
			Log.i(CommandCenter.TAG, "Got WLAN object: " + wlan);
		}
	}

	public void defaultGatewayChange(View v) {
		CheckBox cb = (CheckBox) v;
		Log.i(CommandCenter.TAG, "Default gateway checkbox");
		EditText customip = (EditText) findViewById(R.id.router_ip);
		if (!cb.isChecked()){
			Log.i(CommandCenter.TAG, "Showing box.");
			customip.setVisibility(EditText.VISIBLE);
		} else {
			Log.i(CommandCenter.TAG, "Hiding box.");
			customip.setVisibility(EditText.GONE);
		}
	}

	/**
	 * this method is called when the connect button is clicked. It is defined
	 * as a listener in the android:onClick element of the fragment XML.
	 * 
	 * @param v
	 *            View that called this (the fragment)
	 */
	public void connectClick(View v) {
	  	Intent intent = new Intent(this, CommandCenter.class);
	  	startActivity(intent);
	  	finish();
		
		//Debugging stuff
		/*
		CheckBox gateway = (CheckBox) findViewById(R.id.use_default_gateway);
		if (gateway != null){
			String routerip = "";
			if (gateway.isChecked()) {
				final WifiManager manager = (WifiManager) super
						.getSystemService(WIFI_SERVICE);
				final DhcpInfo dhcp = manager.getDhcpInfo();
				// This method is deprecated because we use IPv4 and the new one is IPv6. IPv6 is way more complicated and used in more
				// telecom related things (public facing things). It still works so ignore the deprecation.
				routerip = Formatter.formatIpAddress(dhcp.gateway); 
			} else {
				EditText iptext = (EditText) findViewById(R.id.router_ip);
				routerip = iptext.getText().toString();
			}
	
			Log.i(CommandCenter.TAG, "Routerip is: " + routerip);
	
			// perform the request.
			com.cs481.mobilemapper.responses.status.wlan.GetRequest request = new com.cs481.mobilemapper.responses.status.wlan.GetRequest(
					routerip);
			String lastRequestCacheKey = request.createCacheKey();
	
			spiceManager.execute(request, lastRequestCacheKey,
					DurationInMillis.ONE_MINUTE, new GetRequestListener()); 
		} */
	}
}
