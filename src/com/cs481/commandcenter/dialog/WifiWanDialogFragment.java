package com.cs481.commandcenter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.fragments.WifiAsWANFragment;
import com.cs481.commandcenter.responses.config.wwan.WANProfile;
import com.cs481.commandcenter.responses.status.wlan.WAP;

/**
 * Dialog fragment that shows options for when the user tries to connect to a
 * wireless access point as the router
 * 
 * @author Mike Perez
 */

public class WifiWanDialogFragment extends DialogFragment {
	private WAP wap;
	private AuthInfo authInfo;

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public WifiWanDialogFragment() {
		Log.i(CommandCenterActivity.TAG, "Created wifi as wan fragment.");
		// context = getActivity();
	}

	/**
	 * This creates a new instance of WifiWanDialogFragment with the given
	 * parameters set.
	 */
	public static WifiWanDialogFragment newInstance(WAP wap, AuthInfo authInfo) {
		WifiWanDialogFragment wwdf = new WifiWanDialogFragment();
		wwdf.wap = wap;
		wwdf.authInfo = authInfo;
		return wwdf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			wap = savedInstanceState.getParcelable("wap");
			authInfo = savedInstanceState.getParcelable("authInfo");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
		outState.putParcelable("wap", wap);
		outState.putParcelable("authInfo", authInfo);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		HoloDialogBuilder dialogBuilder = new HoloDialogBuilder(getActivity());

		Theme theme = getActivity().getTheme();
		TypedValue typedValue = new TypedValue();
		theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true);
		// Color color = getResources().getColor(colorid);

		dialogBuilder.setDividerColor(typedValue.resourceId);
		dialogBuilder.setTitleColor(typedValue.resourceId);
		// dialogBuilder.setTitleColor(getResources().getString(R.color.Black));
		String title = wap.getSsid();
		boolean hiddenSsid = false; // used to show the SSID field
		if (title.equals("")) {
			title = getResources().getString(R.string.hidden_ssid);
			hiddenSsid = true;
		}

		dialogBuilder.setTitle(title);
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		final View dialogView = inflater.inflate(R.layout.dialog_wifiwan, null);

		if (!hiddenSsid) {
			Log.i(CommandCenterActivity.TAG, "Not a hidden SSID");
			LinearLayout layout = (LinearLayout) dialogView.findViewById(R.id.wapconnect_ssidlayout);
			layout.setVisibility(LinearLayout.GONE);
			Log.i(CommandCenterActivity.TAG, "Layout visibility: " + layout.getVisibility());
		}

		if (wap.getAuthmode().equals("none")) {
			RelativeLayout passwordLayout = (RelativeLayout) dialogView.findViewById(R.id.wapconnect_passwordlayout);
			passwordLayout.setVisibility(RelativeLayout.GONE);
		} else {
			TextView warning = (TextView) dialogView.findViewById(R.id.wapconnect_insecure_text);
			warning.setVisibility(TextView.GONE);

			CheckBox showPass = (CheckBox) dialogView.findViewById(R.id.wapconnect_showpassword);
			showPass.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					EditText password = (EditText) dialogView.findViewById(R.id.wapconnect_password_field);
					if (isChecked) {
						password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
					} else {
						password.setInputType(129); // mask password. don't know
													// why its a number
					}

				}
			});
		}

		// Get dynamic content, set font to BOLD
		SpannableString securityString = new SpannableString(wap.getAuthmode());
		securityString.setSpan(new StyleSpan(Typeface.BOLD), 0, securityString.length(), 0);

		SpannableString signalString = new SpannableString(Utility.rssiToSignalLiteral(wap.getRssi(), getResources()));
		signalString.setSpan(new StyleSpan(Typeface.BOLD), 0, signalString.length(), 0);

		// Set the dynamic content
		TextView securityType = (TextView) dialogView.findViewById(R.id.wapconnect_securitytype_value);
		securityType.setText(securityString);

		TextView signalStrength = (TextView) dialogView.findViewById(R.id.wapconnect_signalstrength_value);
		signalStrength.setText(signalString);
		dialogBuilder.setCustomView(dialogView);

		// setup buttons
		dialogBuilder.setPositiveButton(R.string.connect, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), getResources().getString(R.string.connecting_as_wan), Toast.LENGTH_LONG).show();
				
				WANProfile wanprofile = new WANProfile();
				wanprofile.setAuthmode(wap.getSecurityType());

				CheckBox roaming = (CheckBox) dialogView.findViewById(R.id.wapconnect_roaming);

				if (!roaming.isChecked()) {
					wanprofile.setBssid(wap.getBssid());
				}

				String ssid = wap.getSsid();
				if (ssid.equals("")) {
					// the user has tried to connect to a blank SSID.
					EditText ssidField = (EditText) dialogView.findViewById(R.id.wapconnect_ssidset_field);
					ssid = ssidField.getText().toString();
				}
				wanprofile.setSsid(ssid);

				// wanprofile.setBssid(wap.getBssid());

				wanprofile.setUid(wap.getBssid());
				wanprofile.setEnabled(true);

				TextView tv = (TextView) dialogView.findViewById(R.id.wapconnect_password_field);
				String pass = tv.getText().toString();

				if (!wap.getAuthmode().equals("none")) {
					wanprofile.setSerializePassword(pass);

					// wanprofile.setWpapsk(pass);
					wanprofile.setWpacipher(wap.getCipher());
				}
				Intent intent = new Intent();
				intent.putExtra("wanprofile", wanprofile);
				
				getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
			}
		});

		dialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		return dialogBuilder.create();
	}
}