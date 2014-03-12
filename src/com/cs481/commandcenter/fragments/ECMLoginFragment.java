package com.cs481.commandcenter.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.LoginActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.responses.ecm.routers.Routers;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class ECMLoginFragment extends Fragment {
	private ProgressDialog progressDialog;
	private SpiceManager spiceManager;

	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_ecmlogin, container, false);

		Button loginButton = (Button) rootView.findViewById(R.id.login_button);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(CommandCenterActivity.TAG, "Clicked on button.");
				readECMRouters();
			}
		});

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		SpiceActivity sa = (SpiceActivity) getActivity();
		Resources resources = getResources();
		sa.setTitle(resources.getString(R.string.ecm_actionbar_title)); // TODO
																		// change
																		// to
																		// string
																		// resource
		spiceManager = sa.getSpiceManager();
		EditText passw = (EditText) getView().findViewById(R.id.ecm_password);
		final Button connect = (Button) getView().findViewById(R.id.login_button);
		passw.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				Log.i(CommandCenterActivity.TAG, "Action ID: " + actionId);
				if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_NULL) {
					connect.performClick();
					return true;
				}
				return false;
			}
		});

	}

	private void readECMRouters() {
		Log.i(CommandCenterActivity.TAG, "Reading ECM Routers as login");
		AuthInfo authInfo = new AuthInfo();
		EditText usern = (EditText) getView().findViewById(R.id.ecm_username);
		EditText passw = (EditText) getView().findViewById(R.id.ecm_password);
		authInfo.setUsername(usern.getText().toString().trim());
		authInfo.setPassword(passw.getText().toString());
		Resources resources = getResources();

		com.cs481.commandcenter.responses.ecm.routers.GetRequest request = new com.cs481.commandcenter.responses.ecm.routers.GetRequest(authInfo);
		String lastRequestCacheKey = request.createCacheKey();

		ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), R.style.DialogTheme);
		progressDialog = new ProgressDialog(wrapper);
		progressDialog.setMessage(resources.getString(R.string.ecm_connecting));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new ECMRoutersGetRequestListener());
	}

	// inner class of your spiced Activity
	private class ECMRoutersGetRequestListener implements RequestListener<Routers> {
		Resources resources = getResources();

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			Toast.makeText(getActivity(), resources.getString(R.string.ecm_failed_get_routers), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Routers routers) {
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			if (routers.getSuccess()) {
				AuthInfo authInfo = new AuthInfo();
				EditText usern = (EditText) getView().findViewById(R.id.ecm_username);
				EditText passw = (EditText) getView().findViewById(R.id.ecm_password);
				authInfo.setUsername(usern.getText().toString());
				authInfo.setPassword(passw.getText().toString());
				authInfo.setEcm(true);

				ECMRoutersFragment routersFragment = ECMRoutersFragment.newInstance(routers.getData(), authInfo);

				// In case this activity was started with special instructions
				// from
				// an
				// Intent, pass the Intent's extras to the fragment as arguments
				// firstFragment.setArguments(getIntent().getExtras());

				// Add the fragment to the 'fragment_container' FrameLayout
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

				// check if the parent activity is dual pane based.
				transaction.replace(R.id.login_fragment, routersFragment);

				transaction.addToBackStack(null);
				transaction.commit();
			} else {
				// not a success
				StringBuilder errorMessage = new StringBuilder();
				
				errorMessage.append(getResources().getString(R.string.server_exception));
				errorMessage.append(": ");
				errorMessage.append(routers.getException());
				
				String message = routers.getMessage();
				if (!message.equals("")) {
					errorMessage.append(" - ");
					errorMessage.append(message);
				}
				
				errorMessage.append(" @ ");
				errorMessage.append(routers.getPath());

				Toast.makeText(getActivity(), errorMessage.toString(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem directitem = menu.findItem(R.id.menu_switchtolocal);
		if (directitem != null) {
			directitem.setVisible(true);
		}
		
		MenuItem ecmitem = menu.findItem(R.id.menu_switchtoecm);
		if (ecmitem != null) {
			ecmitem.setVisible(false);
		}
		
		super.onPrepareOptionsMenu(menu);
	} 



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection
		switch (item.getItemId()) {
		case R.id.menu_switchtolocal:
			DirectLoginFragment localFragment = new DirectLoginFragment();

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments
			// firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

			transaction.replace(R.id.login_fragment, localFragment);
			transaction.commit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
