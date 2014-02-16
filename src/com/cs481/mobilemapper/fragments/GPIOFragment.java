package com.cs481.mobilemapper.fragments;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;
import com.cs481.mobilemapper.responses.control.gpio.GPIO;
import com.cs481.mobilemapper.responses.control.led.LED;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class GPIOFragment extends Fragment implements OnRefreshListener,
		OnCheckedChangeListener {
	private PullToRefreshLayout mPullToRefreshLayout;
	private AuthInfo authInfo;
	private GPIO gpio;
	private boolean checking = true; // Flag for a refresh taking place. First
										// time it will be true as we don't want
										// to 'update' the switches
										// and have them immediately change
										// stuff when they are updated.
	ProgressDialog progressDialog;
	private SpiceManager spiceManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_gpio, container,
				false);
		return rootView;
	}

	private void readGPIOConfig(boolean dialog) {
		// perform the request.
		com.cs481.mobilemapper.responses.status.gpio.GetRequest request = new com.cs481.mobilemapper.responses.status.gpio.GetRequest(
				((CommandCenterActivity) getActivity()).getAuthInfo());
		String lastRequestCacheKey = request.createCacheKey();

		if (dialog) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage(getResources().getString(
					R.string.gpio_reading));
			progressDialog.show();
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
		}
		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED, new GPIOGetRequestListener());
	}

	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle(getResources().getString(R.string.gpio_title)); // TODO
																	// change to
																	// string
																	// resource
		spiceManager = sa.getSpiceManager();
		readGPIOConfig(true);

		// Now find the PullToRefreshLayout to setup
		mPullToRefreshLayout = (PullToRefreshLayout) getView().findViewById(
				R.id.ptr_layout);

		setupListeners();
	}

	/**
	 * Sets up the listeners that this fragment will display. For this fragment,
	 * it is the set of switches. We have to define them in a fragment because
	 * defining them in XML delivers them to the parent activity, which will
	 * have a difficult time maintaining the reference to this fragment at all
	 * times without memory leaks.
	 */
	private void setupListeners() {
		// TODO Auto-generated method stub
		// Now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity())
		// Mark All Children as pullable
				.allChildrenArePullable()
				// Set the OnRefreshListener
				.listener(this)
				// Finally commit the setup to our PullToRefreshLayout
				.setup(mPullToRefreshLayout);

		Switch toggle = (Switch) getView().findViewById(R.id.powerled_state);
		toggle.setOnCheckedChangeListener(this);

		// Power LED (Orange/Green)
		toggle = (Switch) getView().findViewById(R.id.powerled_state);
		toggle.setOnCheckedChangeListener(this);

		// Red Wifi LED
		toggle = (Switch) getView().findViewById(R.id.wifiledr_state);
		toggle.setOnCheckedChangeListener(this);

		// Green Wifi LED
		toggle = (Switch) getView().findViewById(R.id.wifiledg_state);
		toggle.setOnCheckedChangeListener(this);

		// Blue Wifi LED
		toggle = (Switch) getView().findViewById(R.id.wifiledb_state);
		toggle.setOnCheckedChangeListener(this);

		// Signal Strength 0
		toggle = (Switch) getView().findViewById(R.id.ss0_state);
		toggle.setOnCheckedChangeListener(this);

		// Signal Strength 1
		toggle = (Switch) getView().findViewById(R.id.ss1_state);
		toggle.setOnCheckedChangeListener(this);

		// Signal Strength 2
		toggle = (Switch) getView().findViewById(R.id.ss2_state);
		toggle.setOnCheckedChangeListener(this);

		// Signal Strength 3
		toggle = (Switch) getView().findViewById(R.id.ss3_state);
		toggle.setOnCheckedChangeListener(this);

		// Red USB 1 LED
		toggle = (Switch) getView().findViewById(R.id.usb1ledr_state);
		toggle.setOnCheckedChangeListener(this);

		// Green USB 1 LED
		toggle = (Switch) getView().findViewById(R.id.usb1ledg_state);
		toggle.setOnCheckedChangeListener(this);

		// Red USB 2 LED
		toggle = (Switch) getView().findViewById(R.id.usb2ledr_state);
		toggle.setOnCheckedChangeListener(this);

		// Green USB 2 LED
		toggle = (Switch) getView().findViewById(R.id.usb2ledg_state);
		toggle.setOnCheckedChangeListener(this);
		
		// Red USB 3 LED
		toggle = (Switch) getView().findViewById(R.id.usb3ledr_state);
		toggle.setOnCheckedChangeListener(this);

		// Green USB 3 LED
		toggle = (Switch) getView().findViewById(R.id.usb3ledg_state);
		toggle.setOnCheckedChangeListener(this);
		
		// Red Expansion Bay 1 LED
		toggle = (Switch) getView().findViewById(R.id.expansionbay1ledr_state);
		toggle.setOnCheckedChangeListener(this);
		
		// Green Expansion Bay 1 LED
		toggle = (Switch) getView().findViewById(R.id.expansionbay1ledg_state);
		toggle.setOnCheckedChangeListener(this);
		
		// Red Expansion Bay 2 LED
		toggle = (Switch) getView().findViewById(R.id.expansionbay2ledr_state);
		toggle.setOnCheckedChangeListener(this);
		
		// Green Expansion Bay 2 LED
		toggle = (Switch) getView().findViewById(R.id.expansionbay2ledg_state);
		toggle.setOnCheckedChangeListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		authInfo = ((CommandCenterActivity) getActivity()).getAuthInfo();
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		checking = true;
		readGPIOConfig(false);
	}

	// inner class of your spiced Activity
	private class GPIOGetRequestListener implements RequestListener<GPIO> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			progressDialog.dismiss();
			mPullToRefreshLayout.setRefreshComplete();
			Log.i(CommandCenterActivity.TAG, "Failed to read GPIO!");

			Toast.makeText(getActivity(),
					getResources().getString(R.string.gpio_get_config_failure),
					Toast.LENGTH_SHORT).show();
			checking = false;
		}

		@Override
		public void onRequestSuccess(GPIO gpio) {
			// update your UI
			progressDialog.dismiss(); // update your UI
			if (gpio != null) {
				System.out.println("DEBUG POIN");
				if (gpio.getSuccess()) {
					setGPIO(gpio);

					// Power LED Green (turns to orange if off)
					Switch lswitch = (Switch) getView().findViewById(
							R.id.powerled_state);
					lswitch.setChecked((gpio.getData().getLed_power() == 1) ? true
							: false);

					// Wifi Red
					lswitch = (Switch) getView().findViewById(
							R.id.wifiledr_state);
					lswitch.setChecked((gpio.getData().getLed_wifi_red() == 1) ? true
							: false);

					// Wifi Green
					lswitch = (Switch) getView().findViewById(
							R.id.wifiledg_state);
					lswitch.setChecked((gpio.getData().getLed_wifi() == 1) ? false
							: true);

					// Wifi Blue
					lswitch = (Switch) getView().findViewById(
							R.id.wifiledb_state);
					lswitch.setChecked((gpio.getData().getLed_wifi_blue() == 1) ? true
							: false);

					// Signal Strength 0
					lswitch = (Switch) getView().findViewById(R.id.ss0_state);
					lswitch.setChecked((gpio.getData().getLed_ss_0() == 1) ? false
							: true);

					// Signal Strength 1
					lswitch = (Switch) getView().findViewById(R.id.ss1_state);
					lswitch.setChecked((gpio.getData().getLed_ss_1() == 1) ? false
							: true);
					
					// Signal Strength 2
					lswitch = (Switch) getView().findViewById(R.id.ss2_state);
					lswitch.setChecked((gpio.getData().getLed_ss_2() == 1) ? false
							: true);
					
					// Signal Strength 3
					lswitch = (Switch) getView().findViewById(R.id.ss3_state);
					lswitch.setChecked((gpio.getData().getLed_ss_3() == 1) ? false
							: true);
					
					// USB 1 Red
					lswitch = (Switch) getView().findViewById(R.id.usb1ledr_state);
					lswitch.setChecked((gpio.getData().getLed_usb1_r() == 1) ? false
							: true);
					
					// USB 1 Green
					lswitch = (Switch) getView().findViewById(R.id.usb1ledg_state);
					lswitch.setChecked((gpio.getData().getLed_usb1_g() == 1) ? false
							: true);
					
					// USB 2 Red
					lswitch = (Switch) getView().findViewById(R.id.usb2ledr_state);
					lswitch.setChecked((gpio.getData().getLed_usb2_r() == 1) ? false
							: true);
					
					// USB 2 Green
					lswitch = (Switch) getView().findViewById(R.id.usb2ledg_state);
					lswitch.setChecked((gpio.getData().getLed_usb2_g() == 1) ? false
							: true);
					
					// USB 3 Red
					lswitch = (Switch) getView().findViewById(R.id.usb3ledr_state);
					lswitch.setChecked((gpio.getData().getLed_usb3_r() == 1) ? false
							: true);
					
					// USB 3 Green
					lswitch = (Switch) getView().findViewById(R.id.usb3ledg_state);
					lswitch.setChecked((gpio.getData().getLed_usb3_g() == 1) ? false
							: true);
					
					// USB 1 Red
					lswitch = (Switch) getView().findViewById(R.id.usb1ledr_state);
					lswitch.setChecked((gpio.getData().getLed_usb1_r() == 1) ? false
							: true);
					
					// Expansion Bay 1 Red
					lswitch = (Switch) getView().findViewById(R.id.expansionbay1ledr_state);
					lswitch.setChecked((gpio.getData().getLed_ex1_r() == 1) ? false
							: true);
					
					// Expansion Bay 1 Green
					lswitch = (Switch) getView().findViewById(R.id.expansionbay1ledg_state);
					lswitch.setChecked((gpio.getData().getLed_ex1_g() == 1) ? false
							: true);
					
					// Expansion Bay 2 Red
					lswitch = (Switch) getView().findViewById(R.id.expansionbay2ledr_state);
					lswitch.setChecked((gpio.getData().getLed_ex2_r() == 1) ? false
							: true);
					
					// Expansion Bay 2 Green
					lswitch = (Switch) getView().findViewById(R.id.expansionbay2ledg_state);
					lswitch.setChecked((gpio.getData().getLed_ex2_g() == 1) ? false
							: true);
				} else {
					Toast.makeText(getActivity(), gpio.getReason(),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						getActivity(),
						getResources().getString(
								R.string.gpio_get_null_response),
						Toast.LENGTH_LONG).show();
			}
			mPullToRefreshLayout.setRefreshComplete();
			checking = false;

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
			Log.i(CommandCenterActivity.TAG, "Command failure!");
		}

		@Override
		public void onRequestSuccess(GPIO gpio) {
			// update your UI
			if (gpio.getSuccess()) {
				if (gpio.getData().getException() == null) {
					Log.i(CommandCenterActivity.TAG, "Command success!");
					Log.i(CommandCenterActivity.TAG, "Put to GPIO: " + gpio);
					// DebugGPIOFragment.this.gpio = gpio;
				} else {
					Toast.makeText(
							getActivity(),
							getResources().getString(R.string.gpio)
									+ getResources().getString(
											R.string.server_exception)
									+ gpio.getData().getException(),
							Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(getActivity(), gpio.getReason(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (gpio != null && checking == false) {
			Log.i(CommandCenterActivity.TAG,"Check changed");
			switch (buttonView.getId()) {
			case R.id.powerled_state:
				gpio.getData().setLed_power((isChecked) ? 1 : 0);
				break;
			case R.id.wifiledr_state:
				gpio.getData().setLed_wifi_red((isChecked) ? 1 : 0);
				break;
			case R.id.wifiledg_state:
				gpio.getData().setLed_wifi((isChecked) ? 0 : 1);
				break;
			case R.id.wifiledb_state:
				gpio.getData().setLed_wifi_blue((isChecked) ? 1 : 0);
				break;
			case R.id.ss0_state:
				gpio.getData().setLed_ss_0((isChecked) ? 0 : 1);
				break;
			case R.id.ss1_state:
				gpio.getData().setLed_ss_1((isChecked) ? 0 : 1);
				break;
			case R.id.ss2_state:
				gpio.getData().setLed_ss_2((isChecked) ? 0 : 1);
				break;
			case R.id.ss3_state:
				gpio.getData().setLed_ss_3((isChecked) ? 0 : 1);
				break;
			case R.id.usb1ledr_state:
				gpio.getData().setLed_usb1_r((isChecked) ? 0 : 1);
				break;
			case R.id.usb1ledg_state:
				gpio.getData().setLed_usb1_g((isChecked) ? 0 : 1);
				break;
			case R.id.usb2ledr_state:
				gpio.getData().setLed_usb2_r((isChecked) ? 0 : 1);
				break;
			case R.id.usb2ledg_state:
				gpio.getData().setLed_usb2_g((isChecked) ? 0 : 1);
				break;
			case R.id.usb3ledr_state:
				gpio.getData().setLed_usb3_r((isChecked) ? 0 : 1);
				break;
			case R.id.usb3ledg_state:
				gpio.getData().setLed_usb3_g((isChecked) ? 0 : 1);
				break;
			case R.id.expansionbay1ledr_state:
				gpio.getData().setLed_ex1_r((isChecked) ? 0 : 1);
				break;
			case R.id.expansionbay1ledg_state:
				gpio.getData().setLed_ex1_g((isChecked) ? 0 : 1);
				break;
			case R.id.expansionbay2ledr_state:
				gpio.getData().setLed_ex2_r((isChecked) ? 0 : 1);
				break;
			case R.id.expansionbay2ledg_state:
				gpio.getData().setLed_ex2_g((isChecked) ? 0 : 1);
				break;

			default:
				return;
			}

			// perform the request.
			com.cs481.mobilemapper.responses.control.gpio.PutRequest request = new com.cs481.mobilemapper.responses.control.gpio.PutRequest(
					authInfo, gpio);
			String lastRequestCacheKey = request.createCacheKey();

			spiceManager.execute(request, lastRequestCacheKey,
					DurationInMillis.ALWAYS_EXPIRED,
					new GPIOPutRequestListener());
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.gpio_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.w(CommandCenterActivity.TAG, "Item was clicked.");
		// handle item selection
		switch (item.getItemId()) {
		case R.id.reset_leds:
			// perform the request.
			com.cs481.mobilemapper.responses.control.led.PutRequest request = new com.cs481.mobilemapper.responses.control.led.PutRequest(
					authInfo);
			String lastRequestCacheKey = request.createCacheKey();

			spiceManager.execute(request, lastRequestCacheKey,
					DurationInMillis.ALWAYS_EXPIRED,
					new LEDPutRequestListener());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// inner class of your spiced Activity
	private class LEDPutRequestListener implements RequestListener<LED> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			Log.i(CommandCenterActivity.TAG, "Command failure!");
		}

		@Override
		public void onRequestSuccess(LED led) {
			// update your UI
			if (led.getData().getException() == null) {
				Log.i(CommandCenterActivity.TAG, "Command success!");
				Log.i(CommandCenterActivity.TAG, "Put to LED: " + led);
			} else {
				Toast.makeText(
						getActivity(),
						"GPIO: Server returned exception: "
								+ led.getData().getException(),
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
