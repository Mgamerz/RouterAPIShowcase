package com.cs481.mobilemapper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.cs481.mobilemapper.R;

public class PINHeaderSubfragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		// setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.subfrag_pinpad, container, false);
/*		FrameLayout pinpad = (FrameLayout) rootView.findViewById(R.id.pinpad_fragment);
		
		//Add the subfragment if we need to
		String pin_id = "pinpad"; //fragment tag
		FragmentManager cfm = getChildFragmentManager(); //only works with this fragment
		PINPadSubfragment pinpad_fragment = (PINPadSubfragment) cfm.findFragmentByTag(pin_id);
		
		if (pinpad_fragment==null){
			
		}
		*/
		return rootView;
	}

	/*
	@Override
	public void onStart() {
		super.onStart();
		// SpiceActivity sa = (SpiceActivity) getActivity();
		// sa.setTitle("UNLOCK"); // TODO change to string resource
		setupUI();
	}

	public void setupUI() {
		// Make the numeric buttons have listeners
		Button pinButton = (Button) getView().findViewById(R.id.pin0);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin1);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin2);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin3);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin4);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin5);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin6);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin7);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin8);
		pinButton.setOnClickListener(this);
		pinButton = (Button) getView().findViewById(R.id.pin9);
		pinButton.setOnClickListener(this);

	}

	/*
	 * @Override public void onCreateOptionsMenu(Menu menu, MenuInflater
	 * inflater) { inflater.inflate(R.menu.login_menu, menu); MenuItem item =
	 * menu.findItem(R.id.menu_switchtolocal); item.setVisible(false); //
	 * getActivity().invalidateOptionsMenu(); }
	 * 
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * Log.w(CommandCenterActivity.TAG, "Item was clicked."); // handle item
	 * selection switch (item.getItemId()) { case R.id.fr_debug: Intent intent =
	 * new Intent(getActivity(), DebugActivity.class); CheckBox gateway =
	 * (CheckBox) getView().findViewById( R.id.use_default_gateway); String
	 * routerip = ""; if (gateway.isChecked()) { routerip =
	 * Utility.getDefaultGateway(getActivity()); } else { EditText iptext =
	 * (EditText) getView().findViewById( R.id.router_ip); routerip =
	 * iptext.getText().toString(); }
	 * 
	 * intent.putExtra("ip", routerip); String password = ((EditText)
	 * getView().findViewById( R.id.router_password)).getText().toString();
	 * intent.putExtra("pass", password); intent.putExtra("ecm", false);
	 * intent.putExtra("id", "NOT-ECM-MANAGED"); intent.putExtra("user",
	 * "admin");
	 * 
	 * startActivity(intent); return true; case R.id.menu_switchtoecm:
	 * ECMLoginFragment ecmFragment = new ECMLoginFragment();
	 * 
	 * // In case this activity was started with special instructions from // an
	 * // Intent, pass the Intent's extras to the fragment as arguments //
	 * firstFragment.setArguments(getIntent().getExtras());
	 * 
	 * // Add the fragment to the 'fragment_container' FrameLayout
	 * FragmentTransaction transaction = getActivity()
	 * .getSupportFragmentManager().beginTransaction();
	 * 
	 * transaction.replace(R.id.login_fragment, ecmFragment);
	 * transaction.commit(); return true; case R.id.menu_preenter_ip: EditText
	 * iptext = (EditText) getView().findViewById( R.id.router_ip);
	 * iptext.setText("132.178.226.103"); return true; default: return
	 * super.onOptionsItemSelected(item); } }
	 */

}
