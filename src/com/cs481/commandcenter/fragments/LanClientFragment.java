package com.cs481.commandcenter.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ActionBar;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.listrows.ClientListRow;
import com.cs481.commandcenter.listrows.WlanListRow;
import com.cs481.commandcenter.responses.GetRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.config.wwan.Radio;
import com.cs481.commandcenter.responses.config.wwan.WANProfile;
import com.cs481.commandcenter.responses.config.wwan.WWAN;
import com.cs481.commandcenter.responses.status.lan.Client;
import com.cs481.commandcenter.responses.status.wlan.StatusWlan;
import com.cs481.commandcenter.responses.status.wlan.WAP;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class LanClientFragment extends ListFragment implements
		OnRefreshListener, ActionBar.OnNavigationListener {

	private PullToRefreshLayout mPullToRefreshLayout;
	private ProgressDialog progressDialog;
	private SpiceManager spiceManager;
	private AuthInfo authInfo;
	
	
	
	private ArrayList<ClientListRow> rows;
	private ArrayList<Client> clients;
	private SpinnerAdapter mSpinnerAdapter;
	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			clients = savedInstanceState.getParcelable("clients");
			authInfo = savedInstanceState.getParcelable("authInfo");
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
		if (clients == null) {
			clients = new ArrayList<Client>(); //prevents null pointer on iteration
		}
		//outState.putParcelable("clients", clients);
		outState.putParcelable("authInfo", authInfo);
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
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST); // makes
																				// the
																				// dropdown
																				// list
																				// appear

		//set the callbacks.
		sa.getActionBar().setListNavigationCallbacks(mSpinnerAdapter, this);
		Log.i(CommandCenterActivity.TAG, "Hiding title bar");
		sa.getActionBar().setDisplayShowTitleEnabled(false);
		
		spiceManager = sa.getSpiceManager();
//		if (shouldLoadData) {
//			readWlanWANConfig(true);
//			readClientMode();
//			shouldLoadData = false;
//		}
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
			title.setText("");
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
			String text = "";
			dropdownItem.setText(text);
			return dropdownItem;

			// return super.getView(position, null, parent);
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		readWlanWANConfig(false);
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
//				updateWlanList(wlan);
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
			if (!isAdded()){
				return;
			}
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
			if (!isAdded()) {
				return;
			}
			if (response.getResponseInfo().getSuccess()) {
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
//	public void updateWlanList(StatusWlan wlan) {
//		waps = wlan.getRadio().get(0).getSurvey(); // might need to
//													// try to add
//													// dual band
//													// support.
//		updateWapList(waps);
//	}

//	public void updateWapList(ArrayList<WAP> waps) {
//		rows = new ArrayList<WlanListRow>();
//		//if (adapter == null) {
//			adapter = new WlanAdapter(getActivity(), rows);
//			setListAdapter(adapter);
//		//}
//
//		Resources resources = getResources();
//		for (WAP wap : waps) {
//			String subtitle = wap.getType() + " - " + wap.getMode();
//			String ssid = wap.getSsid();
//			if (ssid.equals(""))
//				ssid = resources.getString(R.string.wlan_hidden_ssid);
//			rows.add(new WlanListRow(wap, ssid, subtitle));
//		}
//		adapter.notifyDataSetChanged();
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
//		WlanListRow row = (WlanListRow) (l.getAdapter().getItem(position));
		// Log.w(CommandCenterActivity.TAG, "WAP ID clicked: " + row.getId());

		// CommandCenterActivity activity = (CommandCenterActivity)
		// getActivity();

		// authInfo = activity.getAuthInfo();
//		WifiWanDialogFragment wwFragment = WifiWanDialogFragment
//				.newInstance(this);
//		wwFragment.setData(row.getWap(), authInfo);
//		wwFragment
//				.show(getActivity().getSupportFragmentManager(), "WAPConfirm");
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
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(CommandCenterActivity.TAG, "Activity result.");
		
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		
		return true;
	}

}
