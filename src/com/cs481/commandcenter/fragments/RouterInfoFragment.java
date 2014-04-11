package com.cs481.commandcenter.fragments;

import org.apache.commons.lang3.time.DurationFormatUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.LoginActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.dialog.RebootRouterDialogFragment;
import com.cs481.commandcenter.responses.GetRequest;
import com.cs481.commandcenter.responses.PutRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.status.product_info.Fw_info;
import com.cs481.commandcenter.responses.status.product_info.Product_info;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.PendingRequestListener;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Holds router information as well as the LogSubFragment
 * @author Sean Wright
 */
public class RouterInfoFragment extends Fragment {
	private final static String CACHEKEY_PRODUCT = "productinfo_get";
	private final static String CACHEKEY_HOSTNAME = "hostname_get";
	private final static String CACHEKEY_FIRMWARE = "firmware_get";
	private final static String CACHEKEY_MACADDRESS = "macaddress_get";
	private final static String CACHEKEY_UPTIME = "uptime_get";
	private final static String CACHEKEY_CLIENTS = "numclients_get";
	private final static int ROUTER_REBOOT_FRAGMENT = 0;

	private AuthInfo authInfo;
	private SpiceManager spiceManager;
	private boolean shouldLoadData = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			/* save whatever data */
			authInfo = savedInstanceState.getParcelable("authInfo");
			shouldLoadData = savedInstanceState.getBoolean("shouldLoadData", true);
		} else {
			Bundle passedArgs = getArguments();
			if (passedArgs != null) {
				authInfo = passedArgs.getParcelable("authInfo");
			}
		}
	}

	public static RouterInfoFragment newInstance(AuthInfo authInfo) {
		RouterInfoFragment roInFrag = new RouterInfoFragment();

		Bundle args = new Bundle();
		args.putParcelable("authInfo", authInfo);
		roInFrag.setArguments(args);

		return roInFrag;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			// up navigation
			Log.i(CommandCenterActivity.TAG, "UP in GPIO is being handled.");
			FragmentManager fm = getActivity().getSupportFragmentManager();
			fm.popBackStack();
			return true;
		case R.id.reboot_router:
			Log.i(CommandCenterActivity.TAG, "User selected reboot router. Showing confirmation dialog.");
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
			if (prev != null) {
				ft.remove(prev);
			}

			DialogFragment dialogFrag = RebootRouterDialogFragment.newInstance();
			dialogFrag.setTargetFragment(this, ROUTER_REBOOT_FRAGMENT);
			dialogFrag.show(ft, "dialog");
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(CommandCenterActivity.TAG, "Activity result.");
		switch (requestCode) {
		case ROUTER_REBOOT_FRAGMENT:
			if (resultCode == Activity.RESULT_OK) {
				Log.w(CommandCenterActivity.TAG, "User has elected to reboot the router: Sending request.");
				rebootRouter();
			}
			break;
		}
	}

	private void rebootRouter() {
		// TODO Auto-generated method stub
		PutRequest request = new PutRequest(getActivity(), Boolean.valueOf(true), authInfo, "control/system/reboot", Boolean.class);
		String lastRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new RouterRebootPutListener());
	}

	private class RouterRebootPutListener implements RequestListener<Response>, PendingRequestListener<Response> {
		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if (!isAdded()) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to reboot the router");

			Toast.makeText(getActivity(), getResources().getString(R.string.reboot_fail), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response response) {
			if (!isAdded()) {
				return;
			}

			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					//success
					Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
					Toast.makeText(getActivity(), getResources().getString(R.string.logged_out), Toast.LENGTH_SHORT).show();
					startActivity(logoutIntent);
					getActivity().finish();
					getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					return;
				}
			} else {
				Toast.makeText(getActivity(), getResources().getString(R.string.reboot_fail), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onRequestNotFound() {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.routerinfo_menu, menu);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
		Log.i(CommandCenterActivity.TAG, "Saving instance");
		/* put whatever data */

		View v = getView();
		if (v != null) {
			TextView textVal = (TextView) v.findViewById(R.id.product_value);
			outState.putString("product", textVal.getText().toString());

			textVal = (TextView) v.findViewById(R.id.hostname_value);
			outState.putString("hostname", textVal.getText().toString());

			textVal = (TextView) v.findViewById(R.id.firmware_value);
			outState.putString("firmware", textVal.getText().toString());

			textVal = (TextView) v.findViewById(R.id.mac_address_value);
			outState.putString("mac_address", textVal.getText().toString());

			textVal = (TextView) v.findViewById(R.id.uptime_value);
			outState.putString("uptime", textVal.getText().toString());

			textVal = (TextView) v.findViewById(R.id.numclients_value);
			outState.putString("numclients", textVal.getText().toString());

			outState.putParcelable("authInfo", authInfo);
			outState.putBoolean("shouldLoadData", shouldLoadData);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_routerinfo, container, false);

		// If savedinstancestate is not null, then this fragment will already be
		// created and should be automatically reattached for us.
		if (savedInstanceState == null) {
			Log.i(CommandCenterActivity.TAG, "Saved Instance state was null.");
			FragmentManager fm = getChildFragmentManager(); // doing a
															// subfragment
			FragmentTransaction ft = fm.beginTransaction();
			Fragment logFrag = LogSubfragment.newInstance(authInfo);
			ft.add(R.id.log_container, logFrag);
			ft.commit();
		} else {
			Log.i(CommandCenterActivity.TAG, "Restoring saved values from the previous layout.");

			TextView textVal = (TextView) v.findViewById(R.id.product_value);
			textVal.setText(savedInstanceState.getString("product"));

			textVal = (TextView) v.findViewById(R.id.hostname_value);
			textVal.setText(savedInstanceState.getString("hostname"));

			textVal = (TextView) v.findViewById(R.id.firmware_value);
			textVal.setText(savedInstanceState.getString("firmware"));

			textVal = (TextView) v.findViewById(R.id.mac_address_value);
			textVal.setText(savedInstanceState.getString("mac_address"));

			textVal = (TextView) v.findViewById(R.id.uptime_value);
			textVal.setText(savedInstanceState.getString("uptime"));

			textVal = (TextView) v.findViewById(R.id.numclients_value);
			textVal.setText(savedInstanceState.getString("numclients"));
		}
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		//actionbar stuff
		sa.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		sa.getActionBar().setDisplayHomeAsUpEnabled(true);
		sa.getActionBar().setDisplayShowTitleEnabled(true);
		sa.getActionBar().setTitle(getResources().getString(R.string.routerinfo));
		
		//listeners for network requests
		spiceManager = sa.getSpiceManager();
		spiceManager.addListenerIfPending(Response.class, CACHEKEY_PRODUCT, new InfoGetRequestListener());
		spiceManager.addListenerIfPending(Response.class, CACHEKEY_HOSTNAME, new InfoGetConfigListener());
		spiceManager.addListenerIfPending(Response.class, CACHEKEY_FIRMWARE, new InfoGetFWListener());
		spiceManager.addListenerIfPending(Response.class, CACHEKEY_MACADDRESS, new InfoGetSystemListener());
		spiceManager.addListenerIfPending(Response.class, CACHEKEY_UPTIME, new InfoGetSystemListener());
		spiceManager.addListenerIfPending(Response.class, CACHEKEY_CLIENTS, new InfoGetClientListener());

		/* call the reading methods */

		if (shouldLoadData) {
			readProductInfo(true);
			readFWInfo();
			readUptimeInfo();
			readHostNameInfo();
			readNumClientsInfo();
			shouldLoadData = false;
		}
	}

	/**
	 * Reads product information from the router (model name)
	 * @param dialog boolean to show the dialog (or not)
	 */
	private void readProductInfo(boolean dialog) {
		// perform the request.
		GetRequest request = new GetRequest(getActivity(), authInfo, "status/product_info", Product_info.class, CACHEKEY_PRODUCT);
		String lastRequestCacheKey = request.createCacheKey();
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new InfoGetRequestListener());
	}

	/**
	 * Reads the firmware info from the router.
	 */
	private void readFWInfo() {
		// perform the request.
		GetRequest request = new GetRequest(getActivity(), authInfo, "status/fw_info", Fw_info.class, CACHEKEY_FIRMWARE);
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new InfoGetFWListener());
	}

	/**
	 * Reads the uptime from the router.
	 */
	private void readUptimeInfo() {
		// perform the request.
		GetRequest request = new GetRequest(getActivity(), authInfo, "status/system", com.cs481.commandcenter.responses.status.product_info.System.class, CACHEKEY_UPTIME);
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND, new InfoGetSystemListener());
	}

	/**
	 * Reads the hostname from the router. Might fail if not connected to a network.
	 */
	private void readHostNameInfo() {
		// perform the request.
		GetRequest request = new GetRequest(getActivity(), authInfo, "status/wan/devices/ethernet-wan/config", com.cs481.commandcenter.responses.status.wan.devices.ethernetwan.Config.class, CACHEKEY_HOSTNAME);
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new InfoGetConfigListener());
	}

	/**
	 * Reads the number of clients connected to this router.
	 */
	private void readNumClientsInfo() {
		// perform the request.
		GetRequest request = new GetRequest(getActivity(), authInfo, "status/lan", com.cs481.commandcenter.responses.status.lan.Lan.class, CACHEKEY_CLIENTS);
		// GetRequest request = new GetRequest(getActivity(), authInfo,
		// "status/lan/devices/",
		// com.cs481.mobilemapper.responses.status.lan.Devices.class,
		// "client_get");
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new InfoGetClientListener());

	}

	private class InfoGetRequestListener implements RequestListener<Response>, PendingRequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			if (getActivity() == null || getView() == null) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to read Product Info!");

			Toast.makeText(getActivity(), getResources().getString(R.string.product_info_fail), Toast.LENGTH_SHORT).show();
			
			TextView infoValue = (TextView) getView().findViewById(R.id.product_value);
			infoValue.setText(R.string.info_load_fail);
			
			TextView macValue = (TextView) getView().findViewById(R.id.mac_address_value);
			macValue.setText(R.string.info_load_fail);
		}

		@Override
		public void onRequestSuccess(Response response) {
			if (getActivity() == null || getView() == null) {
				return;
			}
			Product_info proin = (Product_info) response.getData();
			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					View v = getView();
					TextView textVal = (TextView) v.findViewById(R.id.product_value);
					if (textVal != null)
						textVal.setText(proin.getProduct_name());
					textVal = (TextView) v.findViewById(R.id.mac_address_value);
					if (textVal != null)
						textVal.setText(proin.getMac0());
				} else {
					Toast.makeText(getActivity(), response.getResponseInfo().getReason(), Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getActivity(), getResources().getString(R.string.info_get_response_null), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onRequestNotFound() {
			// TODO Auto-generated method stub
			// we don't really care i guess.
		}
	}

	private class InfoGetFWListener implements RequestListener<Response>, PendingRequestListener<Response> {
		@Override
		public void onRequestFailure(SpiceException e) {
			if (getActivity() == null || getView() == null) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to read Firmware Info!");

			Toast.makeText(getActivity(), getResources().getString(R.string.fw_info_fail), Toast.LENGTH_SHORT).show();
			
			TextView infoValue = (TextView) getView().findViewById(R.id.firmware_value);
			infoValue.setText(R.string.info_load_fail);
		}

		@Override
		public void onRequestSuccess(Response response) {
			if (getActivity() == null || getView() == null) {
				return; // prevent crash if fragment has been discarded and is
						// awaiting GC
			}
			Fw_info fw = (Fw_info) response.getData();
			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					View v = getView();
					TextView textVal = (TextView) v.findViewById(R.id.firmware_value);
					if (textVal != null) {
						textVal.setText(fw.getFirmware());
					}
				} else {
					Toast.makeText(getActivity(), response.getResponseInfo().getReason(), Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getActivity(), getResources().getString(R.string.info_get_response_null), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onRequestNotFound() {
			// TODO Auto-generated method stub

		}
	}

	private class InfoGetSystemListener implements RequestListener<Response>, PendingRequestListener<Response> {
		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if (getActivity() == null || getView() == null) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to read Status Info!");

			Toast.makeText(getActivity(), getResources().getString(R.string.status_fail), Toast.LENGTH_SHORT).show();
			
			TextView infoValue = (TextView) getView().findViewById(R.id.uptime_value);
			infoValue.setText(R.string.info_load_fail);
		}

		@Override
		public void onRequestSuccess(Response response) {
			if (getActivity() == null || getView() == null) {
				return;
			}
			com.cs481.commandcenter.responses.status.product_info.System sys = (com.cs481.commandcenter.responses.status.product_info.System) response.getData();

			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					View v = getView();
					TextView textVal = (TextView) v.findViewById(R.id.uptime_value);
					if (textVal != null) {
						textVal.setText((DurationFormatUtils.formatDuration((long) sys.getUptime() * 1000, "d'd,' H'h, 'm'm, 's's'")));
					}
				} else {
					Toast.makeText(getActivity(), response.getResponseInfo().getReason(), Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getActivity(), getResources().getString(R.string.info_get_response_null), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onRequestNotFound() {
			// TODO Auto-generated method stub

		}
	}

	private class InfoGetConfigListener implements RequestListener<Response>, PendingRequestListener<Response> {
		@Override
		public void onRequestFailure(SpiceException e) {
			if (getActivity() == null || getView() == null) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to read Config Info!");

			if (getActivity() != null) { // prevent crash in the event the user
											// navigates away.
				Toast.makeText(getActivity(), getResources().getString(R.string.config_fail), Toast.LENGTH_SHORT).show();
			}
			
			TextView infoValue = (TextView) getView().findViewById(R.id.hostname_value);
			infoValue.setText(R.string.info_load_fail);
		}

		@Override
		public void onRequestSuccess(Response response) {
			if (getActivity() == null || getView() == null) {
				return;
			}
			com.cs481.commandcenter.responses.status.wan.devices.ethernetwan.Config con = (com.cs481.commandcenter.responses.status.wan.devices.ethernetwan.Config) response.getData();

			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					View v = getView();
					TextView textVal = (TextView) v.findViewById(R.id.hostname_value);
					if (textVal != null) {
						textVal.setText(con.getHostname());
					}
				} else {
					Toast.makeText(getActivity(), response.getResponseInfo().getReason(), Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getActivity(), getResources().getString(R.string.info_get_response_null), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onRequestNotFound() {
			// TODO Auto-generated method stub

		}
	}

	private class InfoGetClientListener implements RequestListener<Response>, PendingRequestListener<Response> {
		@Override
		public void onRequestFailure(SpiceException e) {
			if (getActivity() == null || getView() == null) {
				return;
			}
			Log.i(CommandCenterActivity.TAG, "Failed to read Client Info!");

			Toast.makeText(getActivity(), getResources().getString(R.string.config_fail), Toast.LENGTH_SHORT).show();
			
			TextView infoValue = (TextView) getView().findViewById(R.id.numclients_value);
			infoValue.setText(R.string.info_load_fail);
		}

		@Override
		public void onRequestSuccess(Response response) {
			if (getActivity() == null || getView() == null) {
				return;
			}
			com.cs481.commandcenter.responses.status.lan.Lan dat = (com.cs481.commandcenter.responses.status.lan.Lan) response.getData();
			// Devices dat = (Devices) response.getData();
			// Log.i(CommandCenterActivity.TAG,
			// "Successfully parsed a Devices object");
			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					View v = getView();
					TextView textVal = (TextView) v.findViewById(R.id.numclients_value);
					if (textVal != null) {
						textVal.setText(Integer.toString(dat.getClients().size()));
					}
				} else {
					Toast.makeText(getActivity(), response.getResponseInfo().getReason(), Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getActivity(), getResources().getString(R.string.info_get_response_null), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onRequestNotFound() {
			// TODO Auto-generated method stub

		}
	}
}
