package com.cs481.commandcenter.fragments;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.config.wlan.Bss;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class WifiWAPFragment extends Fragment {
	private static final String CACHEKEY_WWAPPUT = "config_wlan_put";
	private PullToRefreshLayout mPullToRefreshLayout;
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
		View v = inflater.inflate(R.layout.fragment_wifiwap, container, false);
		
		EditText ssidField = (EditText) v.findViewById(R.id.wap_ssid_value);
		ssidField.setText(wapinfo.getSsid());
		
		
		Spinner spinner = (Spinner) v.findViewById(R.id.wap_encryptiontype_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.wifiap_encryptiontype_values, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// /You will setup the action bar with pull to refresh layout
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle(getResources().getString(R.string.wifi_title));
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
		//PutRequest wwapRequest = new PutRequest(getActivity(), authInfo, "config/wlan", ConfigWlan.class, CACHEKEY_WWAPPUT);
		//String lastRequestCacheKey = wwapRequest.createCacheKey();
		//spiceManager.execute(wwapRequest, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new WWAPSGetRequestListener());
	}

	private class WAPPutRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			if (!isAdded()) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to put data to server for WAP!");
			Toast.makeText(getActivity(), getResources().getString(R.string.failed_wlan_config), Toast.LENGTH_SHORT).show();
			//wwapListState = WWAP_FAILED;

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
