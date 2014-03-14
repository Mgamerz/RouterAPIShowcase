package com.cs481.commandcenter.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.dialog.WifiWanDialogFragment;
import com.cs481.commandcenter.listrows.WlanListRow;
import com.cs481.commandcenter.responses.GetRequest;
import com.cs481.commandcenter.responses.PostRequest;
import com.cs481.commandcenter.responses.PutRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.config.wlan.ConfigWlan;
import com.cs481.commandcenter.responses.config.wwan.Radio;
import com.cs481.commandcenter.responses.config.wwan.WANProfile;
import com.cs481.commandcenter.responses.config.wwan.WWAN;
import com.cs481.commandcenter.responses.status.wlan.StatusWlan;
import com.cs481.commandcenter.responses.status.wlan.WAP;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class WifiFragment extends ListFragment implements OnRefreshListener {
	private static final int WANDIALOG_FRAGMENT = 0;
	private static final String CACHEKEY_WLANGET = "config_wlan_get";
	private PullToRefreshLayout mPullToRefreshLayout;
	private ProgressDialog progressDialog;
	private SpiceManager spiceManager;
	private AuthInfo authInfo;
	private ArrayList<WlanListRow> rows;
	private ArrayList<WAP> waps;
	private boolean shouldLoadData = true;
	private boolean wifiState = false;
	private boolean wifiStateEnabled = false;
	private ArrayList<WANProfile> wanprofiles;

	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			authInfo = savedInstanceState.getParcelable("authInfo");
			wifiStateEnabled = savedInstanceState
					.getBoolean("wifiStateEnabled");
			wifiState = savedInstanceState.getBoolean("wifiState");
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
			}
		}
	}

	public static WifiFragment newInstance(AuthInfo authInfo) {
		WifiFragment wifiFrag = new WifiFragment();

		Bundle args = new Bundle();
		args.putParcelable("authInfo", authInfo);
		wifiFrag.setArguments(args);

		return wifiFrag;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
		Log.i(CommandCenterActivity.TAG, "Saving instance");
		outState.putParcelable("authInfo", authInfo);
		outState.putBoolean("shouldLoadData", shouldLoadData);
		outState.putBoolean("wifiState", wifiState);
		outState.putBoolean("wifiStateEnabled", wifiStateEnabled);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_wificlient, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// This is the View which is created by ListFragment
		ViewGroup viewGroup = (ViewGroup) view;

		// We need to create a PullToRefreshLayout manually
		mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

		// We can now setup the PullToRefreshLayout
		ActionBarPullToRefresh
				.from(getActivity())
				.insertLayoutInto(viewGroup)
				.theseChildrenArePullable(getListView(),
						getListView().getEmptyView()).listener(this)
				.setup(mPullToRefreshLayout);
	}

	@Override
	public void onStart() {
		super.onStart();
		// /You will setup the action bar with pull to refresh layout
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle(getResources().getString(R.string.wifiwan_title)); // TODO
																		// change
																		// to
																		// string
																		// resource
		spiceManager = sa.getSpiceManager();
		if (shouldLoadData) {
			setWifiState();
			readWlanConfig(true);
			shouldLoadData = false;
		} else {
			Log.i(CommandCenterActivity.TAG, waps.toString());
			//updateWapList(waps);
		}
	}

	/**
	 * Set's a network operation to change the Wifi Toggle switch state
	 */
	private void setWifiState() {
		// perform the request.
		GetRequest request = new GetRequest(getActivity(), authInfo, "config/wlan",
				ConfigWlan.class, "wlanconfigget");
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED,
				new WLANConfigGetRequestListener());
	}

	@Override
	public void onRefreshStarted(View view) {
		readWlanConfig(false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
		inflater.inflate(R.menu.wifiwan_menu, this.menu);
		// Get widget's instance
		setWifiToggleListener();
	}

	private void setWifiToggleListener() {
		Switch wifiToggle = (Switch) menu.findItem(R.id.wifi_toggle)
				.getActionView();
		wifiToggle.setChecked(wifiState);
		wifiToggle.setEnabled(wifiStateEnabled);
		wifiToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Log.i(CommandCenterActivity.TAG,
						"Performing put request to enabled wlan");
				PutRequest request = new PutRequest(getActivity(), Boolean.valueOf(isChecked),
						authInfo, "config/wlan/radio/0/enabled", Boolean.class);
				String lastRequestCacheKey = request.createCacheKey();

				spiceManager.execute(request, lastRequestCacheKey,
						DurationInMillis.ALWAYS_EXPIRED,
						new WLANEnabledPutRequestListener());
			}

		});
	}

	private void readWlanConfig(boolean dialog) {
		// perform the request.
		 GetRequest wapListrequest = new GetRequest(getActivity(), authInfo, "status/wlan",
				StatusWlan.class, CACHEKEY_WLANGET);
		String lastRequestCacheKey = wapListrequest.createCacheKey();
		Resources resources = getResources();
		 if (dialog) {
			ContextThemeWrapper wrapper = new ContextThemeWrapper(
					getActivity(), android.R.style.Theme_Holo_Light);

			progressDialog = new ProgressDialog(wrapper);
			progressDialog.setMessage(resources
					.getString(R.string.wlan_reading));
			progressDialog.show();
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
		} 

		spiceManager.execute(wapListrequest, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED,
				new WLANStatusGetRequestListener()); 
	}

	private class WLANStatusGetRequestListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Resources resources = getResources();
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			Log.i(CommandCenterActivity.TAG, "Failed to read WLAN!");
			Toast.makeText(getActivity(),
					resources.getString(R.string.wlan_get_config_failure),
					Toast.LENGTH_SHORT).show();
			mPullToRefreshLayout.setRefreshComplete();
		}

		@Override
		public void onRequestSuccess(Response response) {
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			if (response.getResponseInfo().getSuccess()) {
				StatusWlan wlan = (StatusWlan) response.getData();
				Log.i(CommandCenterActivity.TAG, "WLAN request successful");
				//updateWlanList(wlan);
				Log.i(CommandCenterActivity.TAG, "isRefresh(): "
						+ mPullToRefreshLayout.isRefreshing());
			} else {

				Toast.makeText(getActivity(),
						response.getResponseInfo().getReason(),
						Toast.LENGTH_LONG).show();
			}
			mPullToRefreshLayout.setRefreshComplete();

		}
	}

	private class WLANEnabledPutRequestListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.i(CommandCenterActivity.TAG, "Failed to put the wifi status!");
			Toast.makeText(getActivity(), "Failed to change the wifi status.",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response enabledPutResult) {
			Log.i(CommandCenterActivity.TAG, "UPDATING SWITCH!");
			Boolean bool = (Boolean) enabledPutResult.getData();
			Switch wifiToggle = (Switch) menu.findItem(R.id.wifi_toggle)
					.getActionView();
			wifiToggle.setOnCheckedChangeListener(null);
			wifiState = bool.booleanValue();
			wifiStateEnabled = true;
			wifiToggle.setEnabled(wifiStateEnabled);
			wifiToggle.setChecked(wifiState);
			setWifiToggleListener();
		}
	}

	private class WLANConfigGetRequestListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.i(CommandCenterActivity.TAG, "Failed to read WLAN!");
			Toast.makeText(getActivity(), "Failed to get the WLAN Config.",
					Toast.LENGTH_SHORT).show();
			mPullToRefreshLayout.setRefreshComplete();
		}

		@Override
		public void onRequestSuccess(Response wlanConfig) {
			Log.i(CommandCenterActivity.TAG, "UPDATING SWITCH!");
			ConfigWlan cwlan = (ConfigWlan) wlanConfig.getData();
			Switch wifiToggle = (Switch) menu.findItem(R.id.wifi_toggle)
					.getActionView();
			wifiToggle.setOnCheckedChangeListener(null);
			wifiState = cwlan.getRadios().get(0).getEnabled();
			wifiStateEnabled = true;
			wifiToggle.setEnabled(wifiStateEnabled);
			wifiToggle.setChecked(wifiState);
			setWifiToggleListener();
			mPullToRefreshLayout.setRefreshComplete();
		}
	}

	public void connectAsWAN(WANProfile wanprofile) {
		Log.w(CommandCenterActivity.TAG, "Connecting as WAN.");

		for (WANProfile knownProfile : wanprofiles) {
			if (wanprofile.equals(knownProfile)) {
				Log.e(CommandCenterActivity.TAG,
						"This profile is already defined!");
				return; // it's known
			}
		}

		// Profile is not yet defined. Do a POST to the router.
		Log.i(CommandCenterActivity.TAG,
				"Performing put request to enabled wlan");
		PostRequest request = new PostRequest(getActivity(), wanprofile, authInfo,
				"config/wwan/radio/0/profiles", WANProfile.class); // TODO will
																	// have to
																	// deal with
																	// dual band
																	// again.
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED,
				new WANProfilePostRequestListener());

	}

	private class WANProfilesGetRequestListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.i(CommandCenterActivity.TAG, "Failed to read WAN Profile list!");
			Toast.makeText(getActivity(),
					"Failed to get the WAN Profile listg.", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onRequestSuccess(Response wanProfileList) {
			Log.i(CommandCenterActivity.TAG, "Got wan profiles list.");
			WWAN wwan = (WWAN) wanProfileList.getData();
			ArrayList<Radio> profileRadios = wwan.getRadios();
			ArrayList<WANProfile> wanprofiles = new ArrayList<WANProfile>();

			if (profileRadios != null) {
				for (Radio radio : profileRadios) {
					ArrayList<WANProfile> singleRadioProfiles = radio
							.getProfiles();
					if (singleRadioProfiles != null) {
						for (WANProfile profile : singleRadioProfiles) {
							wanprofiles.add(profile);
						}
					}
				}
			}
			WifiFragment.this.wanprofiles = wanprofiles;
		}
	}

	private class WANProfilePostRequestListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.w(CommandCenterActivity.TAG, "Failed to post the WAN Profile!");
			Toast.makeText(getActivity(), "Failed to POST Profile to router.",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response wanProfileList) {
			Log.i(CommandCenterActivity.TAG, "Posted the WAN profile.");
			// WWAN wwan = (WWAN) wanProfileList.getData();
			Toast.makeText(getActivity(), "POST successful.", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case WANDIALOG_FRAGMENT:

			if (resultCode == Activity.RESULT_OK) {
				// After Ok code.
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// After Cancel code.
			}

			break;
		}
	}
}
