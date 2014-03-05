package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.activities.SpiceActivity;
import com.cs481.mobilemapper.dialog.WifiClientChangeDialog;
import com.cs481.mobilemapper.dialog.WifiWanDialogFragment;
import com.cs481.mobilemapper.listrows.WlanListRow;
import com.cs481.mobilemapper.responses.GetRequest;
import com.cs481.mobilemapper.responses.PostRequest;
import com.cs481.mobilemapper.responses.PutRequest;
import com.cs481.mobilemapper.responses.Response;
import com.cs481.mobilemapper.responses.config.wwan.Radio;
import com.cs481.mobilemapper.responses.config.wwan.WANProfile;
import com.cs481.mobilemapper.responses.config.wwan.WWAN;
import com.cs481.mobilemapper.responses.status.wlan.StatusWlan;
import com.cs481.mobilemapper.responses.status.wlan.WAP;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class WifiClientFragment extends ListFragment implements
		OnRefreshListener, ActionBar.OnNavigationListener {

	private final int WIFICLIENT_DISABLED = 0;
	private final int WIFICLIENT_WAN = 1;
	private final int WIFICLIENT_BRIDGE = 2;

	public static final int WAN_CONNECT_FRAGMENT = 1;
	public static final int WAN_CHANGE_FRAGMENT = 2;

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
	private ArrayList<WANProfile> wanprofiles;
	private SpinnerAdapter mSpinnerAdapter;
	private int temporaryClientMode = -1;
	private int currentClientMode = 0;
	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			waps = savedInstanceState.getParcelableArrayList("waps");
			wanprofiles = savedInstanceState
					.getParcelableArrayList("wanprofiles");
			authInfo = savedInstanceState.getParcelable("authInfo");
			shouldLoadData = savedInstanceState.getBoolean("shouldLoadData");
			wifiStateEnabled = savedInstanceState
					.getBoolean("wifiStateEnabled");
			wifiState = savedInstanceState.getBoolean("wifiState");
			currentClientMode = savedInstanceState.getInt("currentClientMode");
			temporaryClientMode = savedInstanceState
					.getInt("temporaryClientMode");

			Log.i(CommandCenterActivity.TAG, "current: " + currentClientMode
					+ ", temp: " + temporaryClientMode);
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
			}
		}
	}

	public static WifiClientFragment newInstance(AuthInfo authInfo) {
		WifiClientFragment wawFrag = new WifiClientFragment();

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
		if (waps == null) {
			waps = new ArrayList<WAP>(); //prevents null pointer on iteration
		}
		outState.putParcelableArrayList("waps", waps);
		outState.putParcelableArrayList("wanprofiles", wanprofiles);
		outState.putParcelable("authInfo", authInfo);
		outState.putBoolean("shouldLoadData", shouldLoadData);
		outState.putBoolean("wifiState", wifiState);
		outState.putBoolean("wifiStateEnabled", wifiStateEnabled);
		outState.putInt("currentClientMode", currentClientMode);
		outState.putInt("temporaryClientMode", temporaryClientMode);
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
		// Setup navigation stuff.
		// onStart is called before onResume()
		ArrayList<String> values = new ArrayList<String>(
				Arrays.asList(getResources().getStringArray(
						R.array.wificlient_values)));
		mSpinnerAdapter = new DropdownAdapter(getActivity(), values,
				getActivity().getActionBar().getSubtitle().toString());

	}

	@Override
	public void onResume() {
		super.onResume();
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST); // makes
																				// the
																				// dropdown
																				// list
																				// appear

		// set the title before we set the callbacks.
		// sa.getActionBar().setListNavigationCallbacks(mSpinnerAdapter, null);

		sa.getActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);
		sa.getActionBar().setDisplayShowTitleEnabled(false);
		sa.setTitle(getResources().getString(R.string.wifiwan_title)); // TODO
																		// change
																		// to
																		// string
																		// resource
		spiceManager = sa.getSpiceManager();
		if (shouldLoadData) {
			readWlanWANConfig(true);
			readClientMode();
			shouldLoadData = false;
		} else {
			// Log.i(CommandCenterActivity.TAG, waps.toString());
			updateWapList(waps);
			if (temporaryClientMode != -1) {
				sa.getActionBar().setSelectedNavigationItem(temporaryClientMode);
			} else {
				sa.getActionBar().setSelectedNavigationItem(currentClientMode);

			}

		}
	}

	private void readClientMode() {
		// TODO Auto-generated method stub
		GetRequest clientModeReq = new GetRequest(authInfo,
				"config/wwan/radio/0/mode", String.class, "ConfigClientGet");
		String lastRequestCacheKey = clientModeReq.createCacheKey();
		spiceManager.execute(clientModeReq, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED,
				new WIFIClientModeGetRequestListener());
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

	public class DropdownAdapter extends ArrayAdapter<String> implements
			SpinnerAdapter {
		private final Context context;
		private final ArrayList<String> rows;
		private String subtitle;

		public DropdownAdapter(Context context, ArrayList<String> rows,
				String subtitle) {
			super(context, R.layout.actionbar_spinner_item, rows);
			this.context = context;
			this.rows = rows;
			this.subtitle = subtitle;
		}

		@Override
		public String getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// actionbar view

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.actionbar_spinner_item,
					parent, false);
			TextView title = (TextView) rowView
					.findViewById(R.id.actionbar_title);
			title.setText(dropdownToString(position));

			TextView subtitleView = (TextView) rowView
					.findViewById(R.id.actionbar_subtitle);
			subtitleView.setText(subtitle);

			return rowView;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// Return a view which appears in the spinner list.

			// Ignoring convertView to make things simpler, considering
			// we have different types of views. If the list is long, think
			// twice!

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, parent,
					false);

			TextView dropdownItem = (TextView) rowView
					.findViewById(android.R.id.text1);
			String text = dropdownToString(position);
			dropdownItem.setText(text);
			return dropdownItem;

			// return super.getView(position, null, parent);
		}
	}

	private String dropdownToString(int position) {
		switch (position) {
		case WIFICLIENT_WAN:
			return getResources().getString(R.string.wifiwan_title);
		case WIFICLIENT_BRIDGE:
			return getResources().getString(R.string.wifibridge_title);
		default:
			return getResources().getString(R.string.wificlient_title);
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		readWlanWANConfig(false);

		/*
		 * GetRequest request = new GetRequest( authInfo, "config/wlan",
		 * ConfigWlan.class, "configwlanget"); String lastRequestCacheKey =
		 * request.createCacheKey();
		 * 
		 * spiceManager.execute(request, lastRequestCacheKey,
		 * DurationInMillis.ALWAYS_EXPIRED, new WLANGetRequestListener());
		 */
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// super.onCreateOptionsMenu(menu, inflater);
		this.menu = menu;
		inflater.inflate(R.menu.wifiwan_menu, this.menu);
	}

	private void readWlanWANConfig(boolean dialog) {
		// perform the request.
		GetRequest wapListrequest = new GetRequest(authInfo, "status/wlan",
				StatusWlan.class, "statuswlanget");
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
		}

		spiceManager.execute(wapListrequest, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED,
				new WLANStatusGetRequestListener());

		// get wwan profiles

		GetRequest profilesRequest = new GetRequest(authInfo, "config/wwan",
				WWAN.class, "configwwanget");

		spiceManager.execute(profilesRequest, profilesRequest.createCacheKey(),
				DurationInMillis.ALWAYS_EXPIRED,
				new WANProfilesGetRequestListener());
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
			} else {

				Toast.makeText(getActivity(),
						response.getResponseInfo().getReason(),
						Toast.LENGTH_LONG).show();
			}
			mPullToRefreshLayout.setRefreshComplete();

		}
	}

	private class WIFIClientModeGetRequestListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Resources resources = getResources();
			Log.i(CommandCenterActivity.TAG,
					"Failed to read the client wan mode!");
			Toast.makeText(getActivity(),
					resources.getString(R.string.wlan_get_config_failure),
					Toast.LENGTH_SHORT).show();
			mPullToRefreshLayout.setRefreshComplete();
		}

		@Override
		public void onRequestSuccess(Response response) {
			// update your UI

			if (response.getResponseInfo().getSuccess()) {
				String currentMode = (String) response.getData();

				// convert string to position of the dropdown list.
				int position = 0;
				if (currentMode.equals("bridge")) {
					position = WIFICLIENT_BRIDGE;
				} else if (currentMode.equals("wwan")) {
					position = WIFICLIENT_WAN;
				} else {
					position = WIFICLIENT_DISABLED;
				}

				getActivity().getActionBar()
						.setSelectedNavigationItem(position);
				currentClientMode = position;
				Log.i(CommandCenterActivity.TAG,
						"WWAN Mode get request successful");
			} else {

				Toast.makeText(getActivity(),
						response.getResponseInfo().getReason(),
						Toast.LENGTH_LONG).show();
			}
			mPullToRefreshLayout.setRefreshComplete();

		}
	}

	/**
	 * Should be run when a WLAN object has been returned and the list of AP's
	 * should be updated
	 * 
	 * @param wlan
	 *            wlan response object
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
		WifiWanDialogFragment wwFragment = WifiWanDialogFragment
				.newInstance(this);
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
		PostRequest request = new PostRequest(wanprofile, authInfo,
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
			WifiClientFragment.this.wanprofiles = wanprofiles;
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
		Log.i(CommandCenterActivity.TAG, "Activity result.");
		switch (requestCode) {
		case WAN_CHANGE_FRAGMENT:
			if (resultCode == Activity.RESULT_OK) {
				// After Ok code.
				Log.i(CommandCenterActivity.TAG, "user pressed ok");
				currentClientMode = getActivity().getActionBar()
						.getSelectedNavigationIndex();
				temporaryClientMode = -1;

				// Send data to server to change modes
				String newMode = getClientMode();
				PutRequest profilesRequest = new PutRequest(newMode, authInfo,
						"config/wwan/radio/0/mode", String.class);

				spiceManager.execute(profilesRequest,
						profilesRequest.createCacheKey(),
						DurationInMillis.ALWAYS_EXPIRED,
						new WIFIClientModePutRequestListener());

			} else if (resultCode == Activity.RESULT_CANCELED) {
				// After Cancel code.
				Log.i(CommandCenterActivity.TAG,
						"user pressed cancel, mode now " + currentClientMode);
				//currentClientMode = temporaryClientMode;
				temporaryClientMode = -1;
				Log.i(CommandCenterActivity.TAG, "reverted mode, mode now "
						+ currentClientMode);

				getActivity().getActionBar().setSelectedNavigationItem(
						currentClientMode);
			}

			break;
		}
	}

	/**
	 * Translates the items in the dropdown list to their respected string names
	 * on the router.
	 * 
	 * @return name of string to put to the router to set that mode.
	 */
	private String getClientMode() {
		// TODO Auto-generated method stub
		int selected = getActivity().getActionBar()
				.getSelectedNavigationIndex();
		Resources resources = getResources();
		switch (selected) {
		case WIFICLIENT_WAN:
			return resources.getString(R.string.wwan);
		case WIFICLIENT_BRIDGE:
			return resources.getString(R.string.bridged);
		default:
			return resources.getString(R.string.disabled);
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (itemPosition == currentClientMode) {
			return true; // nothing changed.
		}

		Log.w(CommandCenterActivity.TAG, "Changing from mode "
				+ currentClientMode + " to mode " + itemPosition);
		temporaryClientMode = itemPosition;
		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		Fragment prev = getActivity().getSupportFragmentManager()
				.findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		DialogFragment dialogFrag = WifiClientChangeDialog
				.newInstance(dropdownToString(itemPosition));
		dialogFrag.setTargetFragment(this, WAN_CHANGE_FRAGMENT);
		dialogFrag.show(ft, "dialog");

		return true;
	}

	/**
	 * Listener for a Client type change to Disabled, WAN or Bridge.
	 * 
	 * @author mjperez
	 * 
	 */
	private class WIFIClientModePutRequestListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.w(CommandCenterActivity.TAG,
					"Failed to update the WiFi Client type!");
			Toast.makeText(getActivity(),
					"Failed to update the WiFi Client type.",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response clientChange) {
			Log.i(CommandCenterActivity.TAG, "Updated the WiFi Client type.");

		}
	}
}
