package com.cs481.commandcenter.fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.dialog.DisableWifiDialog;
import com.cs481.commandcenter.dialog.RouterConfirmDialogFragment;
import com.cs481.commandcenter.listeners.WLANEnabledPutRequestListener;
import com.cs481.commandcenter.responses.GetRequest;
import com.cs481.commandcenter.responses.PutRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.config.wlan.Bss;
import com.cs481.commandcenter.responses.config.wlan.ConfigWlan;
import com.cs481.commandcenter.responses.config.wlan.Radio;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class WifiFragment extends ListFragment implements OnRefreshListener {
	private static final String CACHEKEY_WWAPGET = "config_wlan_get";
	private static final int WWAP_LOADING = 0;
	private static final int WWAP_LOADED = 1;
	private static final int WWAP_FAILED = 2;
	private PullToRefreshLayout mPullToRefreshLayout;
	private SpiceManager spiceManager;
	private AuthInfo authInfo;
	private WWAPAdapter adapter;
	private ArrayList<Bss> wwaps; // this is the original one that should never
									// be modified unless on a refresh
	private int wwapListState = WWAP_LOADING;
	private boolean shouldLoadData = true;
	private boolean wifiState = false;
	private boolean wifiStateEnabled = false;

	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			authInfo = savedInstanceState.getParcelable("authInfo");
			wifiStateEnabled = savedInstanceState.getBoolean("wifiStateEnabled");
			wifiState = savedInstanceState.getBoolean("wifiState");
			wwapListState = savedInstanceState.getInt("wlanListState");
			wwaps = savedInstanceState.getParcelableArrayList("wwaps");
			shouldLoadData = savedInstanceState.getBoolean("shouldLoadData");
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
		Log.i(CommandCenterActivity.TAG, "Saving wifi instance");
		outState.putParcelable("authInfo", authInfo);
		outState.putBoolean("shouldLoadData", shouldLoadData);
		outState.putBoolean("wifiState", wifiState);
		outState.putBoolean("wifiStateEnabled", wifiStateEnabled);
		outState.putInt("wlanListState", wwapListState);

		if (wwaps == null) {
			Log.i(CommandCenterActivity.TAG, "WWAPS are null for onSaveInstanceState(), creating empty list to prevent crash.");
			wwaps = new ArrayList<Bss>();
		}
		outState.putParcelableArrayList("wwaps", wwaps);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_wifi, container, false);
		if (wwapListState == WWAP_FAILED) {
			ProgressBar bar = (ProgressBar) v.findViewById(R.id.wwap_loadingprogressbar);
			bar.setVisibility(ProgressBar.GONE);

			TextView message = (TextView) v.findViewById(R.id.wireless_loadingtext);
			message.setText(getActivity().getResources().getString(R.string.wifi_load_failed));
		}
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// This is the View which is created by ListFragment
		ViewGroup viewGroup = (ViewGroup) view;

		// We need to create a PullToRefreshLayout manually
		mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

		// We can now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity()).insertLayoutInto(viewGroup).theseChildrenArePullable(getListView(), getListView().getEmptyView()).listener(this).setup(mPullToRefreshLayout);
	}

	@Override
	public void onStart() {
		super.onStart();
		// /You will setup the action bar with pull to refresh layout
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle(getResources().getString(R.string.wifi_title));
		spiceManager = sa.getSpiceManager();
		// getListView().set
		if (shouldLoadData) {
			setWifiState();
			readWlanConfig();
			shouldLoadData = false;
		} else {
			Log.i(CommandCenterActivity.TAG, "Reloading existing WWAPS from onStart() " + wwaps);
			updateWWAPList(wwaps);
		}
	}

	/**
	 * Set's a network operation to change the Wifi Toggle switch state
	 */
	private void setWifiState() {
		// perform the request.
		GetRequest request = new GetRequest(getActivity(), authInfo, "config/wlan", ConfigWlan.class, "wlanconfigget");
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new WLANConfigGetRequestListener());
	}

	@Override
	public void onRefreshStarted(View view) {
		readWlanConfig();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
		inflater.inflate(R.menu.wifi_menu, this.menu);
		// Get widget's instance
		setWifiToggleListener();
	}

	/*
	 * 
	 */
	private void setWifiToggleListener() {
		final Switch wifiToggle = (Switch) menu.findItem(R.id.wifi_toggle).getActionView();
		wifiToggle.setChecked(wifiState);
		wifiToggle.setEnabled(wifiStateEnabled);
		wifiToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
				// If we're going from ON to OFF, ask if user is sure
				if(!isChecked){
					new AlertDialog.Builder(getActivity())
				    .setTitle("Disabling WiFi")
				    .setMessage("Are you sure you want to disable WiFi?")
				    .setCancelable(false)
				    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				        	Log.i(CommandCenterActivity.TAG, "Performing put request to enabled wlan");
							PutRequest request = new PutRequest(getActivity(), Boolean.valueOf(isChecked), authInfo, "config/wlan/radio/0/enabled", Boolean.class);
							String lastRequestCacheKey = request.createCacheKey();
							spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new WLANEnabledPutRequestListener());
				        }
				     })
				    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            wifiToggle.setChecked(true);
				        }
				     })
				    .show();
				}
				// Otherwise just turn it from OFF to ON
				else{
					Log.i(CommandCenterActivity.TAG, "Performing put request to enabled wlan");
					PutRequest request = new PutRequest(getActivity(), Boolean.valueOf(isChecked), authInfo, "config/wlan/radio/0/enabled", Boolean.class);
					String lastRequestCacheKey = request.createCacheKey();
					spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new WLANEnabledPutRequestListener());
				}
				
				
			}
		});
	}
	

	private void readWlanConfig() {
		// perform the request.
		GetRequest wwapRequest = new GetRequest(getActivity(), authInfo, "config/wlan", ConfigWlan.class, CACHEKEY_WWAPGET);
		String lastRequestCacheKey = wwapRequest.createCacheKey();
		spiceManager.execute(wwapRequest, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new WWAPSGetRequestListener());
	}

/*	public class WLANEnabledPutRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.i(CommandCenterActivity.TAG, "Failed to put the wifi status!");
			Toast.makeText(getActivity(), "Failed to change the wifi status.", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response enabledPutResult) {
			Log.i(CommandCenterActivity.TAG, "UPDATING SWITCH!");
			Boolean bool = (Boolean) enabledPutResult.getData();
			Switch wifiToggle = (Switch) menu.findItem(R.id.wifi_toggle).getActionView();
			wifiToggle.setOnCheckedChangeListener(null);
			wifiState = bool.booleanValue();
			wifiStateEnabled = true;
			wifiToggle.setEnabled(wifiStateEnabled);
			wifiToggle.setChecked(wifiState);
			setWifiToggleListener();
		}
	}*/

	private class WLANConfigGetRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.i(CommandCenterActivity.TAG, "Failed to read WLAN!");
			Toast.makeText(getActivity(), "Failed to get the WLAN Config.", Toast.LENGTH_SHORT).show();
			mPullToRefreshLayout.setRefreshComplete();
		}

		@Override
		public void onRequestSuccess(Response wlanConfig) {
			Log.i(CommandCenterActivity.TAG, "UPDATING SWITCH!");
			ConfigWlan cwlan = (ConfigWlan) wlanConfig.getData();
			Switch wifiToggle = (Switch) menu.findItem(R.id.wifi_toggle).getActionView();
			wifiToggle.setOnCheckedChangeListener(null);
			wifiState = cwlan.getRadios().get(0).getEnabled();
			wifiStateEnabled = true;
			wifiToggle.setEnabled(wifiStateEnabled);
			wifiToggle.setChecked(wifiState);
			setWifiToggleListener();
			mPullToRefreshLayout.setRefreshComplete();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.w(CommandCenterActivity.TAG, "Item was clicked at pos " + position + ", id " + id);
		String wwap = WifiWAPFragment.class.getName();

		Bss bss = (Bss) (l.getAdapter().getItem(position));
		FragmentManager fm = getActivity().getSupportFragmentManager();

		WifiWAPFragment wlanFragment = WifiWAPFragment.newInstance(authInfo, bss, position);
		FragmentTransaction transaction = fm.beginTransaction();

		// check if the parent activity is dual pane based.
		CommandCenterActivity parent = (CommandCenterActivity) getActivity();
		if (parent.isDualPane()) {
			transaction.replace(R.id.rightside_fragment, wlanFragment, wwap);
		} else {
			transaction.replace(R.id.leftside_fragment, wlanFragment, wwap);
		}
		transaction.addToBackStack(wwap);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.commit();

	}

	private class WWAPSGetRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			if (!isAdded()) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to read the list of WAP configs!");
			Toast.makeText(getActivity(), getResources().getString(R.string.failed_wlan_config), Toast.LENGTH_SHORT).show();
			wwapListState = WWAP_FAILED;

		}

		@Override
		public void onRequestSuccess(Response response) {
			if (!isAdded()) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Fetched the WLAN Config in WifiFragment");
			ConfigWlan wlan = (ConfigWlan) response.getData();
			ArrayList<Radio> radios = wlan.getRadios();
			wwapListState = WWAP_LOADED;
			updateWWAPList(radios.get(0).getBss());
		}
	}

	private void updateWWAPList(ArrayList<Bss> wwaps) {
		if (getActivity() == null) {
			return; // fragment has died
		}
		// TODO Auto-generated method stub
		Log.i(CommandCenterActivity.TAG, "Updating adapter with new wwap information.");
		this.wwaps = wwaps;
		adapter = new WWAPAdapter(getActivity(), wwaps);
		Log.i(CommandCenterActivity.TAG, "created new log adapter :" + adapter);

		setListAdapter(adapter);
		Log.i(CommandCenterActivity.TAG, "Number of wwaps: " + wwaps.size());
		this.wwaps = wwaps;
		Log.i(CommandCenterActivity.TAG, "Number of wwaps pa: " + wwaps.size());

		Log.i(CommandCenterActivity.TAG, "notifying adapter of new dataset");
		shouldLoadData = false;
		adapter.notifyDataSetChanged();
	}

	public class WWAPAdapter extends ArrayAdapter<Bss> {
		private final Context context;
		private final ArrayList<Bss> rows;

		public WWAPAdapter(Context context, ArrayList<Bss> rows) {
			super(context, R.layout.listrow_bss, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public Bss getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView;
			if (convertView == null) {
				rowView = inflater.inflate(R.layout.listrow_bss, parent, false);
			} else {
				rowView = convertView;
			}

			Bss wwap = rows.get(position);

			TextView messageView = (TextView) rowView.findViewById(R.id.listrow_wwap_name);
			messageView.setText(wwap.getSsid());

			Switch apEnabled = (Switch) rowView.findViewById(R.id.listrow_wwap_switch);
			apEnabled.setChecked(wwap.getEnabled());
			return rowView;
		}
	}
}
