package com.cs481.commandcenter.fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.listrows.ClientListRow;
import com.cs481.commandcenter.responses.GetRequest;
import com.cs481.commandcenter.responses.PutRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.status.lan.Client;
import com.cs481.commandcenter.responses.status.lan.Lan;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Fragment that allows users to kick and ban clients on a locally connected
 * router
 * 
 * @author Sean Wright, Mike Perez, Melissa Neibaur
 */

public class LanClientFragment extends Fragment implements OnRefreshListener {

	// private PullToRefreshLayout mPullToRefreshLayout;
	private static int LIST_NOCLIENTS = 4;
	private SpiceManager spiceManager;
	private AuthInfo authInfo;
	private ClientAdapter adapter;
	private boolean shouldLoadData = true;
	private ArrayList<Client> clients;
	private int listState = Utility.CONTENT_LOADING; // default to the loading
														// state
	private ExpandableListView mExpandableList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			clients = savedInstanceState.getParcelableArrayList("clients");
			shouldLoadData = savedInstanceState.getBoolean("shouldLoadData");
			authInfo = savedInstanceState.getParcelable("authInfo");
			listState = savedInstanceState.getInt("listState");
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
			}
		}
	}

	/**
	 * Creqtes a new LANClientFragment with the given parameters.
	 * 
	 * @param authInfo
	 *            authinfo object to connect to the router with
	 * @return LanClientFragment with the authInfo attached
	 */
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
			Log.i(CommandCenterActivity.TAG, "Client list is null");
			clients = new ArrayList<Client>(); // prevents null pointer on
			// iteration
		}
		outState.putParcelableArrayList("clients", clients);
		outState.putBoolean("shouldLoadData", shouldLoadData);
		outState.putParcelable("authInfo", authInfo);
		outState.putInt("listState", listState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_clients, container, false);
		// mExpandableList = (ExpandableListView)
		// v.findViewById(R.id.expandable_clientlist);
		// mExpandableList.setAdapter(adapter);
		if (listState == Utility.CONTENT_LOADED) {
			// hide the loading layout
			LinearLayout loading_layout = (LinearLayout) v.findViewById(R.id.lan_loading_layout);
			loading_layout.setVisibility(View.GONE);
		}
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		// onStart is called before onResume()
		SpiceActivity sa = (SpiceActivity) getActivity();
		spiceManager = sa.getSpiceManager();
		sa.getActionBar().setDisplayShowTitleEnabled(true);
		sa.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		sa.getActionBar().setDisplayHomeAsUpEnabled(true);
		sa.getActionBar().setTitle(getResources().getString(R.string.lan_title));
		if (shouldLoadData) {
			Log.i(CommandCenterActivity.TAG, "shouldLoadData was set to true");
			readClients();
			shouldLoadData = false;
		} else {
			updateClientList(clients);
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		// Check for dualpane - if we are dual pane, make sure we are the selected one in DashboardFragment
		if (isAdded()){
			// ^ Check to make sure we are actually in ready to check. Only happens when input is fast.
			CommandCenterActivity activity = (CommandCenterActivity) getActivity();
			if (activity.isDualPane()){
				//we only care in dual pane really
				DashboardFragment df = (DashboardFragment) activity.getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getName());
				if (df != null){
					//we have a reference to the left hand side of the tablet interface (dashboard fragment)
					df.setCurrentSelection(DashboardFragment.lLAN);
				}
			}
		}
	}

	/**
	 * Performs a network request to read the clients off of the router.
	 */
	private void readClients() {
		GetRequest clientReq = new GetRequest(getActivity(), authInfo, "status/lan", Lan.class, "LanGet");
		String lastRequestCacheKey = clientReq.createCacheKey();
		spiceManager.execute(clientReq, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new ClientsGetRequestListener());
	}

	/**
	 * Adapter that maps client objects to the user interface, including the
	 * drop objects.
	 * 
	 * @author Mike Perez
	 * 
	 */
	public class ClientAdapter extends BaseExpandableListAdapter {
		private LayoutInflater inflater;
		private ArrayList<Client> clients;

		public ClientAdapter(Context context, ArrayList<Client> clients) {
			this.clients = clients;
			inflater = LayoutInflater.from(context);
		}

		@Override
		// counts the number of group/profile items so the list knows how many
		// times calls getGroupView() method
		public int getGroupCount() {
			return clients.size();
		}

		@Override
		// counts the number of children items so the list knows how many times
		// calls getChildView() method
		public int getChildrenCount(int i) {
			return 1; // can be up to 2 - we don't have ban implemented so its
						// only 1.
		}

		@Override
		// gets the title of each InstructionType/group
		public Object getGroup(int i) {
			return clients.get(i).getIp_address();
		}

		@Override
		// gets the name of each item
		public Object getChild(int i, int i1) {
			return clients.get(i);
		}

		@Override
		public long getGroupId(int i) {
			return i;
		}

		@Override
		public long getChildId(int i, int i1) {
			return i1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		// in this method you must set the text to see the InstructionType/group
		// on the list
		public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {

			if (view == null) {
				view = inflater.inflate(R.layout.listrow_clients, viewGroup, false);
			}

			final Client client = (Client) clients.get(i);
			TextView textView = (TextView) view.findViewById(R.id.ip_address_value);
			textView.setText(client.getIp_address());

			textView = (TextView) view.findViewById(R.id.clients_mac_value);
			textView.setText(client.getMac());

			// return the entire view
			return view;
		}

		@Override
		// in this method you must set the text to see the children on the list
		public View getChildView(int profileindex, int profileinfo_index, boolean b, View view, ViewGroup viewGroup) {
			if (view == null) {
				view = inflater.inflate(R.layout.expandable_client, viewGroup, false);
			}

			TextView descriptor = (TextView) view.findViewById(R.id.expandableclient_text);
			Resources resources = getResources();
			switch (profileinfo_index) {
			case 0:
				descriptor.setText(resources.getString(R.string.kick));
				break;
			case 1:
				descriptor.setText(resources.getString(R.string.ban));
				break;
			default:
				descriptor.setText("unknown");
				break;
			}

			// return the entire view
			return view;
		}

		@Override
		public boolean isChildSelectable(int i, int i1) {
			return true;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			/* used to make the notifyDataSetChanged() method work */
			super.registerDataSetObserver(observer);
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		readClients();
	}

	/**
	 * Listener for the get request for the clients.
	 * 
	 * @author Mike Perez
	 * 
	 */
	private class ClientsGetRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			if (!isAdded()) {
				return;
			}
			Resources resources = getResources();
			// update your UI
			Log.i(CommandCenterActivity.TAG, "Failed to read LAN!");
			Toast.makeText(getActivity(), resources.getString(R.string.lan_clients_failure), Toast.LENGTH_SHORT).show();
			// mPullToRefreshLayout.setRefreshComplete();
		}

		@Override
		public void onRequestSuccess(Response response) {
			// update your UI
			if (!isAdded()) {
				return;
			}

			if (response.getResponseInfo().getSuccess()) {
				if (response.getData() == null)
					return;
				Lan lan = (Lan) response.getData();
				clients = lan.getClients();
				Log.i(CommandCenterActivity.TAG, "LAN Client request successful");
				View v = getView();
				TextView textView = (TextView) v.findViewById(R.id.clients_value);
				textView.setText("");
				updateClientList(clients);
			} else {

				Toast.makeText(getActivity(), response.getResponseInfo().getReason(), Toast.LENGTH_LONG).show();
			}
			// mPullToRefreshLayout.setRefreshComplete();
		}
	}

	/**
	 * Updates the interface with the results of the get request listener.
	 * 
	 * @param clients
	 *            list of clients obtained from the get request listener
	 */
	public void updateClientList(ArrayList<Client> clients) {
		Log.i(CommandCenterActivity.TAG, "Updating client list");
		View v = getView();
		mExpandableList = (ExpandableListView) v.findViewById(R.id.expandable_clientlist);
		adapter = new ClientAdapter(getActivity(), clients);
		mExpandableList.setAdapter(adapter);
		mExpandableList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				ExpandableListAdapter ela = parent.getExpandableListAdapter();
				Client client = (Client) ela.getChild(groupPosition, childPosition);
				String clientMac = client.getMac();

				switch (childPosition) {
				case 0: // kick
					// Send data to server to kick
					PutRequest kickRequest = new PutRequest(getActivity(), clientMac, authInfo, "control/wlan/kick_mac", String.class);
					spiceManager.execute(kickRequest, kickRequest.createCacheKey(), DurationInMillis.ALWAYS_EXPIRED, new LanKickPutRequestListener());
					break;
				case 1:
					// ban - not yet implemented.
					break;
				}
				return true;
			}

		});

		if (clients.size() == 0) {
			TextView banner = (TextView) v.findViewById(R.id.clients_value);
			banner.setText(getResources().getString(R.string.no_clients));
			ProgressBar spinny = (ProgressBar) v.findViewById(R.id.clients_loading_progressbar);
			spinny.setVisibility(View.GONE);
			mExpandableList.setVisibility(ExpandableListView.GONE);
			listState = LIST_NOCLIENTS;
		} else {
			// hide the loading layout
			listState = Utility.CONTENT_LOADED;
			LinearLayout loading_layout = (LinearLayout) v.findViewById(R.id.lan_loading_layout);
			loading_layout.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// up navigation
			Log.i(CommandCenterActivity.TAG, "UP in LAN Clients is being handled.");
			FragmentManager fm = getActivity().getSupportFragmentManager();
			fm.popBackStack();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		ClientListRow row = (ClientListRow) (l.getAdapter().getItem(position));
		Client client = row.getClient();
		Log.w(CommandCenterActivity.TAG, "Client IP clicked: " + client.getIp_address());

		// this handles clicking on a list item click... we need to handle kicks
		// in the subchild handler defined above.
	}

	/**
	 * Listener for a kick request.
	 * 
	 */
	private class LanKickPutRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Log.w(CommandCenterActivity.TAG, "Failed to Kick!");
			Toast.makeText(getActivity(), "Failed to kick.", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response clientChange) {
			Log.i(CommandCenterActivity.TAG, "Kick successful.");

		}
	}

}
