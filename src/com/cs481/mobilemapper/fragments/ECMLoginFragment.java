package com.cs481.mobilemapper.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.CommandCenter;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;
import com.cs481.mobilemapper.debug.DebugActivity;
import com.cs481.mobilemapper.responses.ecm.routers.Routers;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ECMLoginFragment extends Fragment {
	private ProgressDialog progressDialog;
	private SpiceManager spiceManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_ecmlogin, container,
				false);

		Button loginButton = (Button) rootView.findViewById(R.id.login_button);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				readECMRouters();
			}

		});

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle("ECM Login"); // TODO change to string resource
		spiceManager = sa.getSpiceManager();
	}

	private void readECMRouters() {
		AuthInfo authInfo = new AuthInfo();
		EditText usern = (EditText) getView().findViewById(R.id.ecm_username);
		EditText passw = (EditText) getView().findViewById(R.id.ecm_password);
		authInfo.setUsername(usern.getText().toString());
		authInfo.setPassword(passw.getText().toString());

		com.cs481.mobilemapper.responses.ecm.routers.GetRequest request = new com.cs481.mobilemapper.responses.ecm.routers.GetRequest(
				authInfo);
		String lastRequestCacheKey = request.createCacheKey();

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Connecting to ECM...");
		progressDialog.show();
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED,
				new ECMRoutersGetRequestListener());
	}

	// inner class of your spiced Activity
	private class ECMRoutersGetRequestListener implements
			RequestListener<Routers> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			progressDialog.dismiss();
			Toast.makeText(getActivity(),
					"Failed to get list of routers from ECM",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Routers routers) {
			// update your UI
			progressDialog.dismiss();
			((DebugActivity) getActivity()).setRouters(routers);
			ECMRoutersFragment routersFragment = new ECMRoutersFragment();

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments
			// firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getActivity().getSupportFragmentManager()
					.beginTransaction();

			// check if the parent activity is dual pane based.
			transaction.replace(R.id.debug_container, routersFragment);

			transaction.addToBackStack(null);
			transaction.commit();
		}
	}
}
