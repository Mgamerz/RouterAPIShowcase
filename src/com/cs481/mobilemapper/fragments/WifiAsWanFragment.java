package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.activities.SpiceActivity;
import com.cs481.mobilemapper.listrows.WlanListRow;
import com.cs481.mobilemapper.responses.GetRequest;
import com.cs481.mobilemapper.responses.PutRequest;
import com.cs481.mobilemapper.responses.Response;
import com.cs481.mobilemapper.responses.config.wlan.ConfigWlan;
import com.cs481.mobilemapper.responses.status.wlan.StatusWlan;
import com.cs481.mobilemapper.responses.status.wlan.WAP;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class WifiAsWanFragment extends ListFragment implements
		OnRefreshListener {
	private PullToRefreshLayout mPullToRefreshLayout;
	private ProgressDialog progressDialog;
	private SpiceManager spiceManager;
	private AuthInfo authInfo;
	private ArrayList<WlanListRow> rows;
	private WlanAdapter adapter;
	private ArrayList<WAP> waps;
	private boolean shouldLoadData = true;
	private boolean wifiState = false;
	private boolean wifiStateEnabled = false;
	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			waps = savedInstanceState.getParcelableArrayList("waps");
			authInfo = savedInstanceState.getParcelable("authInfo");
			shouldLoadData = savedInstanceState.getBoolean("shouldLoadData");
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

	public static WifiAsWanFragment newInstance(AuthInfo authInfo) {
		WifiAsWanFragment wawFrag = new WifiAsWanFragment();

		Bundle args = new Bundle();
		args.putParcelable("authInfo", authInfo);
		wawFrag.setArguments(args);

		return wawFrag;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
		Log.i(CommandCenterActivity.TAG, "Saving instance");
		outState.putParcelableArrayList("waps", waps);
		outState.putParcelable("authInfo", authInfo);
		outState.putBoolean("shouldLoadData", shouldLoadData);
		outState.putBoolean("wifiState", wifiState);
		outState.putBoolean("wifiStateEnabled", wifiStateEnabled);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_wlan, container, false);
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
		sa.setTitle("WLAN"); // TODO change to string resource
		spiceManager = sa.getSpiceManager();
		if (shouldLoadData) {
			setWifiState();
			readWlanConfig(true);
			shouldLoadData = false;
		} else {
			Log.i(CommandCenterActivity.TAG, waps.toString());
			updateWapList(waps);
		}
	}

	/**
	 * Set's a network operation to change the Wifi Toggle switch state
	 */
	private void setWifiState() {
		// perform the request.
		com.cs481.mobilemapper.responses.GetRequest request = new com.cs481.mobilemapper.responses.GetRequest(
				authInfo, "config/wlan", ConfigWlan.class, "wlanconfigget");
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED,
				new WLANConfigGetRequestListener());
	}

	public class WlanAdapter extends ArrayAdapter<WlanListRow> {
		private final Context context;
		private final ArrayList<WlanListRow> rows;

		public WlanAdapter(Context context, ArrayList<WlanListRow> rows) {
			super(context, R.layout.listrow_wlan_network, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public WlanListRow getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.listrow_wlan_network,
					parent, false);

			WAP wap = rows.get(position).getWap();

			// title
			TextView title = (TextView) rowView
					.findViewById(R.id.wlan_ssid_text);
			title.setText(rows.get(position).getTitle());

			// signal indicator
			ImageView signalStrengthIcon = (ImageView) rowView
					.findViewById(R.id.signal_strength);
			int dbm = rows.get(position).getWap().getRssi();

			int signalStrength = (Utility.rssiToHumanSignal(dbm) / 25);
			signalStrength = Math.min(signalStrength, 3); // cap it.
			if (wap.getAuthmode().equals("none")) {
				signalStrengthIcon.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_wifi_dark_open));
			} else {
				signalStrengthIcon.setImageDrawable(getResources().getDrawable(
						R.drawable.ic_wifi_dark_locked));
			}
			// signalStrengthIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_wifi_dark_open));
			signalStrengthIcon.setImageLevel(signalStrength);

			// subtitle
			TextView subtitle = (TextView) rowView
					.findViewById(R.id.wlan_type_mode_text);
			subtitle.setText(rows.get(position).getSubtitle());

			return rowView;
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		readWlanConfig(false);

		/*GetRequest request = new GetRequest(
				authInfo, "config/wlan", ConfigWlan.class, "configwlanget");
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED,
				new WLANGetRequestListener());*/
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
		inflater.inflate(R.menu.wifi_menu, this.menu);
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
				Log.i(CommandCenterActivity.TAG, "Performing put request to enabled wlan"); 
				PutRequest request = new PutRequest(Boolean.valueOf(isChecked),
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
		com.cs481.mobilemapper.responses.GetRequest request = new com.cs481.mobilemapper.responses.GetRequest(
				authInfo, "status/wlan", StatusWlan.class, "statuswlanget");
		String lastRequestCacheKey = request.createCacheKey();
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

		spiceManager.execute(request, lastRequestCacheKey,
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
				updateWlanList(wlan);
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

	private class WLANConfigEnabledPutListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.i(CommandCenterActivity.TAG, "Failed to enable/disable WLAN!");
			Toast.makeText(getActivity(), "Failed to set the WLAN Config.",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response wlanConfig) {
			Log.i(CommandCenterActivity.TAG, "Updated the wlan config.");
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

	/**
	 * Should be run when a WLAN object has been returned and the list of AP's
	 * shoudl be updated
	 * 
	 * @param wlan
	 */
	public void updateWlanList(StatusWlan wlan) {
		waps = wlan.getRadio().get(0).getSurvey(); // might need to
													// try to add
													// dual band
													// support.
		updateWapList(waps);
	}

	public void updateWapList(ArrayList<WAP> waps) {
		rows = new ArrayList<WlanListRow>();
		if (adapter == null) {
			adapter = new WlanAdapter(getActivity(), rows);
			setListAdapter(adapter);
		}

		Resources resources = getResources();
		for (WAP wap : waps) {
			String subtitle = wap.getType() + " - " + wap.getMode();
			String ssid = wap.getSsid();
			if (ssid.equals(""))
				ssid = resources.getString(R.string.wlan_hidden_ssid);
			rows.add(new WlanListRow(wap, ssid, subtitle));
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.wifimenu_sort_alphabetically:
			sortAlphabetically();
			return true;
		case R.id.wifimenu_sort_signal:
			sortSignal();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/**
	 * Sorts the list of AP's this router can see by name.
	 * 
	 */
	public void sortAlphabetically() {
		if (rows != null && rows.size() > 1) {
			Collections.sort(rows, new Comparator<WlanListRow>() {

				@Override
				public int compare(WlanListRow lhs, WlanListRow rhs) {
					return lhs.getWap().getSsid()
							.compareToIgnoreCase(rhs.getWap().getSsid());
				}
			});
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		WlanListRow row = (WlanListRow) (l.getAdapter().getItem(position));
		// Log.w(CommandCenterActivity.TAG, "WAP ID clicked: " + row.getId());

		// CommandCenterActivity activity = (CommandCenterActivity)
		// getActivity();

		// authInfo = activity.getAuthInfo();
		WifiWanDialog wwFragment = new WifiWanDialog();
		wwFragment.setData(row.getWap(), authInfo);
		wwFragment
				.show(getActivity().getSupportFragmentManager(), "WAPConfirm");
	}

	/**
	 * Sorts the list of AP's this router can see by signal strength.
	 * 
	 */
	public void sortSignal() {
		if (rows != null && rows.size() > 1) {
			Collections.sort(rows, new Comparator<WlanListRow>() {

				@Override
				public int compare(WlanListRow lhs, WlanListRow rhs) {
					Integer lhsRssi = lhs.getWap().getRssi();
					Integer rhsRssi = rhs.getWap().getRssi();
					return rhsRssi.compareTo(lhsRssi);
				}
			});
			adapter.notifyDataSetChanged();
		}
	}

}
