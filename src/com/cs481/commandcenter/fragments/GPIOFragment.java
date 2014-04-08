package com.cs481.commandcenter.fragments;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.responses.GetRequest;
import com.cs481.commandcenter.responses.PutRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.control.gpio.GPIO;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Fragment that allows connected users of a router to
 * change the router's LED light status
 * @author Mike Perez
 */

public class GPIOFragment extends Fragment implements OnRefreshListener,
		OnCheckedChangeListener {
	private static final String CACHEKEY_GPIOGET = "gpio_get";
	private PullToRefreshLayout mPullToRefreshLayout;
	private AuthInfo authInfo;
	private GPIO gpio;
	private boolean shouldLoadData = true; // defaults to true.s
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

		if (savedInstanceState != null) {
			/* save whatever data */
			gpio = savedInstanceState.getParcelable("gpio");

			authInfo = savedInstanceState.getParcelable("authInfo");
			shouldLoadData = savedInstanceState.getBoolean("shouldLoadData",
					true);
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("gpio", gpio);
		outState.putBoolean("shouldLoadData", shouldLoadData);
		outState.putParcelable("authInfo", authInfo);
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
		GetRequest request = new GetRequest(getActivity(), authInfo, "status/gpio",
				GPIO.class, CACHEKEY_GPIOGET);
		String lastRequestCacheKey = request.createCacheKey();

		if (dialog) {
			progressDialog = new ProgressDialog(getActivity(),
					R.style.DialogTheme);
			progressDialog.setMessage(getResources().getString(
					R.string.gpio_reading));
			progressDialog.show();
			progressDialog.setCanceledOnTouchOutside(false);
		}
		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED, new GPIOGetRequestListener());
	}

	public static GPIOFragment newInstance(AuthInfo authInfo) {
		GPIOFragment gpioFrag = new GPIOFragment();
		Bundle args = new Bundle();
		args.putParcelable("authInfo", authInfo);
		gpioFrag.setArguments(args);
		return gpioFrag;
	}

	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.getActionBar().setDisplayShowTitleEnabled(true);
		sa.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		sa.getActionBar().setTitle(getResources().getString(R.string.gpio_title)); // TODO
																	// change to
																	// string
																	// resource
		spiceManager = sa.getSpiceManager();
		if (shouldLoadData) {
			readGPIOConfig(true);
			shouldLoadData = false;
		} else {
			// set the switches
			updateSwitches(); // gpio should exist.
		}

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
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		checking = true;
		readGPIOConfig(false);
	}

	// inner class of your spiced Activity
	private class GPIOGetRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss(); // update your UI
			}
			mPullToRefreshLayout.setRefreshComplete();
			Log.i(CommandCenterActivity.TAG, "Failed to read GPIO!");

			Toast.makeText(getActivity(),
					getResources().getString(R.string.gpio_get_config_failure),
					Toast.LENGTH_SHORT).show();
			checking = false;
		}

		@Override
		public void onRequestSuccess(Response response) {
			GPIO gpio = (GPIO) response.getData();
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss(); // update your UI
			}
			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					setGPIO(gpio);
					updateSwitches();
				} else {
					Toast.makeText(getActivity(),
							response.getResponseInfo().getReason(),
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

	public void updateSwitches() {
		if (gpio != null && getView() != null) {
			// Power LED Green (turns to orange if off)
			Switch lswitch = (Switch) getView().findViewById(
					R.id.powerled_state);
			lswitch.setChecked((gpio.getLed_power() == 1) ? true : false);

			// Wifi Red
			lswitch = (Switch) getView().findViewById(R.id.wifiledr_state);
			lswitch.setChecked((gpio.getLed_wifi_red() == 1) ? false : true);

			// Wifi Green
			lswitch = (Switch) getView().findViewById(R.id.wifiledg_state);
			lswitch.setChecked((gpio.getLed_wifi() == 1) ? false : true);

			// Wifi Blue
			lswitch = (Switch) getView().findViewById(R.id.wifiledb_state);
			lswitch.setChecked((gpio.getLed_wifi_blue() == 1) ? false : true);

			// Signal Strength 0
			lswitch = (Switch) getView().findViewById(R.id.ss0_state);
			lswitch.setChecked((gpio.getLed_ss_0() == 1) ? false : true);

			// Signal Strength 1
			lswitch = (Switch) getView().findViewById(R.id.ss1_state);
			lswitch.setChecked((gpio.getLed_ss_1() == 1) ? false : true);

			// Signal Strength 2
			lswitch = (Switch) getView().findViewById(R.id.ss2_state);
			lswitch.setChecked((gpio.getLed_ss_2() == 1) ? false : true);

			// Signal Strength 3
			lswitch = (Switch) getView().findViewById(R.id.ss3_state);
			lswitch.setChecked((gpio.getLed_ss_3() == 1) ? false : true);

			// USB 1 Red
			lswitch = (Switch) getView().findViewById(R.id.usb1ledr_state);
			lswitch.setChecked((gpio.getLed_usb1_r() == 1) ? false : true);

			// USB 1 Green
			lswitch = (Switch) getView().findViewById(R.id.usb1ledg_state);
			lswitch.setChecked((gpio.getLed_usb1_g() == 1) ? false : true);

			// USB 2 Red
			lswitch = (Switch) getView().findViewById(R.id.usb2ledr_state);
			lswitch.setChecked((gpio.getLed_usb2_r() == 1) ? false : true);

			// USB 2 Green
			lswitch = (Switch) getView().findViewById(R.id.usb2ledg_state);
			lswitch.setChecked((gpio.getLed_usb2_g() == 1) ? false : true);

			// USB 3 Red
			lswitch = (Switch) getView().findViewById(R.id.usb3ledr_state);
			lswitch.setChecked((gpio.getLed_usb3_r() == 1) ? false : true);

			// USB 3 Green
			lswitch = (Switch) getView().findViewById(R.id.usb3ledg_state);
			lswitch.setChecked((gpio.getLed_usb3_g() == 1) ? false : true);

			// USB 1 Red
			lswitch = (Switch) getView().findViewById(R.id.usb1ledr_state);
			lswitch.setChecked((gpio.getLed_usb1_r() == 1) ? false : true);

			// Expansion Bay 1 Red
			lswitch = (Switch) getView().findViewById(
					R.id.expansionbay1ledr_state);
			lswitch.setChecked((gpio.getLed_ex1_r() == 1) ? false : true);

			// Expansion Bay 1 Green
			lswitch = (Switch) getView().findViewById(
					R.id.expansionbay1ledg_state);
			lswitch.setChecked((gpio.getLed_ex1_g() == 1) ? false : true);

			// Expansion Bay 2 Red
			lswitch = (Switch) getView().findViewById(
					R.id.expansionbay2ledr_state);
			lswitch.setChecked((gpio.getLed_ex2_r() == 1) ? false : true);

			// Expansion Bay 2 Green
			lswitch = (Switch) getView().findViewById(
					R.id.expansionbay2ledg_state);
			lswitch.setChecked((gpio.getLed_ex2_g() == 1) ? false : true);
		}
	}

	// inner class of your spiced Activity
	private class GPIONewPutRequestListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			Log.i(CommandCenterActivity.TAG, "Command failure!");
		}

		@Override
		public void onRequestSuccess(Response response) {
			// update your UI. We don't have anything to update right now.

			if (response.getResponseInfo().getSuccess()) {
				GPIO gpio = (GPIO) response.getData();
				if (gpio.getException() == null) {
					// handle here
				} else {
					Toast.makeText(
							getActivity(),
							getResources().getString(R.string.gpio)
									+ getResources().getString(
											R.string.server_exception)
									+ gpio.getException(), Toast.LENGTH_LONG)
							.show();
				}

			} else {
				Toast.makeText(getActivity(),
						response.getResponseInfo().getReason(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (gpio != null && checking == false) {
			switch (buttonView.getId()) {
			case R.id.powerled_state:
				gpio.setLed_power((isChecked) ? 1 : 0);
				break;
			case R.id.wifiledr_state:
				gpio.setLed_wifi_red((isChecked) ? 0 : 1);
				break;
			case R.id.wifiledg_state:
				gpio.setLed_wifi((isChecked) ? 0 : 1);
				break;
			case R.id.wifiledb_state:
				gpio.setLed_wifi_blue((isChecked) ? 0 : 1);
				break;
			case R.id.ss0_state:
				gpio.setLed_ss_0((isChecked) ? 0 : 1);
				break;
			case R.id.ss1_state:
				gpio.setLed_ss_1((isChecked) ? 0 : 1);
				break;
			case R.id.ss2_state:
				gpio.setLed_ss_2((isChecked) ? 0 : 1);
				break;
			case R.id.ss3_state:
				gpio.setLed_ss_3((isChecked) ? 0 : 1);
				break;
			case R.id.usb1ledr_state:
				gpio.setLed_usb1_r((isChecked) ? 0 : 1);
				break;
			case R.id.usb1ledg_state:
				gpio.setLed_usb1_g((isChecked) ? 0 : 1);
				break;
			case R.id.usb2ledr_state:
				gpio.setLed_usb2_r((isChecked) ? 0 : 1);
				break;
			case R.id.usb2ledg_state:
				gpio.setLed_usb2_g((isChecked) ? 0 : 1);
				break;
			case R.id.usb3ledr_state:
				gpio.setLed_usb3_r((isChecked) ? 0 : 1);
				break;
			case R.id.usb3ledg_state:
				gpio.setLed_usb3_g((isChecked) ? 0 : 1);
				break;
			case R.id.expansionbay1ledr_state:
				gpio.setLed_ex1_r((isChecked) ? 0 : 1);
				break;
			case R.id.expansionbay1ledg_state:
				gpio.setLed_ex1_g((isChecked) ? 0 : 1);
				break;
			case R.id.expansionbay2ledr_state:
				gpio.setLed_ex2_r((isChecked) ? 0 : 1);
				break;
			case R.id.expansionbay2ledg_state:
				gpio.setLed_ex2_g((isChecked) ? 0 : 1);
				break;

			default:
				return;
			}

			// perform the request.
			PutRequest request = new PutRequest(getActivity(), gpio, authInfo, "control/gpio",
					com.cs481.commandcenter.responses.control.gpio.GPIO.class);
			String lastRequestCacheKey = request.createCacheKey();

			spiceManager.execute(request, lastRequestCacheKey,
					DurationInMillis.ALWAYS_EXPIRED,
					new GPIONewPutRequestListener());
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.gpio_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			//up navigation
			Log.i(CommandCenterActivity.TAG, "UP in GPIO is being handled.");
			FragmentManager fm = getActivity().getSupportFragmentManager();
			fm.popBackStack();
			return true;
		case R.id.reset_leds:
			// perform the request.
			PutRequest request = new PutRequest(getActivity(), Boolean.valueOf(true), authInfo, "control/led/reset_leds",
					Boolean.class);
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
	private class LEDPutRequestListener implements RequestListener<Response> {
		@Override
		public void onRequestFailure(SpiceException e) {
			Log.i(CommandCenterActivity.TAG, "Failed to reset LEDs!");
			
			if (!isAdded()){
				return;
			}
			/* Toast.makeText(getActivity(),
					getResources().getString(R.string.gpio_get_config_failure),
					Toast.LENGTH_SHORT).show(); */
			Toast.makeText(getActivity(),
					"LED reset request failed.",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response response) {
		
		}
	}
}
