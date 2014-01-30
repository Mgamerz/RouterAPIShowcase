package com.cs481.mobilemapper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MappingFragment extends Fragment implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	/**
	 * The fragment representing the Mapping tab
	 */
	// public static final String ARG_SECTION_NUMBER = "section_number";
	private GoogleMap map;
	private LocationClient locationClient;

	public MappingFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mapping, container,
				false);
		configureMap();
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isGooglePlayServicesAvailable()) {
			locationClient = new LocationClient(getActivity(), this, this);
			locationClient.connect();
		}
	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	public void onStop() {
		// Disconnecting the client invalidates it.
		locationClient.disconnect();
		super.onStop();
	}

	private void configureMap() {
		map = ((SupportMapFragment) getFragmentManager().findFragmentById(
				R.id.trackmap)).getMap();
		map.setMyLocationEnabled(true);
		// CameraUpdate cam = CameraUpdateFactory.;
		Log.i(CommandCenter.TAG, "Finished setting up map");
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
		Location location = locationClient.getLastLocation();
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,
				17);
		map.animateCamera(cameraUpdate);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Disconnected", Toast.LENGTH_SHORT)
				.show();

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// Report to the UI that the location was updated
		String msg = "Updated Location: "
				+ Double.toString(location.getLatitude()) + ","
				+ Double.toString(location.getLongitude());
		Toast.makeText(getActivity(), "Updating map with new location.",
				Toast.LENGTH_SHORT).show();
		if (map != null) {
			// map.
		}
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				locationClient.connect();
				break;
			}

		}
	}

	
	public void onDestroy(){
	//somewhere in your onDestroy(){
	CameraPosition mMyCam = map.getCameraPosition();
	double longitude = mMyCam.target.longitude;
	//boolean is
	//SharedPreferences settings = getActivity().getSharedPreferences("SOME_NAME", 0);
	//SharedPreferences.Editor editor = settings.edit();
	//editor.putFloat("longitude", longitude);
	//editor.commit();
	}

	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			return true;
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode, getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getActivity().getSupportFragmentManager(),
						"Location Updates");
			}

			return false;
		}
	}
}
