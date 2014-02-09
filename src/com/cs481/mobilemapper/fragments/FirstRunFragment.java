package com.cs481.mobilemapper.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cs481.mobilemapper.CommandCenter;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.debug.DebugActivity;

public class FirstRunFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_firstrun, container,
				false);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		EditText passw = (EditText) getView()
				.findViewById(R.id.router_password);
		final Button connect = (Button) getView().findViewById(
				R.id.connect_button);
		passw.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Log.i(CommandCenter.TAG, "Action ID: " + actionId);
				if (actionId == EditorInfo.IME_ACTION_SEND) {
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

		Button connect = (Button) getView().findViewById(R.id.connect_button);
		connect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//collect information.
				CheckBox gateway = (CheckBox) getView().findViewById(
						R.id.use_default_gateway);
				String routerip = "";
				if (gateway.isChecked()) {
					routerip = Utility.getDefaultGateway(getActivity());
				} else {
					EditText iptext = (EditText) getView().findViewById(
							R.id.router_ip);
					routerip = iptext.getText().toString();
				}
				
				//Prepare new intent.
				Intent intent = new Intent(getActivity(), CommandCenter.class);
				intent.putExtra("ip", routerip);
				String password = ((EditText) getView().findViewById(
						R.id.router_password)).getText().toString();
				intent.putExtra("pass", password);
				
				//Set ecm flags to false.
				intent.putExtra("ecm", false);
				intent.putExtra("id", "NOT-ECM-MANAGED");
				intent.putExtra("user", "admin");
				
				//start the new activity, and prevent this one from being returned to unless logout is chosen.
				startActivity(intent);
				getActivity().finish();
			}

		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.firstrun_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.w(CommandCenter.TAG, "Item was clicked.");
		// handle item selection
		switch (item.getItemId()) {
		case R.id.fr_debug:
			Intent intent = new Intent(getActivity(), DebugActivity.class);
			CheckBox gateway = (CheckBox) getView().findViewById(
					R.id.use_default_gateway);
			String routerip = "";
			if (gateway.isChecked()) {
				routerip = Utility.getDefaultGateway(getActivity());
			} else {
				EditText iptext = (EditText) getView().findViewById(
						R.id.router_ip);
				routerip = iptext.getText().toString();
			}

			intent.putExtra("ip", routerip);
			String password = ((EditText) getView().findViewById(
					R.id.router_password)).getText().toString();
			intent.putExtra("pass", password);
			intent.putExtra("ecm", false);
			intent.putExtra("id", "NOT-ECM-MANAGED");
			intent.putExtra("user", "admin");

			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
