package com.cs481.commandcenter.fragments;

import java.util.ArrayList;

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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.Profile;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.LoginActivity;
import com.cs481.commandcenter.activities.SpiceActivity;
import com.cs481.commandcenter.dialog.DirectSaveProfileDialogFragment;
import com.cs481.commandcenter.responses.GetRequest;
import com.cs481.commandcenter.responses.Response;
import com.cs481.commandcenter.responses.status.product_info.Product_info;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class DirectLoginFragment extends Fragment {

	private SpiceManager spiceManager;
	private ProgressDialog progressDialog;
	private AuthInfo authInfo;
	private AuthInfo encryptedInfo;
	private boolean showingAdvanced = false;
	private boolean showingNongateway = false;
	private boolean loginSavingProfile = false; // set when save profile is set

	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		if (savedInstancedState != null) {
			showingAdvanced = savedInstancedState.getBoolean("showingAdvanced", false);
			showingNongateway = savedInstancedState.getBoolean("showingNongateway", false);
			authInfo = savedInstancedState.getParcelable("authInfo");
			encryptedInfo = savedInstancedState.getParcelable("encryptedInfo");
		}
		setHasOptionsMenu(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("showingAdvanced", showingAdvanced);
		outState.putBoolean("showingNongateway", showingNongateway);
		outState.putParcelable("authInfo", authInfo);
		outState.putParcelable("encryptedInfo", encryptedInfo);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_directlogin, container, false);

		if (showingAdvanced) {
			rootView.findViewById(R.id.use_ssl).setVisibility(View.VISIBLE);
			rootView.findViewById(R.id.router_username).setVisibility(View.VISIBLE);
			Button advanced = (Button) rootView.findViewById(R.id.show_direct_advanced_button);
			advanced.setText(getResources().getString(R.string.hide_direct_advanced));
		}

		if (showingNongateway) {
			rootView.findViewById(R.id.nongateway_layout).setVisibility(View.VISIBLE);
		}

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

		EditText passw = (EditText) getView().findViewById(R.id.router_password);
		final Button connect = (Button) getView().findViewById(R.id.connect_button);
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
		setupUI();
	}

	public void setupUI() {
		final EditText ipAddress = (EditText) getView().findViewById(R.id.router_ip);
		final CheckBox defaultGateway = (CheckBox) getView().findViewById(R.id.use_default_gateway);
		final Button toggleAdvanced = (Button) getView().findViewById(R.id.show_direct_advanced_button);

		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
					if (!resultingTxt.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
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

		defaultGateway.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				showingNongateway = !showingNongateway;
				Log.i(CommandCenterActivity.TAG, "Default gateway checkbox clicked");
				LinearLayout connectOptions = (LinearLayout) getView().findViewById(R.id.nongateway_layout);
				if (!isChecked) {
					connectOptions.setVisibility(EditText.VISIBLE);
				} else {
					connectOptions.setVisibility(EditText.GONE);
				}
			}
		});

		// toggle advanced options
		toggleAdvanced.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showingAdvanced = !showingAdvanced; // toggle
				int visibility = (showingAdvanced) ? View.VISIBLE : View.GONE;

				CheckBox ssl = (CheckBox) getView().findViewById(R.id.use_ssl);
				ssl.setVisibility(visibility);

				EditText username = (EditText) getView().findViewById(R.id.router_username);
				username.setVisibility(visibility);

				toggleAdvanced.setText(getResources().getString((showingAdvanced) ? R.string.hide_direct_advanced : R.string.show_direct_advanced));
				Log.i(CommandCenterActivity.TAG, "Toggling advanced, new visibility is " + visibility);

			}
		});

		// connect button
		Button connect = (Button) getView().findViewById(R.id.connect_button);
		connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get current profiles
				LoginActivity la = (LoginActivity) getActivity();
				ArrayList<Profile> profiles = la.getProfiles();

				// construct a temporary profile
				
				Profile directprofile = buildProfile(buildAuthInfo());
				
				if (profiles.contains(directprofile)){
					//already exists. just login
					testLogin(false, null);
				} else {
					//show the dialog for saving the profile
					DirectSaveProfileDialogFragment dpsFragment = DirectSaveProfileDialogFragment.newInstance(buildAuthInfo());
					dpsFragment.show(getFragmentManager(), "DirectProfileSave");
				}
			}
		});
	}

	protected Profile buildProfile(AuthInfo authInfo) {
		// construct a temporary profile
		Profile directProfile = new Profile();
		directProfile.setAuthInfo(authInfo);
		directProfile.setProfileName(authInfo.getRouterip());
		return directProfile;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem directitem = menu.findItem(R.id.menu_switchtolocal);
		if (directitem != null) {
			directitem.setVisible(false);
		}

		MenuItem ecmitem = menu.findItem(R.id.menu_switchtoecm);
		if (ecmitem != null) {
			ecmitem.setVisible(true);
		}

		super.onPrepareOptionsMenu(menu);
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
			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

			transaction.replace(R.id.login_fragment, ecmFragment, "DirectFragment");
			transaction.commit();
			return true;
		case R.id.menu_preenter_ip:
			EditText iptext = (EditText) getView().findViewById(R.id.router_ip);
			iptext.setText("132.178.226.103");

			EditText portNum = (EditText) getView().findViewById(R.id.router_port);
			portNum.setText("8080");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Begins a login process to the router.
	 * @param saveprofile boolean flag to save this as a profile if the command succeeds.
	 * @param encryptedInfo encrypted info save into the database if it succeeds. should be generated by the dialog for saving.
	 */
	public void testLogin(boolean saveprofile, AuthInfo encryptedInfo) {
		AuthInfo authInfo = buildAuthInfo();
		this.authInfo = authInfo;
		this.encryptedInfo = encryptedInfo;
		this.loginSavingProfile = saveprofile;
		
		Log.i(CommandCenterActivity.TAG, "Encrypted Auth Info via dialog: "+encryptedInfo);
		// Perform network check
		GetRequest request = new GetRequest(getActivity(), authInfo, "status/product_info", Product_info.class, "direct_login");
		String lastRequestCacheKey = request.createCacheKey();

		progressDialog = new ProgressDialog(getActivity(), R.style.DialogTheme);
		progressDialog.setMessage(getResources().getString(R.string.connecting));
		progressDialog.show();
		progressDialog.setCanceledOnTouchOutside(false);
		spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ALWAYS_EXPIRED, new LoginGetRequestListener());
	}

	private AuthInfo buildAuthInfo() {
		// get necessary info. there's going to be a lot
		// collect information.
		CheckBox gateway = (CheckBox) getView().findViewById(R.id.use_default_gateway);
		String routerip = "";
		EditText passw = (EditText) getView().findViewById(R.id.router_password);
		String password = passw.getText().toString();

		int port = 80; // default port
		String username = "admin"; // default username
		boolean isSSL = false; // default SSL connection

		if (gateway.isChecked()) {
			routerip = Utility.getDefaultGateway(getActivity());
		} else {
			EditText iptext = (EditText) getView().findViewById(R.id.router_ip);
			routerip = iptext.getText().toString();

			// port
			EditText portField = (EditText) getView().findViewById(R.id.router_port);
			String portString = portField.getText().toString();
			port = Integer.parseInt(portString);
		}

		// check for advanced settings.

		if (showingAdvanced) {

			// ssl
			CheckBox ssl = (CheckBox) getView().findViewById(R.id.use_ssl);
			isSSL = ssl.isChecked();

			// username
			EditText usernameField = (EditText) getView().findViewById(R.id.router_username);
			username = usernameField.getText().toString();
		}

		// Build authInfo object
		AuthInfo authInfo = new AuthInfo();
		authInfo.setEcm(false);
		authInfo.setPort(port);
		authInfo.setRouterip(routerip);
		authInfo.setRouterId(null);
		authInfo.setUsername(username);
		authInfo.setPassword(password);
		authInfo.setHttps(isSSL);

		return authInfo;
	}

	private class LoginGetRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			DirectLoginFragment.this.loginSavingProfile = false;
			Log.i(CommandCenterActivity.TAG, "Failed to log in!");
			Toast.makeText(getActivity(), getResources().getString(R.string.direct_login_fail), Toast.LENGTH_SHORT).show();
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

					// We should get ready to save this as profile if thats set
					if (loginSavingProfile) {
						Utility.saveProfile(getActivity(), buildProfile(encryptedInfo));
					}

					// Prepare new intent.
					Intent intent = new Intent(getActivity(), CommandCenterActivity.class);
					intent.putExtra("authInfo", authInfo);
					intent.putExtra("ab_subtitle", proin.getProduct_name()); // changes
																				// subtitle.
					// start the new activity, and prevent this one from being
					// returned to unless logout is chosen.
					startActivity(intent);
					getActivity().finish();
				} else {
					Toast.makeText(getActivity(), response.getResponseInfo().getReason(), Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getActivity(), getResources().getString(R.string.gpio_get_null_response), Toast.LENGTH_LONG).show();
			}
		}
	}

}
