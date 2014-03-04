package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.activities.SpiceActivity;
import com.cs481.mobilemapper.listrows.DashboardListRow;

public class DashboardFragment extends ListFragment {
	private final int lWLAN = 0;
	private final int lLAN = 1;
	private final int lWAN = 2;
	private final int lGPIO = 3;
	private final int lABOUT = 4;
	private AuthInfo authInfo;
	private int currentSelection = -1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			// rows = savedInstanceState.getP
			authInfo = savedInstanceState.getParcelable("authInfo");
			currentSelection = savedInstanceState
					.getInt("currentSelection", -1);
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
		outState.putParcelable("authInfo", authInfo);
		outState.putInt("currentSelection", currentSelection);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_dash, container, false);
	}

	public static DashboardFragment newInstance(AuthInfo authInfo) {
		DashboardFragment routerFrag = new DashboardFragment();

		Bundle args = new Bundle();
		args.putParcelable("authInfo", authInfo);
		routerFrag.setArguments(args);

		return routerFrag;
	}

	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		Resources resources = getResources();

		sa.setTitle(resources.getString(R.string.dashboard_title));
		sa.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD); // makes
																					// the
																					// dropdown
																					// list
																					// appear
		sa.getActionBar().setDisplayShowTitleEnabled(true);

		ArrayList<DashboardListRow> rows = new ArrayList<DashboardListRow>();
		rows.add(new DashboardListRow(lWLAN, resources
				.getString(R.string.wireless), "Partially Operational"));
		rows.add(new DashboardListRow(lLAN, resources.getString(R.string.lan),
				"Non Operational"));
		rows.add(new DashboardListRow(lWAN, resources.getString(R.string.wan),
				"Non Operational"));
		rows.add(new DashboardListRow(lGPIO,
				resources.getString(R.string.gpio), "Fully Operational"));
		rows.add(new DashboardListRow(lABOUT, resources
				.getString(R.string.routerinfo), "In Progress"));
		setListAdapter(new DashboardAdapter(getActivity(), rows));

		boolean isDualPane = (getActivity().findViewById(
				R.id.rightside_fragment) == null);
		if (isDualPane) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			getListView().setItemChecked(currentSelection, true);
		}
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

			// Highlight selected item
			if (position == currentSelection) {
				rowView.setBackgroundColor((rowView.getResources()
						.getColor(R.color.TransparentWhite)));
			} else {
				Drawable sel = rowView.getResources().getDrawable(
						R.drawable.listview_background);
				rowView.setBackgroundDrawable(sel); // have to use because we
													// use API 14 and 15. Only
													// added in 16.
			}

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
			GPIOFragment gpioFragment = GPIOFragment.newInstance(authInfo);

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
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
			break;
		case lWLAN: {
			Log.i(CommandCenterActivity.TAG, "WLAN WAS CLICKED");
			WifiClientFragment wlanFragment = WifiClientFragment
					.newInstance(authInfo);

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
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
			break;
		case lABOUT: {
			Log.i(CommandCenterActivity.TAG, "ABOUT WAS CLICKED");
			RouterInfoFragment infoFragment = RouterInfoFragment
					.newInstance(authInfo);

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments
			// firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();

			// check if the parent activity is dual pane based.
			CommandCenterActivity parent = (CommandCenterActivity) getActivity();
			if (parent.isDualPane()) {
				transaction.replace(R.id.rightside_fragment, infoFragment);
			} else {
				transaction.replace(R.id.leftside_fragment, infoFragment);
			}
			transaction.addToBackStack(null);
			transaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
			break;

		default:
			super.onListItemClick(l, v, position, id);
			return;
		}

		if (((CommandCenterActivity) getActivity())
				.findViewById(R.id.rightside_fragment) != null) {
			currentSelection = position;
			getListView().setItemChecked(currentSelection, true);
		}
	}

}
