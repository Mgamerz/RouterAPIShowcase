package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.activities.SpiceActivity;
import com.cs481.mobilemapper.responses.GetRequest;
import com.cs481.mobilemapper.responses.Response;
import com.cs481.mobilemapper.responses.status.log.LogMessage;
import com.cs481.mobilemapper.responses.status.log.Logs;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.PendingRequestListener;
import com.octo.android.robospice.request.listener.RequestListener;

public class LogSubfragment extends ListFragment implements OnRefreshListener {

	private static final String CACHEKEY_LOGS = "logs_get";
	private PullToRefreshLayout mPullToRefreshLayout;
	private SpiceManager spiceManager;
	private AuthInfo authInfo;
	private LogAdapter adapter;
	private ArrayList<LogMessage> logs;
	private boolean shouldLoadData = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (savedInstanceState != null) {
			logs = savedInstanceState.getParcelableArrayList("logs");
			shouldLoadData = savedInstanceState.getBoolean("shouldLoadData");
			authInfo = savedInstanceState.getParcelable("authInfo");
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
			}
		}
		Log.i(CommandCenterActivity.TAG, "Creating a new log fragment.");
	}

	public static LogSubfragment newInstance(AuthInfo authInfo) {
		LogSubfragment wawFrag = new LogSubfragment();

		Bundle args = new Bundle();
		args.putParcelable("authInfo", authInfo);
		wawFrag.setArguments(args);

		return wawFrag;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
		Log.i(CommandCenterActivity.TAG, "Saving logs instance");
		outState.putBoolean("shouldLoadData", shouldLoadData);
		if(logs==null) { logs = new ArrayList<LogMessage>(); }
		outState.putParcelableArrayList("logs", logs); // This is how to save
														// the logs object. The
														// LogMessage object
														// must be parcelable
		outState.putParcelable("authInfo", authInfo);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.subfrag_log, container, false);
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
	public void onStart(){
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		spiceManager = sa.getSpiceManager();
		spiceManager.addListenerIfPending(Response.class, CACHEKEY_LOGS, new LogsGetRequestListener());
		if (shouldLoadData) {
			Log.i(CommandCenterActivity.TAG, "Reading logs, should load data.");
			readLogs();
			shouldLoadData = false;
		} else {
			updateLogsList(logs);
		}
	}

	/**
	 * Read's the logs off the server.
	 */
	private void readLogs() {
		// TODO Auto-generated method stub
		GetRequest clientModeReq = new GetRequest(authInfo, "status/log",
				Logs.class, CACHEKEY_LOGS);
		String lastRequestCacheKey = clientModeReq.createCacheKey();
		spiceManager.execute(clientModeReq, lastRequestCacheKey,
				SpiceActivity.DURATION_3SECS, new LogsGetRequestListener());
	}

	public class LogAdapter extends ArrayAdapter<LogMessage> {
		private final Context context;
		private final ArrayList<LogMessage> rows;

		public LogAdapter(Context context, ArrayList<LogMessage> rows) {
			super(context, R.layout.listrow_log, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public LogMessage getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater
					.inflate(R.layout.listrow_log, parent, false);

			// Setup log stuff here. rowView is the container for each
			// individual log in the list, so you can call
			// v.findViewById(<id here>) to find sub elements to set.

			LogMessage log = rows.get(position);

			TextView messageView = (TextView) rowView
					.findViewById(R.id.log_message);
			TextView tagView = (TextView) rowView.findViewById(R.id.log_tag);
			TextView severityView = (TextView) rowView
					.findViewById(R.id.log_severity);
			TextView timeView = (TextView) rowView.findViewById(R.id.log_time);

			messageView.setText(log.getMessage());
			tagView.setText(log.getTag());
			severityView.setText(log.getSeverity());
			timeView.setText(log.getDateString());			
			return rowView;
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		readLogs();
	}

	private class LogsGetRequestListener implements RequestListener<Response>, PendingRequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			Resources resources = getResources();
			Log.i(CommandCenterActivity.TAG, "Failed to read logs!");
			Toast.makeText(getActivity(),
					resources.getString(R.string.log_get_failed),
					Toast.LENGTH_SHORT).show();
			mPullToRefreshLayout.setRefreshComplete();
			
			ProgressBar bar = (ProgressBar) getView().findViewById(R.id.log_loadingprogressbar);
			bar.setVisibility(ProgressBar.GONE);
			
			TextView message = (TextView) getView().findViewById(R.id.log_loadingtext);
			message.setText(getActivity().getResources().getString(R.string.log_get_failed));
		}

		@Override
		public void onRequestSuccess(Response response) {
			// update your UI
			if (response.getResponseInfo().getSuccess()) {
				Logs logs = (Logs) response.getData();
				Log.i(CommandCenterActivity.TAG, "Logs get request successful");
				updateLogsList(logs.getLogs());
			} else {
				Toast.makeText(getActivity(),
						response.getResponseInfo().getReason(),
						Toast.LENGTH_LONG).show();
			}
			mPullToRefreshLayout.setRefreshComplete();
		}

		@Override
		public void onRequestNotFound() {
			// TODO Auto-generated method stub
			Log.w(CommandCenterActivity.TAG, "No request pending to listen to for logs.");
		}
	}

	private void updateLogsList(ArrayList<LogMessage> logs) {
		// TODO Auto-generated method stub
		Log.i(CommandCenterActivity.TAG, "Updating adapter with new log information.");
		Log.i(CommandCenterActivity.TAG, "Number of logs: "+logs.size());
		this.logs = logs;
		if (adapter == null) {
			Log.i(CommandCenterActivity.TAG, "created new adapter.");
			adapter = new LogAdapter(getActivity(), this.logs);
			setListAdapter(adapter);
		}
		Log.i(CommandCenterActivity.TAG, "notifying adapter of new dataset");
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		// when a context menu is being created
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.log_contextmenu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo(); // info is the item in the adapter that was
								// selected (long pressed.)
		LogMessage messageSelected = logs.get(info.position);
		switch (item.getItemId()) {
		case R.id.contextmenu_copy:
			// Gets a handle to the clipboard service.
			ClipboardManager clipboard = (ClipboardManager) getActivity()
					.getSystemService(Context.CLIPBOARD_SERVICE);
			// Creates a new text clip to put on the clipboard
			ClipData clip = ClipData.newPlainText("logmessage",
					messageSelected.toString());
			clipboard.setPrimaryClip(clip);
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.log_copied), Toast.LENGTH_LONG).show();
			return true;
		default:
			return false;
		}
	}
}
