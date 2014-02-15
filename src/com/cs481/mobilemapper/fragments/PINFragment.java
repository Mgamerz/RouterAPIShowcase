package com.cs481.mobilemapper.fragments;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;

public class PINFragment extends Fragment implements OnClickListener {
	String currentPin = ""; // pin that has been currently entered
	int attemptsRemaining = 5; // TODO this should be set in SharedPreferences
								// with a timestamp for a cooldown on attempts
	boolean isTimerRunning = false;
	boolean pinsetup;
	
	//Crypto variables
	private int KEY_LENGTH = 256;
	private int ITERATION_COUNT = 1000;
	public final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";

	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		if (savedInstancedState != null) {
			currentPin = savedInstancedState.getString("pin");
		}
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
		}
		return rootView;
	}

	public void wrongPIN() {
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
		SpiceActivity sa = (SpiceActivity) getActivity();
		Resources resources = getResources();
		sa.setTitle(resources.getString(R.string.pin_actionbar_title)); // TODO
																		// change
																		// to
																		// string
																		// resource
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
			String hash = createPIN();
			testEncryptionDecryption(hash);
			// Debugging - simulate a bad pin.
			// wrongPIN();
		}
	}

	private String createPIN() {
		Resources resources = getResources();
		SharedPreferences crypto = getActivity().getSharedPreferences(
				resources.getString(R.string.crypto_prefsdb),
				Context.MODE_PRIVATE);
		String uuid = crypto.getString("uuid", null);

		String device_uuid = Secure.getString(getActivity()
				.getContentResolver(), Secure.ANDROID_ID);
		uuid = uuid + device_uuid; // device specific. Might want to make
									// this more random.
		return uuid;
	}

	private void testEncryptionDecryption(String hash) {
		try {
		SecretKey secret = generateKey(currentPin, hash.getBytes("UTF-8"));
		byte[] encrypted = encryptMsg("LOGINPASS", secret);
		Log.i(CommandCenterActivity.TAG, "Encrypted to: "+new String(encrypted, "UTF-8"));
		String result = decryptMsg(encrypted, secret);
		Log.i(CommandCenterActivity.TAG, "Decrypted back to: "+result);
		} catch (Exception e){
			//too many exceptions to catch.
			e.printStackTrace();
		}
	}

	public static SecretKey generateKey(String code, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
	    // Number of PBKDF2 hardening rounds to use. Larger values increase
	    // computation time. You should select a value that causes computation
	    // to take >100ms.
	    final int iterations = 1000; 

	    // Generate a 256-bit key
	    final int outputKeyLength = 256;

	    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    KeySpec keySpec = new PBEKeySpec(code.toCharArray(), salt, iterations, outputKeyLength);
	    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
	    return secretKey;
	}

	public static byte[] encryptMsg(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
	/* Encrypt the message. */
	    Cipher cipher = null;
	    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, secret);
	    byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
	    return cipherText;
	}

	public static String decryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

	    /* Decrypt the message, given derived encContentValues and initialization vector. */
	    Cipher cipher = null;
	    cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	   cipher.init(Cipher.DECRYPT_MODE, secret);
	    String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
	    return decryptString;
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
	}
}
