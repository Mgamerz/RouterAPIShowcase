package com.cs481.commandcenter.fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.listrows.ClientListRow;
import com.cs481.commandcenter.responses.GetRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.status.lan.Client;
import com.cs481.commandcenter.responses.status.lan.Lan;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class LanClientFragment extends ListFragment implements
		OnRefreshListener {

	private PullToRefreshLayout mPullToRefreshLayout;
	private ProgressDialog progressDialog;
	private SpiceManager spiceManager;
	private AuthInfo authInfo;

	private ClientAdapter adapter;
	private boolean shouldLoadData = true;
	private ArrayList<ClientListRow> rows;
	private ArrayList<Client> clients;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			clients = savedInstanceState.getParcelableArrayList("clients");
			authInfo = savedInstanceState.getParcelable("authInfo");
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
				clients = passedArgs.getParcelableArrayList("clients");
			}
		}
	}

	public static LanClientFragment newInstance(AuthInfo authInfo) {
		LanClientFragment lcFrag = new LanClientFragment();

		Bundle args = new Bundle();
		args.putParcelable("authInfo", authInfo);
		lcFrag.setArguments(args);

		return lcFrag;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
		Log.i(CommandCenterActivity.TAG, "Saving instance");
		if (clients == null) {
			clients = new ArrayList<Client>(); // prevents null pointer on
			// iteration
		}
		outState.putParcelableArrayList("clients", clients);
		outState.putParcelable("authInfo", authInfo);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_clients, container, false);
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
		// onStart is called before onResume()
		SpiceActivity sa = (SpiceActivity) getActivity();
		spiceManager = sa.getSpiceManager();
		sa.getActionBar().setDisplayShowTitleEnabled(true);
		sa.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		sa.getActionBar()
				.setTitle(getResources().getString(R.string.lan_title));
		if (shouldLoadData) {
			readClients();
			shouldLoadData = false;
		}
	}

	private void readClients() {
		GetRequest clientReq = new GetRequest(getActivity(), authInfo,
				"status/lan", Lan.class, "LanGet");
		String lastRequestCacheKey = clientReq.createCacheKey();
		spiceManager.execute(clientReq, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED,
				new ClientsGetRequestListener());
	}

	public class ClientAdapter extends ArrayAdapter<ClientListRow> {
		private final Context context;
		private final ArrayList<ClientListRow> rows;

		public ClientAdapter(Context context, ArrayList<ClientListRow> rows) {
			super(context, R.layout.listrow_clients, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public ClientListRow getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.listrow_clients, parent,
					false);

			Client cli = rows.get(position).getClient();

			// mac address
			TextView title = (TextView) rowView
					.findViewById(R.id.clients_mac_value);
			title.setText(cli.getMac());

			// ip address
			title = (TextView) rowView.findViewById(R.id.ip_address_value);
			title.setText(cli.getIp_address());

			return rowView;
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		readClients();
	}

	private class ClientsGetRequestListener implements
			RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			if (!isAdded()) {
				return;
			}
			Resources resources = getResources();
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			Log.i(CommandCenterActivity.TAG, "Failed to read LAN!");
			Toast.makeText(getActivity(),
					resources.getString(R.string.lan_clients_failure),
					Toast.LENGTH_SHORT).show();
			mPullToRefreshLayout.setRefreshComplete();
		}

		@Override
		public void onRequestSuccess(Response response) {
			// update your UI
			if (!isAdded()) {
				return;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			if (response.getResponseInfo().getSuccess()) {
				if (response.getData() == null)
					return;
				Lan lan = (Lan) response.getData();
				ArrayList<Client> clients = lan.getClients();
				Log.i(CommandCenterActivity.TAG,
						"LAN Client request successful");
				updateClientList(clients);
			} else {

				Toast.makeText(getActivity(),
						response.getResponseInfo().getReason(),
						Toast.LENGTH_LONG).show();
			}
			mPullToRefreshLayout.setRefreshComplete();
		}
	}

	public void updateClientList(ArrayList<Client> clients) {
		Log.i(CommandCenterActivity.TAG, "Updating client list");
		rows = new ArrayList<ClientListRow>();
		adapter = new ClientAdapter(getActivity(), rows);
		setListAdapter(adapter);
		for (Client cli : clients) {
			String ip = cli.getIp_address();
			String mac = cli.getMac();
			rows.add(new ClientListRow(cli, mac, ip));
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// up navigation
			Log.i(CommandCenterActivity.TAG,
					"UP in LAN Clients is being handled.");
			FragmentManager fm = getActivity().getSupportFragmentManager();
			fm.popBackStack();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ClientListRow row = (ClientListRow) (l.getAdapter().getItem(position));
		Log.w(CommandCenterActivity.TAG, "Client IP clicked: "
				+ row.getClient().getIp_address());

		/** TODO: Add kick/ban options */
		// CommandCenterActivity activity = (CommandCenterActivity)
		// getActivity();

		// authInfo = activity.getAuthInfo();
		// WifiWanDialogFragment wwFragment = WifiWanDialogFragment
		// .newInstance(this);
		// wwFragment.setData(row.getWap(), authInfo);
		// wwFragment
		// .show(getActivity().getSupportFragmentManager(), "WAPConfirm");
	}

}
