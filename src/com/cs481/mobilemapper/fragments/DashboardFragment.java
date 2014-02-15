package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.DashboardListRow;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;

public class DashboardFragment extends ListFragment {
	private final int lWLAN = 0;
	private final int lLAN = 1;
	private final int lWAN = 2;
	private final int lGPIO = 3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_dash, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		Resources resources = getResources();

		sa.setTitle(resources.getString(R.string.dashboard_title));
		
		ArrayList<DashboardListRow> rows = new ArrayList<DashboardListRow>();
		rows.add(new DashboardListRow(lWLAN, resources.getString(R.string.wireless), "Partially Operational"));
		rows.add(new DashboardListRow(lLAN, resources.getString(R.string.lan), "Non Operational"));
		rows.add(new DashboardListRow(lWAN, resources.getString(R.string.wan), "Non Operational"));
		rows.add(new DashboardListRow(lGPIO, resources.getString(R.string.gpio), "Partially Operational"));
		setListAdapter(new DashboardAdapter(getActivity(), rows));
	}

	public class DashboardAdapter extends ArrayAdapter<DashboardListRow> {
		private final Context context;
		private final ArrayList<DashboardListRow> rows;

		public DashboardAdapter(Context context,
				ArrayList<DashboardListRow> rows) {
			super(context, R.layout.listrow_ecm_routers, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public DashboardListRow getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.listrow_dashboard, parent,
					false);
			TextView title = (TextView) rowView
					.findViewById(R.id.listview_title);
			title.setText(rows.get(position).getTitle());
			TextView subtitle = (TextView) rowView
					.findViewById(R.id.listview_subtitle);
			subtitle.setText(rows.get(position).getSubtitle());
			return rowView;
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Log.w(CommandCenterActivity.TAG, "Item was clicked at pos " + position
				+ ", id " + id);
		DashboardListRow row = (DashboardListRow) (l.getAdapter()
				.getItem(position));
		switch (row.getId()) {
		// case lWLAN:
		// case lLAN:
		// case lWAN:
		case lGPIO: {
			// Create a new Fragment to be placed in the activity layout
			GPIOFragment gpioFragment = new GPIOFragment();

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments
			// firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();

			// check if the parent activity is dual pane based.
			CommandCenterActivity parent = (CommandCenterActivity) getActivity();
			if (parent.isDualPane()) {
				transaction.replace(R.id.rightside_fragment, gpioFragment);
			} else {
				transaction.replace(R.id.leftside_fragment, gpioFragment);
			}
			transaction.addToBackStack(null);
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
			break;
		case lWLAN: {
			Log.i(CommandCenterActivity.TAG, "WLAN WAS CLICKED");
			WlanFragment wlanFragment = new WlanFragment();

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments
			// firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();

			// check if the parent activity is dual pane based.
			CommandCenterActivity parent = (CommandCenterActivity) getActivity();
			if (parent.isDualPane()) {
				transaction.replace(R.id.rightside_fragment, wlanFragment);
			} else {
				transaction.replace(R.id.leftside_fragment, wlanFragment);
			}
			transaction.addToBackStack(null);
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
			break;

		default:
			super.onListItemClick(l, v, position, id);
		}

	}

}
