package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.content.Context;
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
import android.widget.Toast;

import com.cs481.mobilemapper.CommandCenter;
import com.cs481.mobilemapper.ListRow;
import com.cs481.mobilemapper.R;

public class DashboardFragment extends ListFragment implements
		OnRefreshListener {
	private PullToRefreshLayout mPullToRefreshLayout;
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
		Toast.makeText(getActivity(), "Welcome...", Toast.LENGTH_SHORT).show();
		Toast.makeText(getActivity(), "... Professor.", Toast.LENGTH_SHORT)
				.show();
		// /You will setup the action bar with pull to refresh layout
		mPullToRefreshLayout = (PullToRefreshLayout) getView().findViewById(
				R.id.ptr_layout);
		ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable()
				.listener(this).setup(mPullToRefreshLayout);

		// ListView list = (ListView)
		// getView().findViewById(R.id.overview_list);
		ArrayList<ListRow> rows = new ArrayList<ListRow>();

		rows.add(new ListRow(lWLAN, "Wireless", "2 CLIENTS"));
		rows.add(new ListRow(lLAN, "LAN", "DHCP - 2 Clients"));
		rows.add(new ListRow(lWAN, "WAN", "3 Forwarded ports"));
		rows.add(new ListRow(lGPIO, "GPIO", "Dimmed Mode"));
		setListAdapter(new DashboardAdapter(getActivity(), rows));
	}

	public class DashboardAdapter extends ArrayAdapter<ListRow> {
		private final Context context;
		private final ArrayList<ListRow> rows;

		public DashboardAdapter(Context context, ArrayList<ListRow> rows) {
			super(context, R.layout.listview_row, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public ListRow getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.listview_row, parent,
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
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Log.w(CommandCenter.TAG, "Item was clicked at pos " + position
				+ ", id " + id);
		ListRow row = (ListRow) (l.getAdapter().getItem(position));
		switch (row.getId()) {
		// case lWLAN:
		// case lLAN:
		// case lWAN:
		case lGPIO:
			// Create a new Fragment to be placed in the activity layout
			GPIOFragment gpioFragment = new GPIOFragment();

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments
			// firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.leftside_fragment, gpioFragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		default:
			super.onListItemClick(l, v, position, id);
		}

	}

}
