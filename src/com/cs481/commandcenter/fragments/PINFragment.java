package com.cs481.commandcenter.fragments;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.Cryptography;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.activities.CommandCenterActivity;

/**
 * Fragment that contains PIN header and PIN pad fragments
 * and handles inputs from these fragments
 * @author Mike Perez
 */

public class PINFragment extends Fragment implements OnClickListener {
	//private static final Bundle savedInstancedState = null;
	String currentPin = "", verifyPin = ""; // pin that has been currently entered
	int attemptsRemaining = 5; // TODO this should be set in SharedPreferences
								// with a timestamp for a cooldown on attempts.
								// Not sure how to do that.
	boolean isTimerRunning = false;
	boolean pinsetup, verify = false; // verify stage - "Enter again to verify"

	public final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";

	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		Bundle args = getArguments();
		pinsetup = args.getBoolean("createpin");
		if (savedInstancedState != null) {
			currentPin = savedInstancedState.getString("pin");
			attemptsRemaining = savedInstancedState.getInt("attemptsRemaining");
			verify = savedInstancedState.getBoolean("verify");
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		ActionBar ab = getActivity().getActionBar();
		ab.hide();
	}

	/**
	 * Use this to create a new fragment of PINFragment. Do not use the empty
	 * constructor. We have to do this because of the kinda stupid way android
	 * handles memory and device rotation (we need to setup the UI before the
	 * user can see it)
	 * 
	 * @param createPIN
	 *            If in pin setup mode, set this to true. Otherwise it's in
	 *            decrypt mode
	 * @return
	 */
	public static PINFragment newInstance(boolean createPIN) {
		PINFragment pinFragment = new PINFragment();

		Bundle args = new Bundle();
		Log.i(CommandCenterActivity.TAG, "CP: "+createPIN);
		args.putBoolean("createpin", createPIN);
		pinFragment.setArguments(args);
		return pinFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_pin, container,
				false);
		if (savedInstanceState == null) {
			FrameLayout header = (FrameLayout) rootView
					.findViewById(R.id.pinprogress_fragment);
			FrameLayout pinpad = (FrameLayout) rootView
					.findViewById(R.id.pinpad_fragment);

			// Add the subfragment if we need to
			String pinpadId = "pinpad"; // fragment tag
			String pinHeaderId = "pinheader";
			FragmentManager cfm = getChildFragmentManager(); // only works with
																// this
																// fragment
			PINHeaderSubfragment headerFragment = (PINHeaderSubfragment) cfm
					.findFragmentByTag(pinHeaderId);
			PINPadSubfragment pinpadFragment = (PINPadSubfragment) cfm
					.findFragmentByTag(pinpadId);

			// transaction on header
			if (headerFragment == null) {
				String directions = getActivity().getResources().getString(R.string.pin_enter);
				if (pinsetup){
					directions = getActivity().getResources().getString(R.string.pin_create);
				}
				headerFragment = PINHeaderSubfragment.newInstance(directions);
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
		}
		return rootView;
	}

	/**
	 * If the PIN is incorrectly input, the screen shakes and the pad remains locked.
	 */
	public void wrongPIN() {
		Vibrator vibr = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(150);
		attemptsRemaining--;
		LinearLayout pinProgress = (LinearLayout) getView().findViewById(
				R.id.pinprogress_layout);
		Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.animation_shake);
		pinProgress.startAnimation(shakeAnimation);
		currentPin = "";
		unlockPad(false);
		resetTimer();
	}

	@Override
	public void onStart() {
		super.onStart();
		setupUI();
		
		if (!currentPin.equals("")) {
			updateProgress(currentPin);
		}
	}
	
	public void setupUI() {
		//TextView debugPin = (TextView) getView().findViewById(
		//		R.id.debug_enteredpin);
		//debugPin.setText(currentPin);

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
		//TextView debugPin = (TextView) getView().findViewById(
		//		R.id.debug_enteredpin);

		int buttonId = v.getId();
		
		Vibrator vibr = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(45);
		
		// Prevent entering a pin over 4 characters long (if its over 3, the pin
		// count is full
		if (currentPin.length() > 3 && buttonId != R.id.pin_backspace) {
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
			if (currentPin.length() > 0) {
				currentPin = currentPin.substring(0, currentPin.length() - 1);
			}
			break;
		}
		updateProgress(currentPin);
		//debugPin.setText(currentPin);
	}

	/**
	 * Updates the circle shape color to make it appear that it has indeed been
	 * entered already.
	 * 
	 * @param pin
	 *            current pin (it is not read - only the length matters)
	 */
	private void updateProgress(String pin) {
		if (!isAdded()) {
			return; //screen was rotated.
		}
		ImageView progressPos = null;
		for (int i = 1; i < 5; i++) {
			GradientDrawable circleIcon = (GradientDrawable) getActivity()
					.getResources().getDrawable(R.drawable.pin_circle);
			if (i <= pin.length()) {
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

		if (currentPin.length() == 4) {
			pinEntryComplete();
			// Debugging - simulate a bad pin.
			// wrongPIN();
		}
	}



	private void testEncryptionDecryption(String hash) {
		try {
			String uuid = Cryptography.createLocalUUID(getActivity());
			SecretKey secret = Cryptography.generateKey(currentPin,
					uuid.getBytes("UTF-8"));
			byte[] encrypted = Cryptography.encryptMsg(uuid, secret);
			Log.i(CommandCenterActivity.TAG, "Encrypted to: "
					+ new String(encrypted, "UTF-8"));
			String result = Cryptography.decryptMsg(encrypted, secret);
			Log.i(CommandCenterActivity.TAG, "Decrypted back to: " + result);
		} catch (Exception e) {
			// too many exceptions to catch.
			e.printStackTrace();
		}
	}

	/**
	 * Unlocks and locks all keys on the entry pad.
	 * 
	 * @param lock
	 *            True will unlock all elements in the pad allowing entry. False
	 *            will disable all items.
	 */
	private void unlockPad(boolean lock) {
		Button pinButton = (Button) getView().findViewById(R.id.pin0);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin1);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin2);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin3);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin4);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin5);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin6);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin7);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin8);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin9);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin_backspace);
		pinButton.setEnabled(lock);
		pinButton = (Button) getView().findViewById(R.id.pin_extra);
		pinButton.setEnabled(lock);
	}

	/**
	 * This method checks if the entered PIN will be able to unlock (hopefully)
	 * data encrypted with it by attempting to
	 * 
	 * @return
	 */
	private boolean testUnlockPin() {
		String uuid = Cryptography.createLocalUUID(getActivity()); // create a local-device only version
											// so the app can't move to
											// different devices.
		SharedPreferences crypto = getActivity().getSharedPreferences(
				getResources().getString(R.string.crypto_prefsdb),
				Context.MODE_PRIVATE);
		String verify = crypto.getString("validator", "FAILURE"); // this should turn
															// into the uuid
															// stored in the
															// prefs.
		Log.i(CommandCenterActivity.TAG, "Reading stored validation token: "+verify);
		try {

			//SecretKey secret = Cryptography.generateKey(currentPin,
			//		uuid.getBytes("UTF-8"));
			//byte[] encrypted = Cryptography.encryptMsg(uuid, secret);
			
			Log.w(CommandCenterActivity.TAG, "Unlocking: "+currentPin+" with salt "+uuid);
			SecretKey secret = Cryptography.generateKey(currentPin,
					uuid.getBytes("UTF-8"));
			String result = Cryptography.decryptMsg(Base64.decode(verify, Base64.DEFAULT),
					secret);
			Log.i(CommandCenterActivity.TAG, "Decrypted back to: " + result);
			//Log.i(CommandCenterActivity.TAG, "Compare against: "+crypto.getString("uuid", "FAILURE"));
			if (result.equals(Cryptography.createLocalUUID(getActivity()))) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidParameterSpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Saves a verification object to the SharedPreferences that allow us to
	 * validate a PIN. It does not store the PIN on the device.
	 */
	private void savePIN() {
		SharedPreferences crypto = getActivity().getSharedPreferences(
				getResources().getString(R.string.crypto_prefsdb),
				Context.MODE_PRIVATE);
		String uuid = Cryptography.createLocalUUID(getActivity());
		if (uuid != null) {
			try {
				SecretKey secret = Cryptography.generateKey(currentPin,
						uuid.getBytes("UTF-8"));
				Log.w(CommandCenterActivity.TAG, "Unlocking: "+currentPin+" with salt "+uuid);

				byte[] encrypted = Cryptography.encryptMsg(uuid, secret);
				//Log.i(CommandCenterActsivity.TAG, "Encrypted to: "
				//		+ new String(encrypted, "UTF-8"));
				SharedPreferences.Editor editor = crypto.edit();
				String putdata = Base64.encodeToString(encrypted, Base64.DEFAULT);
				Log.i(CommandCenterActivity.TAG, "B64-E: "+putdata);
				editor.putString("validator", putdata);
				// Commit the edits!
				editor.commit();
				
				//Test decrypting the validator object
				String validation = crypto.getString("validator", "FAILURE");
				String result = Cryptography.decryptMsg(Base64.decode(validation, Base64.DEFAULT), secret);
				
				Log.i(CommandCenterActivity.TAG, "Decrypted to: "+result);
				
			} catch (Exception e) {
				// too many exceptions to catch.
				e.printStackTrace();
			}
		} else {
			Toast.makeText(
					getActivity(),
					"DEBUG: No UUID - cannot create a PIN verification object.",
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Starts a new thread that will callback to end the shaking animation when
	 * a PIN is incorrectly entered.
	 */
	protected void resetTimer() {
		isTimerRunning = true;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				mHandler.obtainMessage(1).sendToTarget();
			}
		}, 500);
	}

	/**
	 * UI Thread callback for timer
	 */
	public Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			currentPin = "";
			updateProgress(currentPin);
			unlockPad(true);
			return true;
		}
	});

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("pin", currentPin);
		outState.putInt("attemptsRemaining", attemptsRemaining);
		outState.putBoolean("verify", verify);
	}

	/**
	 * This method is called if a PIN has been entered
	 */
	public void pinEntryComplete() {
		//testEncryptionDecryption(null);
		//wrongPIN();
		
		if (pinsetup) {
			if (verify != true) {
				// PIN is being created
				TextView instructions = (TextView) getView().findViewById(
						R.id.enterpin_text);
				instructions.setText(getActivity().getResources().getString(R.string.pin_verify));
				verifyPin = currentPin; //compare against
				currentPin = "";
				verify = true;
				updateProgress(currentPin);
			} else {
				//This is the verify stage
				if (currentPin.equals(verifyPin)){
					savePIN();
					//Toast.makeText(getActivity(), "PIN verification object saved to SharedPrefs",
					Intent returnIntent = new Intent();
					returnIntent.putExtra("pin", currentPin);
					getActivity().setResult(Activity.RESULT_OK, returnIntent);
					getActivity().finish();
				} else {
					wrongPIN();
					TextView instructions = (TextView) getView().findViewById(
							R.id.enterpin_text);
							
					instructions.setText(getResources().getString(R.string.pin_verify_fail));
					verify = false;
				}
			}
		} else {
			// PIN is being validated
			if (testUnlockPin()) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("pin", currentPin);
				getActivity().setResult(Activity.RESULT_OK, returnIntent);
				getActivity().finish();
			} else {
				wrongPIN();
			}
		} 
	}
}
