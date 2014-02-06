package com.cs481.mobilemapper.debug;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class DebugActivity extends SpiceActivity implements OnRefreshListener {

	private PullToRefreshLayout mPullToRefreshLayout;
	private String password, ip;
	private GPIO gpio;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);
		// Now find the PullToRefreshLayout to setup
		mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);

		// Now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(this)
		// Mark All Children as pullable
				.allChildrenArePullable()
				// Set the OnRefreshListener
				.listener(this)
				// Finally commit the setup to our PullToRefreshLayout
				.setup(mPullToRefreshLayout);
	}

	@Override
	public void onStart() {
		super.onStart();
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		password = extras.getString("password");
		ip = extras.getString("ip");

		TextView header = (TextView) findViewById(R.id.debug_header);
		header.setText(ip + " - " + password);

		// Read GPIO config
		readGPIOConfig(true);

	}

	private void readGPIOConfig(boolean dialog) {
		// perform the request.
		com.cs481.mobilemapper.responses.control.gpio.GetRequest request = new com.cs481.mobilemapper.responses.control.gpio.GetRequest(
				ip, password);
		String lastRequestCacheKey = request.createCacheKey();

		if (dialog) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("Reading GPIO Configuration");
			progressDialog.show();
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
		}

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED, new GPIOGetRequestListener());
		/*
		 * gpio=new GPIO(); gpio.setData(new Data());
		 * gpio.getData().setLed_power(1);
		 */
	}

	public void onSwitchChange(View v) {
		
		boolean on = ((Switch) v).isChecked();
		if (gpio != null) {
			switch(v.getId()){
			case R.id.powerled_state:
				gpio.getData().setLed_power((on) ? 1 : 0);
				break;
			default:
				return;
			}

			// perform the request.
			com.cs481.mobilemapper.responses.control.gpio.PutRequest request = new com.cs481.mobilemapper.responses.control.gpio.PutRequest(
					ip, password, gpio);
			String lastRequestCacheKey = request.createCacheKey();

			spiceManager.execute(request, lastRequestCacheKey,
					DurationInMillis.ALWAYS_EXPIRED,
					new GPIOPutRequestListener());
		}
	}

	// inner class of your spiced Activity
	private class GPIOGetRequestListener implements RequestListener<GPIO> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			progressDialog.dismiss();
            mPullToRefreshLayout.setRefreshComplete();
			Log.i(CommandCenter.TAG, "Failed to read GPIO!");
			Toast.makeText(DebugActivity.this,
					"Failed to read GPIO configuration", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onRequestSuccess(GPIO gpio) {
			// update your UI
			progressDialog.dismiss();
			Log.i(CommandCenter.TAG, "Get GPIO Object: " + gpio);
			setGPIO(gpio);

			// Set teh switches
			// Power LED Green (turns to orange if off)
			Switch lswitch = (Switch) findViewById(R.id.powerled_state);
			lswitch.setChecked((gpio.getData().getLed_power() == 1) ? true
					: false);

			// Set teh switches
			lswitch = (Switch) findViewById(R.id.wifiledg_state);
			lswitch.setChecked((gpio.getData().getLed_wifi() == 1) ? true
					: false);

			// Set teh switches
			Switch powerswitch = (Switch) findViewById(R.id.wifiledr_state);
			powerswitch
					.setChecked((gpio.getData().getLed_wifi_red() == 1) ? true
							: false);
            mPullToRefreshLayout.setRefreshComplete();

		}
	}

	public void setGPIO(GPIO gpio) {
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
			if (gpio.getData().getException() == null) {
				Log.i(CommandCenter.TAG, "Command success!");
				Log.i(CommandCenter.TAG, "Put to GPIO: " + gpio);
			} else {
				Toast.makeText(
						DebugActivity.this,
						"GPIO: Server returned exception: "
								+ gpio.getData().getException(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		readGPIOConfig(false);
	}
}
