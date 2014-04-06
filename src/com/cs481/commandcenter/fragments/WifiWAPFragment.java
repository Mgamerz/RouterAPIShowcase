package com.cs481.commandcenter.fragments;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.config.wlan.Bss;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class WifiWAPFragment extends Fragment {
	private static final String CACHEKEY_WWAPPUT = "config_wapedit_put";
	private SpiceManager spiceManager;
	private AuthInfo authInfo;
	private Bss wapinfo;
	private int wapindex;
	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			authInfo = savedInstanceState.getParcelable("authInfo");
			wapinfo = savedInstanceState.getParcelable("wapinfo");
			wapindex = savedInstanceState.getInt("wapindex");
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
				wapinfo = passedArgs.getParcelable("wapinfo");
				wapindex = passedArgs.getInt("wapindex");
			}
		}
	}

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

		Spinner cipher_spinner = (Spinner) v.findViewById(R.id.wap_ciphertype_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> cipher_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.wifiap_ciphertype_values, R.layout.spinner_header);
		// Specify the layout to use when the list of choices appears
		cipher_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		cipher_spinner.setAdapter(cipher_adapter);

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
				if (position == 0) {
					// its set to none
					insecure_text.setVisibility(TextView.VISIBLE);
					cipherSpinner.setVisibility(Spinner.GONE);
				} else {
					insecure_text.setVisibility(TextView.GONE);
					if (position > 1) { //higher than WEP 
						cipherSpinner.setVisibility(Spinner.VISIBLE);
					} else {
						cipherSpinner.setVisibility(Spinner.GONE);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

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
		if (wapinfo.getAuthmode().equals(Utility.AUTH_OPEN)) {
			encryption_spinner.setSelection(0);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WEPAUTO)) {
			encryption_spinner.setSelection(1);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WPA1)) {
			setCipher = true;
			encryption_spinner.setSelection(2);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WPA2)) {
			setCipher = true;
			encryption_spinner.setSelection(3);
		} else if (wapinfo.getAuthmode().equals(Utility.AUTH_WPA1WPA2)) {
			setCipher = true;
			encryption_spinner.setSelection(4);
		}

		if (setCipher) {
			if (wapinfo.getWpacipher().equals(Utility.CIPHER_AES)) {
				cipher_spinner.setSelection(0);
			} else if (wapinfo.getWpacipher().equals(Utility.CIPHER_TKIPAES)) {
				cipher_spinner.setSelection(1);
			}
		} else {
			cipher_spinner.setVisibility(View.GONE);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// /You will setup the action bar with pull to refresh layout
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle(getResources().getString(R.string.wap_editor_title));
		spiceManager = sa.getSpiceManager();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
		inflater.inflate(R.menu.wifiwap_menu, this.menu);
		// Get widget's instance
	}

	private void putWAPConfig() {
		// perform the request.
		// PutRequest wwapRequest = new PutRequest(getActivity(), authInfo,
		// "config/wlan", ConfigWlan.class, CACHEKEY_WWAPPUT);
		// String lastRequestCacheKey = wwapRequest.createCacheKey();
		// spiceManager.execute(wwapRequest, lastRequestCacheKey,
		// DurationInMillis.ALWAYS_EXPIRED, new WWAPSGetRequestListener());
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class WAPPutRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			if (!isAdded()) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to put data to server for WAP!");
			Toast.makeText(getActivity(), getResources().getString(R.string.failed_wlan_config), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response response) {
			if (!isAdded()) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Pushed data to the WLAN Config in WifiWAPFragment");
		}
		
		
	}
}
