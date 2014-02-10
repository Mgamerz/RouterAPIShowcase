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
import android.widget.ListView;
import android.widget.TextView;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.LoginActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.RouterListRow;
import com.cs481.mobilemapper.responses.ecm.routers.Router;
import com.octo.android.robospice.SpiceManager;

public class ECMRoutersFragment extends ListFragment implements
		OnRefreshListener {
	private boolean checking = true;
	private PullToRefreshLayout mPullToRefreshLayout;
	ProgressDialog progressDialog;
	private SpiceManager spiceManager;
	private AuthInfo authInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_ecmrouters, container, false);
		ArrayList<RouterListRow> rows = new ArrayList<RouterListRow>();
		ArrayList<Router> routers = ((LoginActivity) getActivity()).getRouters().getData();
		
		for (Router router : routers){
			String rId = router.getId();
			String name = router.getName();
			String online = router.getState();
			rows.add(new RouterListRow(router, rId, name, online));
		}
	
		setListAdapter(new RouterAdapter(getActivity(), rows));
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
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}

	// List adapter.
	public class RouterAdapter extends ArrayAdapter<RouterListRow> {
		private final Context context;
		private final ArrayList<RouterListRow> rows;

		public RouterAdapter(Context context, ArrayList<RouterListRow> rows) {
			super(context, R.layout.listrow_ecm_routers, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public RouterListRow getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.listrow_ecm_routers, parent,
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
		RouterListRow row = (RouterListRow) (l.getAdapter().getItem(position));
		Log.w(CommandCenterActivity.TAG, "Router ID clicked: " + row.getId());
		LoginActivity activity = (LoginActivity) getActivity();
		authInfo = activity.getAuthInfo();
		Log.i(CommandCenterActivity.TAG, "Authinfo: "+authInfo);
		Log.i(CommandCenterActivity.TAG, "Router: "+row.getRouter());
		
		authInfo.setRouterId(row.getRouter().getId());
		activity.setAuthInfo(authInfo);
		//activity.setRouter(row.getRouter());
		RouterConfirmDialogFragment rcFragment = new RouterConfirmDialogFragment();
		rcFragment.setData(row.getRouter(), authInfo);
		rcFragment.show(getFragmentManager(), "RouterConfirm");
	}
}