package com.cs481.mobilemapper.fragments;

import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;

public class PINFragment extends Fragment implements OnClickListener {
	String currentPin = ""; // pin that has been currently entered

	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		if (savedInstancedState != null) {
			currentPin = savedInstancedState.getString("pin");
		}
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
		if (!currentPin.equals("")) {
			updateProgress(currentPin);
		}
	}

	public void setupUI() {
		TextView debugPin = (TextView) getView().findViewById(
				R.id.debug_enteredpin);
		debugPin.setText(currentPin);

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		TextView debugPin = (TextView) getView().findViewById(
				R.id.debug_enteredpin);

		int buttonId = v.getId();
		//int preAddedLength = currentPin.length();
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
		updateProgress(currentPin);
		debugPin.setText(currentPin);
	}

	/**
	 * Updates the circle shape color to make it appear that it has indeed been
	 * entered already.
	 * 
	 * @param pin
	 *            current pin (it is not read - only the length matters)
	 */
	private void updateProgress(String pin) {
		ImageView progressPos = null;
		for (int i = 1; i < 4; i++) {
			GradientDrawable circleIcon = (GradientDrawable) getActivity()
					.getResources().getDrawable(R.drawable.pin_circle);
			if (i <= pin.length()) {
				Log.i(CommandCenterActivity.TAG, "i vs pin: "+i+" "+pin.length());
				// lightens the image
				circleIcon.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
			}
			switch (i) {
			// in a for loop, update each of the pin icons to the new value.
			case 1:
				progressPos = (ImageView) getView().findViewById(
						R.id.pinprogress1);
				break;
			case 2:
				progressPos = (ImageView) getView().findViewById(
						R.id.pinprogress2);
				break;
			case 3:
				progressPos = (ImageView) getView().findViewById(
						R.id.pinprogress3);
				break;
			case 4:
				progressPos = (ImageView) getView().findViewById(
						R.id.pinprogress4);
				break;
			}
			progressPos.setImageDrawable(circleIcon);
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("pin", currentPin);
	}
}
