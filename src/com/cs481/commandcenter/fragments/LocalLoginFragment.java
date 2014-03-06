package com.cs481.commandcenter.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.responses.GetRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.status.product_info.Product_info;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class LocalLoginFragment extends Fragment {

	private SpiceManager spiceManager;
	private ProgressDialog progressDialog;
	private AuthInfo authInfo;

	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_locallogin,
				container, false);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		Resources resources = getResources();
		SpiceActivity sa = (SpiceActivity) getActivity();
		sa.setTitle(resources.getString(R.string.locallogin_actionbar_title)); // TODO
																				// change
																				// to
																				// string
																				// resource
		spiceManager = sa.getSpiceManager();

		EditText passw = (EditText) getView()
				.findViewById(R.id.router_password);
		final Button connect = (Button) getView().findViewById(
				R.id.connect_button);
		passw.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Log.i(CommandCenterActivity.TAG, "Action ID: " + actionId);
				if (actionId == EditorInfo.IME_ACTION_SEND
						|| actionId == EditorInfo.IME_NULL) {
					connect.performClick();
					return true;
				}
				return false;
			}
		});
		setupUI();
	}

	public void setupUI() {
		final EditText ipAddress = (EditText) getView().findViewById(
				R.id.router_ip);
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart)
							+ source.subSequence(start, end)
							+ destTxt.substring(dend);
					if (!resultingTxt
							.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
						return "";
					} else {
						String[] splits = resultingTxt.split("\\.");
						for (int i = 0; i < splits.length; i++) {
							if (Integer.valueOf(splits[i]) > 255) {
								return "";
							}
						}
					}
				}
				return null;
			}
		};
		ipAddress.setFilters(filters);

		CheckBox cb = (CheckBox) getView().findViewById(
				R.id.use_default_gateway);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				Log.i(CommandCenterActivity.TAG, "Default gateway checkbox");
				LinearLayout connectOptions = (LinearLayout) getView()
						.findViewById(R.id.nongateway_layout);
				if (!isChecked) {
					connectOptions.setVisibility(EditText.VISIBLE);
				} else {
					connectOptions.setVisibility(EditText.GONE);
				}
			}
		});

		Button connect = (Button) getView().findViewById(R.id.connect_button);
		connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// collect information.
				/*
				 * CheckBox gateway = (CheckBox) getView().findViewById(
				 * R.id.use_default_gateway); String routerip = ""; boolean
				 * remoteBool = false; // default to local admin int port = 80;
				 * // default http port for local if (gateway.isChecked()) {
				 * routerip = Utility.getDefaultGateway(getActivity()); } else {
				 * EditText iptext = (EditText) getView().findViewById(
				 * R.id.router_ip); routerip = iptext.getText().toString();
				 * CheckBox remote = (CheckBox) getView().findViewById(
				 * R.id.use_remote_admin); remoteBool = remote.isChecked(); port
				 * = 8080; // TODO add box for custom ports, perhaps SSL //
				 * management }
				 */
				testLogin(true);

			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.login_menu, menu);
		MenuItem item = menu.findItem(R.id.menu_switchtolocal);
		item.setVisible(false);
		// getActivity().invalidateOptionsMenu();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.w(CommandCenterActivity.TAG, "Item was clicked.");
		// handle item selection
		switch (item.getItemId()) {
		case R.id.menu_switchtoecm:
			ECMLoginFragment ecmFragment = new ECMLoginFragment();

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments
			// firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();

			transaction.replace(R.id.login_fragment, ecmFragment);
			transaction.commit();
			return true;
		case R.id.menu_preenter_ip:
			EditText iptext = (EditText) getView().findViewById(R.id.router_ip);
			iptext.setText("132.178.226.103");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void testLogin(boolean dialog) {
		// get necessary info
		// collect information.
		CheckBox gateway = (CheckBox) getView().findViewById(
				R.id.use_default_gateway);
		String routerip = "";
		EditText passw = (EditText) getView()
				.findViewById(R.id.router_password);
		String password = passw.getText().toString();
		// boolean remoteBool = false; // default to local admin
		int port = 80; // default http port for local
		if (gateway.isChecked()) {
			routerip = Utility.getDefaultGateway(getActivity());
		} else {
			EditText iptext = (EditText) getView().findViewById(R.id.router_ip);
			routerip = iptext.getText().toString();
			//CheckBox remote = (CheckBox) getView().findViewById(
			//		R.id.use_remote_admin);
			// remoteBool = remote.isChecked();
			port = 8080; // TODO add box for custom ports, perhaps SSL
							// management
		}

		// Build authInfo object
		AuthInfo authInfo = new AuthInfo();
		authInfo.setEcm(false);
		authInfo.setPort(port);
		authInfo.setRouterip(routerip);
		authInfo.setRouterId(null);
		authInfo.setUsername("admin");
		authInfo.setPassword(password);
		this.authInfo = authInfo;

		// Perform network check

		GetRequest request = new GetRequest(authInfo, "status/product_info",
				Product_info.class, "direct_login");
		String lastRequestCacheKey = request.createCacheKey();

		progressDialog = new ProgressDialog(getActivity(),
				R.style.DialogTheme);
		progressDialog.setMessage(getResources().getString(
				R.string.connecting));
		progressDialog.show();
		progressDialog.setCanceledOnTouchOutside(false);
		// progressDialog.setCancelable(false);

		spiceManager.execute(request, lastRequestCacheKey,
				DurationInMillis.ALWAYS_EXPIRED, new LoginGetRequestListener());

	}

	private class LoginGetRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			Log.i(CommandCenterActivity.TAG, "Failed to log in!");
			Toast.makeText(getActivity(),
					getResources().getString(R.string.direct_login_fail),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response response) {
			 Product_info proin = (Product_info) response.getData();

			// update your UI
			if (progressDialog != null)
				progressDialog.dismiss(); // update your UI
			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					// login successful
					// Prepare new intent.
					Intent intent = new Intent(getActivity(),
							CommandCenterActivity.class);
					intent.putExtra("authInfo", authInfo);
					intent.putExtra("ab_subtitle", proin.getProduct_name()); //changes subtitle.
					// start the new activity, and prevent this one from being
					// returned to unless logout is chosen.
					startActivity(intent);
					getActivity().finish();
				} else {
					Toast.makeText(getActivity(),
							response.getResponseInfo().getReason(),
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
