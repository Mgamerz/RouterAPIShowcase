package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.activities.LoginActivity;
import com.cs481.mobilemapper.activities.SpiceActivity;
import com.cs481.mobilemapper.dialog.RouterConfirmDialogFragment;
import com.cs481.mobilemapper.listrows.RouterListRow;
import com.cs481.mobilemapper.responses.ecm.routers.Router;

public class ECMRoutersFragment extends ListFragment implements
		OnRefreshListener {
	// private boolean checking = true;
	private PullToRefreshLayout mPullToRefreshLayout;
	ProgressDialog progressDialog;
	// private SpiceManager spiceManager;
	private AuthInfo authInfo;
	// private ArrayList<RouterListRow> rows;
	private ArrayList<Router> routers;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			// rows = savedInstanceState.getP
			routers = savedInstanceState.getParcelableArrayList("routers");
			authInfo = savedInstanceState.getParcelable("authInfo");
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				routers = passedArgs.getParcelableArrayList("routers");
				authInfo = passedArgs.getParcelable("authInfo");
			} else {
				routers = new ArrayList<Router>();
			}
		}
	}

	public static ECMRoutersFragment newInstance(ArrayList<Router> routers, AuthInfo authInfo) {
		ECMRoutersFragment routerFrag = new ECMRoutersFragment();

		Bundle args = new Bundle();
		args.putParcelableArrayList("routers", routers);
		args.putParcelable("authInfo", authInfo);
		routerFrag.setArguments(args);

		return routerFrag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_ecmrouters, container,
				false);
		ArrayList<RouterListRow> rows = new ArrayList<RouterListRow>();
		/*ArrayList<Router> routers = ((LoginActivity) getActivity())
				.getRouters().getData();*/

		for (Router router : routers) {
			String rId = router.getId();
			String name = router.getName();
			String online = router.getState();
			rows.add(new RouterListRow(router, rId, name, online));
		}

		setListAdapter(new RouterAdapter(getActivity(), rows));
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		//Save data on rotate. This bundle will be passed to onCreate() by Android.
		Log.i(CommandCenterActivity.TAG, "Saving instance");
		outState.putParcelableArrayList("routers", routers);
		outState.putParcelable("authInfo", authInfo);
	}


	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		Resources resources = getResources();
		sa.setTitle(resources.getString(R.string.ecmrouters_actionbar_title)); // TODO change to string resource
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
			View rowView = inflater.inflate(R.layout.listrow_ecm_routers,
					parent, false);

			// Title text
			TextView title = (TextView) rowView
					.findViewById(R.id.routerrow_title);
			title.setText(rows.get(position).getTitle());

			// Subtitle text
			TextView subtitle = (TextView) rowView
					.findViewById(R.id.routerrow_subtitle);
			subtitle.setText(rows.get(position).getSubtitle());

			// Router image
			ImageView router_icon = (ImageView) rowView
					.findViewById(R.id.routerrow_image);
			Router router = rows.get(position).getRouter();

			if (router.getState().equals("offline")) {
				rowView.setEnabled(false);
				router_icon.setAlpha(.50f); // set transparency to half.
			}
			return rowView;
		}
		
		/*@Override
		public boolean areAllItemsEnabled() {
			return false;
		}*/
		
		@Override
		public boolean isEnabled(int position) {
			RouterListRow row = getItem(position);
			if (row.getRouter().getState().equals("offline")){
				return false;
			}
			return true;
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		RouterListRow row = (RouterListRow) (l.getAdapter().getItem(position));
		Log.w(CommandCenterActivity.TAG, "Router ID clicked: " + row.getId());

		//LoginActivity activity = (LoginActivity) getActivity();

		//authInfo = activity.getAuthInfo();
		Log.i(CommandCenterActivity.TAG, "Authinfo: " + authInfo);
		Log.i(CommandCenterActivity.TAG, "Router: " + row.getRouter());

		authInfo.setRouterId(row.getRouter().getId());
		//activity.setAuthInfo(authInfo);
		// activity.setRouter(row.getRouter());
		RouterConfirmDialogFragment rcFragment = RouterConfirmDialogFragment.newInstance(row.getRouter(), authInfo);
		rcFragment.show(getFragmentManager(), "RouterConfirm");
	}
}