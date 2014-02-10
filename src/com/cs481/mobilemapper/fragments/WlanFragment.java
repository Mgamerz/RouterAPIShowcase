package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.WlanListRow;
import com.cs481.mobilemapper.responses.status.wlan.WAP;
import com.cs481.mobilemapper.responses.status.wlan.Wlan;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class WlanFragment extends ListFragment implements OnRefreshListener {
	private PullToRefreshLayout mPullToRefreshLayout;
	ProgressDialog progressDialog;
	private SpiceManager spiceManager;

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
		ActionBarPullToRefresh.from(getActivity())

		// We need to insert the PullToRefreshLayout into the Fragment's
		// ViewGroup
				.insertLayoutInto(viewGroup)

				// We need to mark the ListView and it's Empty View as pullable
				// This is because they are not dirent children of the ViewGroup
				.theseChildrenArePullable(getListView(),
						getListView().getEmptyView())

				// We can now complete the setup as desired
				.listener(this).setup(mPullToRefreshLayout);
	}

	@Override
	public void onStart() {
		super.onStart();
		// /You will setup the action bar with pull to refresh layout
		mPullToRefreshLayout = (PullToRefreshLayout) getView().findViewById(
				R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(mPullToRefreshLayout);

		// ListView list = (ListView)
		// getView().findViewById(R.id.overview_list);
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle("WLAN"); // TODO change to string resource
		spiceManager = sa.getSpiceManager();
		readWlanConfig(true);
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
			
			//title
			TextView title = (TextView) rowView
					.findViewById(R.id.wlan_ssid_text);
			title.setText(rows.get(position).getTitle());
			
			//signal indicator
			ImageView signalStrengthIcon = (ImageView) rowView.findViewById(R.id.signal_strength);
			int dbm = rows.get(position).getWap().getRssi();
			int signalQuality;
		    if(dbm <= -100){
		        signalQuality = 0;
		    }
		    else if(dbm >= -50) {
		        signalQuality = 100;
		    }
		    else {
		        signalQuality = Utility.rssiToSignalStrength(dbm)-1;
		        if (signalQuality < 0){
		        	signalQuality = 0; //rollunder check
		        }
		    }
			
			int signalStrength = signalQuality/25;
			
			signalStrengthIcon.setImageLevel(signalStrength);
			
			//subtitle
			TextView subtitle = (TextView) rowView
					.findViewById(R.id.wlan_type_mode_text);
			subtitle.setText(rows.get(position).getSubtitle()+" - dBm: "+dbm+" -> SS: "+signalStrength);

			return rowView;
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		readWlanConfig(false);
	}

	private void readWlanConfig(boolean dialog) {
		// perform the request.
		com.cs481.mobilemapper.responses.status.wlan.GetRequest request = new com.cs481.mobilemapper.responses.status.wlan.GetRequest(
				((CommandCenterActivity) getActivity()).getAuthInfo());
		String lastRequestCacheKey = request.createCacheKey();

		if (dialog) {
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage("Reading WLAN Configuration");
			progressDialog.show();
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
		}

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED, new WLANGetRequestListener());
	}

	private class WLANGetRequestListener implements RequestListener<Wlan> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			progressDialog.dismiss();
			mPullToRefreshLayout.setRefreshComplete();
			Log.i(CommandCenterActivity.TAG, "Failed to read WLAN!");
			Toast.makeText(getActivity(), "Failed to read WLAN configuration",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Wlan wlan) {
			// update your UI
			progressDialog.dismiss();
			mPullToRefreshLayout.setRefreshComplete();
			Log.i(CommandCenterActivity.TAG, "Succeded reading from WLAN!");
			updateWlanList(wlan);
		}

	}

	public void updateWlanList(Wlan wlan) {
		Log.i(CommandCenterActivity.TAG, wlan.toString());
		ArrayList<WlanListRow> rows = new ArrayList<WlanListRow>();
		ArrayList<WAP> waps = wlan.getData().getRadio().get(0).getSurvey(); // TODO:
																			// Add
																			// dual
																			// band
																			// support
		for (WAP wap : waps) {
			String subtitle = wap.getType() + " - " + wap.getMode();
			String ssid = wap.getSsid();
			if (ssid.equals(""))
				ssid = "<Hidden>";
			rows.add(new WlanListRow(wap, ssid, subtitle));
		}
		setListAdapter(new WlanAdapter(getActivity(), rows));
	}

}
