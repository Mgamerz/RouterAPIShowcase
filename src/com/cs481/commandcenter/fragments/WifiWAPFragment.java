package com.cs481.commandcenter.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.dialog.CommitWAPDialogFragment;
import com.cs481.commandcenter.responses.PutRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.config.wlan.Bss;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Fragment that allows the user to edit information on a router's wireless
 * access point
 * 
 * @author Mike Perez
 */

public class WifiWAPFragment extends Fragment {
	private static final int COMMIT_CHANGES_FRAGMENT = 0;
	private SpiceManager spiceManager;
	private AuthInfo authInfo;
	private Bss wapinfo;
	private int wapindex;
	private boolean unmaskPassword = false; // password masking
	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			authInfo = savedInstanceState.getParcelable("authInfo");
			wapinfo = savedInstanceState.getParcelable("wapinfo");
			wapindex = savedInstanceState.getInt("wapindex");
			unmaskPassword = savedInstanceState.getBoolean("unmaskPassword");

		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
				wapinfo = passedArgs.getParcelable("wapinfo");
				wapindex = passedArgs.getInt("wapindex");
			}
		}
	}

	/**
	 * Creates a new Wifi WAP fragment.
	 * 
	 * @param authInfo
	 *            Authinfo to use when pushing data to the wap config
	 * @param wapinfo
	 *            WAP information to populate the display with
	 * @param wapindex
	 *            index in the bss list where to push (see the API)
	 * @return
	 */
	public static WifiWAPFragment newInstance(AuthInfo authInfo, Bss wapinfo, int wapindex) {
		WifiWAPFragment wifiFrag = new WifiWAPFragment();
		Bundle args = new Bundle();
		args.putParcelable("authInfo", authInfo);
		args.putParcelable("wapinfo", wapinfo);
		args.putInt("wapindex", wapindex);
		wifiFrag.setArguments(args);
		return wifiFrag;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
		Log.i(CommandCenterActivity.TAG, "Saving wifi instance");
		outState.putParcelable("authInfo", authInfo);
		outState.putParcelable("wapinfo", wapinfo);
		outState.putInt("wapindex", wapindex);
		outState.putBoolean("unmaskPassword", unmaskPassword);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View v = inflater.inflate(R.layout.fragment_wifiwap, container, false);

		EditText ssidField = (EditText) v.findViewById(R.id.wap_ssid_value);
		ssidField.setText(wapinfo.getSsid());

		Spinner encryption_spinner = (Spinner) v.findViewById(R.id.wap_encryptiontype_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> encryption_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.wifiap_encryptiontype_values, R.layout.spinner_header);
		// Specify the layout to use when the list of choices appears
		encryption_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		encryption_spinner.setAdapter(encryption_adapter);

		CheckBox broadcasting = (CheckBox) v.findViewById(R.id.wap_broadcasting);
		broadcasting.setChecked(!wapinfo.getHidden());

		CheckBox isolating = (CheckBox) v.findViewById(R.id.wap_isolating);
		isolating.setChecked(wapinfo.getIsolate());

		parseSecurity(v);

		// listeners
		encryption_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				TextView insecure_text = (TextView) v.findViewById(R.id.wap_insecure_text);
				Spinner cipherSpinner = (Spinner) v.findViewById(R.id.wap_ciphertype_spinner);
				RelativeLayout passwordLayout = (RelativeLayout) v.findViewById(R.id.wapconnect_passwordlayout);
				TableLayout wepkeysLayout = (TableLayout) v.findViewById(R.id.wapconnect_wepkeyslayout);

				if (position == 0) {
					// its set to none
					insecure_text.setText(getResources().getString(R.string.wap_client_connection_insecure));
					insecure_text.setVisibility(TextView.VISIBLE);
					cipherSpinner.setVisibility(Spinner.GONE);
					passwordLayout.setVisibility(View.GONE);
					wepkeysLayout.setVisibility(View.GONE);
				} else if (position > 0 && position < 4) {
					// Its set to a WEP
					insecure_text.setText(getResources().getString(R.string.wap_client_connection_insecure_wep));
					insecure_text.setVisibility(TextView.VISIBLE);
					cipherSpinner.setVisibility(Spinner.GONE);
					passwordLayout.setVisibility(View.GONE);
					wepkeysLayout.setVisibility(View.VISIBLE);
				} else if (position >= 4) {
					// higher than WEP
					insecure_text.setVisibility(TextView.GONE);
					cipherSpinner.setVisibility(Spinner.VISIBLE);
					passwordLayout.setVisibility(View.VISIBLE);
					wepkeysLayout.setVisibility(View.GONE);

					// set the array adapters for the positions:
					if (position == 4 || position == 5) {
						// WPA1
						ArrayAdapter<CharSequence> cipher_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.wifiap_ciphertype_wpa1values, R.layout.spinner_header);
						cipher_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cipherSpinner.setAdapter(cipher_adapter);
					} else if (position == 6 || position == 7) {
						// WPA2
						ArrayAdapter<CharSequence> cipher_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.wifiap_ciphertype_wpa2values, R.layout.spinner_header);
						cipher_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cipherSpinner.setAdapter(cipher_adapter);
					} else if (position == 8 || position == 9) {
						// WPA1/WPA2
						ArrayAdapter<CharSequence> cipher_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.wifiap_ciphertype_wpa1wpa2values, R.layout.spinner_header);
						cipher_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						cipherSpinner.setAdapter(cipher_adapter);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

		// Password masking on/off restoration
		if (unmaskPassword) {
			final EditText password_field = (EditText) v.findViewById(R.id.wapconnect_newpassword_field);
			password_field.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}

		return v;
	}

	private void parseSecurity(View v) {
		// TODO Auto-generated method stub
		Log.i(CommandCenterActivity.TAG, "WAP security: " + wapinfo.getAuthmode());
		Spinner cipher_spinner = (Spinner) v.findViewById(R.id.wap_ciphertype_spinner);
		Spinner encryption_spinner = (Spinner) v.findViewById(R.id.wap_encryptiontype_spinner);

		boolean setCipher = false; // if this get set to true a block of code
									// down below will execute to set the cipher
									// in the UI
		boolean showWEP = false;
		if (wapinfo.getAuthmode().equals(Utility.AUTH_OPEN)) {
			encryption_spinner.setSelection(0);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WEPAUTO)) {
			encryption_spinner.setSelection(1);
			showWEP = true;
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WEPOPEN)) {
			encryption_spinner.setSelection(2);
			showWEP = true;
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WEPSHARED)) {
			encryption_spinner.setSelection(3);
			showWEP = true;
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WPA1)) {
			setCipher = true;
			encryption_spinner.setSelection(4);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WPA1_ENTERPRISE)) {
			setCipher = true;
			encryption_spinner.setSelection(5);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WPA2)) {
			setCipher = true;
			encryption_spinner.setSelection(6);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WPA2_ENTERPRISE)) {
			setCipher = true;
			encryption_spinner.setSelection(7);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WPA1WPA2)) {
			setCipher = true;
			encryption_spinner.setSelection(8);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WPA1WPA2_ENTERPRISE)) {
			setCipher = true;
			encryption_spinner.setSelection(9);
		}

		RelativeLayout passwordLayout = (RelativeLayout) v.findViewById(R.id.wapconnect_passwordlayout);
		TableLayout wepkeysLayout = (TableLayout) v.findViewById(R.id.wapconnect_wepkeyslayout);

		if (setCipher) {
			if (wapinfo.getWpacipher().equals(Utility.CIPHER_AES)) {
				cipher_spinner.setSelection(0);
			} else if (wapinfo.getWpacipher().equals(Utility.CIPHER_TKIPAES)) {
				cipher_spinner.setSelection(1);
			}
			passwordLayout.setVisibility(View.VISIBLE);
			wepkeysLayout.setVisibility(View.GONE);

		} else {
			cipher_spinner.setVisibility(View.GONE);
			if (showWEP) {
				passwordLayout.setVisibility(View.GONE);
				wepkeysLayout.setVisibility(View.VISIBLE);
			} else {
				// no cipher (not wpa), no wep, must be open
				passwordLayout.setVisibility(View.GONE);
				wepkeysLayout.setVisibility(View.GONE);
			}
		}

		// WEP code fields
		EditText wep0 = (EditText) v.findViewById(R.id.wap_wepkey0_value);
		EditText wep1 = (EditText) v.findViewById(R.id.wap_wepkey1_value);
		EditText wep2 = (EditText) v.findViewById(R.id.wap_wepkey2_value);
		EditText wep3 = (EditText) v.findViewById(R.id.wap_wepkey3_value);

		wep0.setText(wapinfo.getWepkey0());
		wep1.setText(wapinfo.getWepkey1());
		wep2.setText(wapinfo.getWepkey2());
		wep3.setText(wapinfo.getWepkey3());

		// password on/off listener
		CheckBox show_password = (CheckBox) v.findViewById(R.id.wapconnect_showpassword);
		final EditText password_field = (EditText) v.findViewById(R.id.wapconnect_newpassword_field);

		show_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					password_field.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
					unmaskPassword = true;
				} else {
					password_field.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					unmaskPassword = false;
				}
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
		// /You will setup the action bar with pull to refresh layout
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle(getResources().getString(R.string.wap_editor_title));
		sa.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		sa.getActionBar().setDisplayHomeAsUpEnabled(true);
		spiceManager = sa.getSpiceManager();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
		inflater.inflate(R.menu.wifiwap_menu, this.menu);
		// Get widget's instance
	}

	/**
	 * Constructs a modified BSS object and puts it to the router. It determines
	 * if something should be collected and modified if it is showing; if it is
	 * not showing, then it could not have been edited. Passwords are only
	 * updated if a new one is filled, if the field is empty, it is ignored.
	 */
	private void putWAPConfig() {
		// perform the request.
		// construct BSS to put
		Bss modified_wap = wapinfo; // edit the existing one

		// get network auth type
		View v = getView();
		EditText ssidField = (EditText) v.findViewById(R.id.wap_ssid_value);
		modified_wap.setSsid(ssidField.getText().toString());

		Spinner authSpinner = (Spinner) v.findViewById(R.id.wap_encryptiontype_spinner);
		int authIndex = authSpinner.getSelectedItemPosition();

		if (authSpinner.getVisibility() == View.VISIBLE) {
			modified_wap.setAuthmode(Utility.indexToAuthString(authIndex));
		}

		// get cipher type if it has changed
		Spinner cipherSpinner = (Spinner) v.findViewById(R.id.wap_ciphertype_spinner);
		if (cipherSpinner.getVisibility() == View.VISIBLE) {
			int cipherIndex = cipherSpinner.getSelectedItemPosition();
			if (authIndex == 4 || authIndex == 5) {
				// WPA 1 is index 0
				modified_wap.setWpacipher(Utility.indexToCipherString(0, cipherIndex));
			} else if (authIndex == 6 || authIndex == 7) {
				// WPA 2 is index 1
				modified_wap.setWpacipher(Utility.indexToCipherString(1, cipherIndex));
			} else if (authIndex == 8 || authIndex == 9) {
				// WPA1/WPA2 is index 2
				modified_wap.setWpacipher(Utility.indexToCipherString(2, cipherIndex));

			}
		}

		// get hidden var
		CheckBox broadcasting = (CheckBox) v.findViewById(R.id.wap_broadcasting);
		modified_wap.setHidden(!broadcasting.isChecked()); // broadcasting is
															// the opposite of
															// hidden, so we
															// reverse the
															// result.

		// get isolated var
		CheckBox isolated = (CheckBox) v.findViewById(R.id.wap_isolating);
		modified_wap.setIsolate(isolated.isChecked()); // broadcasting is the
														// opposite of hidden,
														// so we reverse the
														// result.

		EditText passwordField = (EditText) v.findViewById(R.id.wapconnect_newpassword_field);
		if (passwordField.getVisibility() == View.VISIBLE && !passwordField.getText().toString().equals("")) {
			// new password has been entered
			// router knows if the password being pushed is the original or a
			// new one.
			modified_wap.setWpapsk(passwordField.getText().toString());
		}

		// get wep keys
		// WEP code fields
		TableLayout wepkeysLayout = (TableLayout) v.findViewById(R.id.wapconnect_wepkeyslayout);
		if (wepkeysLayout.getVisibility() == View.VISIBLE) {
			EditText wep0 = (EditText) v.findViewById(R.id.wap_wepkey0_value);
			modified_wap.setWepkey0(wep0.getText().toString());
			EditText wep1 = (EditText) v.findViewById(R.id.wap_wepkey1_value);
			modified_wap.setWepkey1(wep1.getText().toString());

			EditText wep2 = (EditText) v.findViewById(R.id.wap_wepkey2_value);
			modified_wap.setWepkey2(wep2.getText().toString());

			EditText wep3 = (EditText) v.findViewById(R.id.wap_wepkey3_value);
			modified_wap.setWepkey3(wep3.getText().toString());

		}
		PutRequest commitRequest = new PutRequest(getActivity(), modified_wap, authInfo, "config/wlan/radio/0/bss/" + wapindex, Bss.class);
		spiceManager.execute(commitRequest, commitRequest.createCacheKey(), DurationInMillis.ALWAYS_EXPIRED, new WAPPutRequestListener());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// up navigation
			Log.i(CommandCenterActivity.TAG, "UP in WAP Editor is being handled.");
			FragmentManager fm = getActivity().getSupportFragmentManager();
			fm.popBackStack();
			return true;
		case R.id.wifiwap_commit:
			Log.i(CommandCenterActivity.TAG, "Commit changes in WAP Editor is being handled.");
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);

			DialogFragment dialogFrag = CommitWAPDialogFragment.newInstance();
			dialogFrag.setTargetFragment(this, COMMIT_CHANGES_FRAGMENT);
			dialogFrag.show(ft, "dialog");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(CommandCenterActivity.TAG, "Activity result.");
		switch (requestCode) {
		case COMMIT_CHANGES_FRAGMENT:
			if (resultCode == Activity.RESULT_OK) {
				// After Ok code.
				Log.i(CommandCenterActivity.TAG, "User accepted changes, commiting them now.");
				putWAPConfig();
			}

			break;
		}
	}

	private class WAPPutRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			if (!isAdded()) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to put data to server for WAP! Changes may have disconnected this device.");
		}

		@Override
		public void onRequestSuccess(Response response) {
			if (!isAdded()) {
				return;
			}
			if (response.getResponseInfo() != null) {

				if (response.getResponseInfo().getSuccess()) {
					Toast.makeText(getActivity(), getResources().getString(R.string.success_wlan_config), Toast.LENGTH_SHORT).show();
					Log.i(CommandCenterActivity.TAG, "Pushed data to the WLAN Config in WifiWAPFragment");
				} else {
					Bss errorInfo = (Bss) response.getData();
					Log.i(CommandCenterActivity.TAG, "Error pushing to wifi profile to router: " + errorInfo.getException() + ": " + errorInfo.getReason());
				}
			}

		}
	}
}
