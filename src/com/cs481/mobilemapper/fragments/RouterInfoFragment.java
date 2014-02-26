package com.cs481.mobilemapper.fragments;

import org.apache.commons.lang3.time.DurationFormatUtils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.activities.SpiceActivity;
import com.cs481.mobilemapper.responses.GetRequest;
import com.cs481.mobilemapper.responses.Response;
import com.cs481.mobilemapper.responses.status.product_info.Fw_info;
import com.cs481.mobilemapper.responses.status.product_info.Product_info;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * [Description]
 * @author Sean Wright
 *
 */
public class RouterInfoFragment extends Fragment {
	private AuthInfo authInfo;
	private SpiceManager spiceManager;
	private ProgressDialog progressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			/* save whatever data */
			authInfo = savedInstanceState.getParcelable("authInfo");
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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
		Log.i(CommandCenterActivity.TAG, "Saving instance");
		/* put whatever data */
		outState.putParcelable("authInfo", authInfo);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_routerinfo, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle(getResources().getString(R.string.routerinfo));
		spiceManager = sa.getSpiceManager();
		/* call the reading methods */
		
		readProductInfo(true);
		readFWInfo();
		readUptimeInfo();
	}
	
	private void readProductInfo(boolean dialog) {
		// perform the request.
		GetRequest request = new GetRequest(authInfo, "status/product_info", Product_info.class, "product_infoget");
		String lastRequestCacheKey = request.createCacheKey();

		if (dialog) {
			progressDialog = new ProgressDialog(getActivity(), R.style.RedDialogTheme);
			progressDialog.setMessage(getResources().getString(
					R.string.info_reading));
			progressDialog.show();
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
		}
		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED, new InfoGetRequestListener());
	}
	
	private void readFWInfo() {
		// perform the request.
		GetRequest request = new GetRequest(authInfo, "status/fw_info", Fw_info.class, "fw_infoget");
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED, new InfoGetFWListener());
	}
	
	private void readUptimeInfo() {
		//perform the request.
		GetRequest request = new GetRequest(authInfo, "status/system", com.cs481.mobilemapper.responses.status.product_info.System.class, "system_get");
		String lastRequestCacheKey = request.createCacheKey();

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED, new InfoGetSystemListener());
	}
	
	private class InfoGetRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if(progressDialog!=null) progressDialog.dismiss();
			Log.i(CommandCenterActivity.TAG, "Failed to read Product Info!");

			Toast.makeText(getActivity(),
					getResources().getString(R.string.product_info_fail),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response response) {
			Product_info proin = (Product_info) response.getData();
			
			// update your UI
			if(progressDialog!=null) progressDialog.dismiss(); // update your UI
			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					View v = getView();
					TextView textVal = (TextView) v.findViewById(R.id.product_value);
					textVal.setText(proin.getProduct_name());
					textVal = (TextView) v.findViewById(R.id.mac_address_value);
					textVal.setText(proin.getMac0());
				} else {
					Toast.makeText(getActivity(), response.getResponseInfo().getReason(),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						getActivity(),
						getResources().getString(
								R.string.gpio_get_null_response),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private class InfoGetFWListener implements RequestListener<Response> {
		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if(progressDialog!=null) progressDialog.dismiss();
			Log.i(CommandCenterActivity.TAG, "Failed to read Firmware Info!");

			Toast.makeText(getActivity(),
					getResources().getString(R.string.fw_info_fail),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response response) {
			Fw_info fw = (Fw_info) response.getData();
			
			// update your UI
			if(progressDialog!=null) progressDialog.dismiss(); // update your UI
			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					View v = getView();
					TextView textVal = (TextView) v.findViewById(R.id.firmware_value);
					textVal.setText(fw.getFirmware());
				} else {
					Toast.makeText(getActivity(), response.getResponseInfo().getReason(),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						getActivity(),
						getResources().getString(
								R.string.gpio_get_null_response),
						Toast.LENGTH_LONG).show();
			}
		}
	}	
	
	private class InfoGetSystemListener implements RequestListener<Response> {
		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if(progressDialog!=null) progressDialog.dismiss();
			Log.i(CommandCenterActivity.TAG, "Failed to read Status Info!");

			Toast.makeText(getActivity(),
					getResources().getString(R.string.status_fail),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response response) {
			com.cs481.mobilemapper.responses.status.product_info.System sys = (com.cs481.mobilemapper.responses.status.product_info.System) response.getData();
			
			// update your UI
			if(progressDialog!=null) progressDialog.dismiss(); // update your UI
			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					View v = getView();
					TextView textVal = (TextView) v.findViewById(R.id.uptime_value);
					textVal.setText(DurationFormatUtils.formatDuration((long)sys.getUptime()*1000, "d ' Days, ' H 'Hours, 'm 'Minutes, ' s 'Seconds'"));
				} else {
					Toast.makeText(getActivity(), response.getResponseInfo().getReason(),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						getActivity(),
						getResources().getString(
								R.string.gpio_get_null_response),
						Toast.LENGTH_LONG).show();
			}
		}
	}	
}
