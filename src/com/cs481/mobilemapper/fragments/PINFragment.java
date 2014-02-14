package com.cs481.mobilemapper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;

public class PINFragment extends Fragment implements OnClickListener {
	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		// setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_pin, container,
				false);
		FrameLayout header = (FrameLayout) rootView
				.findViewById(R.id.pinprogress_fragment);
		FrameLayout pinpad = (FrameLayout) rootView
				.findViewById(R.id.pinpad_fragment);

		// Add the subfragment if we need to
		String pinpadId = "pinpad"; // fragment tag
		String pinHeaderId = "pinheader";
		FragmentManager cfm = getChildFragmentManager(); // only works with this
															// fragment
		PINHeaderSubfragment headerFragment = (PINHeaderSubfragment) cfm
				.findFragmentByTag(pinHeaderId);
		PINPadSubfragment pinpadFragment = (PINPadSubfragment) cfm
				.findFragmentByTag(pinpadId);

		// transaction on header
		if (headerFragment == null) {
			headerFragment = new PINHeaderSubfragment();
			FragmentTransaction fmt = cfm.beginTransaction();
			fmt.replace(header.getId(), headerFragment, pinHeaderId);
			fmt.commit();
		}

		// transaction on pinpad
		if (pinpadFragment == null) {
			pinpadFragment = new PINPadSubfragment();
			FragmentTransaction fmt = cfm.beginTransaction();
			fmt.replace(pinpad.getId(), pinpadFragment, pinpadId);
			fmt.commit();
		}

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle("UNLOCK"); // TODO change to string resource
		setupUI();
	}

	public void setupUI() {
		TextView debugPin = (TextView) getView().findViewById(
				R.id.debug_enteredpin);
		debugPin.setText("");
		// Make the numeric buttons have listeners
		Button pinButton = (Button) getView().findViewById(R.id.pin0);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin1);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin2);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin3);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin4);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin5);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin6);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin7);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin8);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin9);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin_backspace);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin_extra);
		pinButton.setOnClickListener(this);

	}

	/*
	 * @Override public void onCreateOptionsMenu(Menu menu, MenuInflater
	 * inflater) { inflater.inflate(R.menu.login_menu, menu); MenuItem item =
	 * menu.findItem(R.id.menu_switchtolocal); item.setVisible(false); //
	 * getActivity().invalidateOptionsMenu(); }
	 * 
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * Log.w(CommandCenterActivity.TAG, "Item was clicked."); // handle item
	 * selection switch (item.getItemId()) { case R.id.fr_debug: Intent intent =
	 * new Intent(getActivity(), DebugActivity.class); CheckBox gateway =
	 * (CheckBox) getView().findViewById( R.id.use_default_gateway); String
	 * routerip = ""; if (gateway.isChecked()) { routerip =
	 * Utility.getDefaultGateway(getActivity()); } else { EditText iptext =
	 * (EditText) getView().findViewById( R.id.router_ip); routerip =
	 * iptext.getText().toString(); }
	 * 
	 * intent.putExtra("ip", routerip); String password = ((EditText)
	 * getView().findViewById( R.id.router_password)).getText().toString();
	 * intent.putExtra("pass", password); intent.putExtra("ecm", false);
	 * intent.putExtra("id", "NOT-ECM-MANAGED"); intent.putExtra("user",
	 * "admin");
	 * 
	 * startActivity(intent); return true; case R.id.menu_switchtoecm:
	 * ECMLoginFragment ecmFragment = new ECMLoginFragment();
	 * 
	 * // In case this activity was started with special instructions from // an
	 * // Intent, pass the Intent's extras to the fragment as arguments //
	 * firstFragment.setArguments(getIntent().getExtras());
	 * 
	 * // Add the fragment to the 'fragment_container' FrameLayout
	 * FragmentTransaction transaction = getActivity()
	 * .getSupportFragmentManager().beginTransaction();
	 * 
	 * transaction.replace(R.id.login_fragment, ecmFragment);
	 * transaction.commit(); return true; case R.id.menu_preenter_ip: EditText
	 * iptext = (EditText) getView().findViewById( R.id.router_ip);
	 * iptext.setText("132.178.226.103"); return true; default: return
	 * super.onOptionsItemSelected(item); } }
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		TextView debugPin = (TextView) getView().findViewById(
				R.id.debug_enteredpin);
		String currentPin = debugPin.getText().toString();
		
		int buttonId = v.getId();
		
		if (currentPin.length() >= 4 && buttonId != R.id.pin_backspace) {
			return;
		}
		
		switch (v.getId()) {
		case R.id.pin0:
			currentPin += "0";
			break;
		case R.id.pin1:
			currentPin += "1";
			break;
		case R.id.pin2:
			currentPin += "2";
			break;
		case R.id.pin3:
			currentPin += "3";
			break;
		case R.id.pin4:
			currentPin += "4";
			break;
		case R.id.pin5:
			currentPin += "5";
			break;
		case R.id.pin6:
			currentPin += "6";
			break;
		case R.id.pin7:
			currentPin += "7";
			break;
		case R.id.pin8:
			currentPin += "8";
			break;
		case R.id.pin9:
			currentPin += "9";
			break;
		case R.id.pin_backspace:
			Log.i(CommandCenterActivity.TAG, "BACKSPACE");
			if (currentPin.length() > 0) {
				currentPin = currentPin.substring(0, currentPin.length() - 1);
			}
			break;
		}

		debugPin.setText(currentPin);
	}

}
