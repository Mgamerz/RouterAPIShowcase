package com.cs481.mobilemapper.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.dialog.HoloDialogBuilder;
import com.cs481.mobilemapper.responses.status.wlan.WAP;

public class WifiWanDialog extends DialogFragment {
	WAP wap;
	AuthInfo authInfo;
	Context context;

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public WifiWanDialog() {
		Log.i(CommandCenterActivity.TAG, "Created fragment.");
		// context = getActivity();
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
		HoloDialogBuilder dialogBuilder = new HoloDialogBuilder(
				getActivity());
		dialogBuilder.setTitleColor(getResources().getString(R.color.Black));
		String title = wap.getSsid();
		boolean hiddenSsid = false; // used to show the SSID field
		if (title.equals("")) {
			title = "Hidden SSID";
			hiddenSsid = true;
		}

		dialogBuilder.setTitle(title);
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View dialogView = inflater.inflate(R.layout.dialog_wifiwan, null);

		if (!hiddenSsid) {
			Log.i(CommandCenterActivity.TAG, "Not a hidden SSID");
			LinearLayout layout = (LinearLayout) dialogView
					.findViewById(R.id.wapconnect_ssidlayout);
			layout.setVisibility(LinearLayout.GONE);
			Log.i(CommandCenterActivity.TAG, "Layout visibility: "+layout.getVisibility());
		}

		// Get dynamic content, set font to BOLD
		SpannableString securityString = new SpannableString(wap.getAuthmode());
		securityString.setSpan(new StyleSpan(Typeface.BOLD), 0,
				securityString.length(), 0);

		SpannableString signalString = new SpannableString(
				Utility.rssiToSignalLiteral(wap.getRssi(), getResources()));
		signalString.setSpan(new StyleSpan(Typeface.BOLD), 0,
				signalString.length(), 0);

		// Set the dynamic content
		TextView securityType = (TextView) dialogView
				.findViewById(R.id.wapconnect_securitytype_value);
		securityType.setText(securityString);

		TextView signalStrength = (TextView) dialogView
				.findViewById(R.id.wapconnect_signalstrength_value);
		signalStrength.setText(signalString);

		dialogBuilder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "Connecting as WAN...",
								Toast.LENGTH_LONG).show();
					}
				});

		dialogBuilder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		dialogBuilder.setCustomView(dialogView);
		return dialogBuilder.create();
	}

	/**
	 * Sets the authorization information and the router information, allowing
	 * this dialog to navigate to the CommandCenter activity with all required
	 * information.
	 * 
	 * @param router
	 *            Router object containing this router information
	 * @param authInfo
	 *            Auth information allowing the user to edit this router
	 */
	public void setData(WAP wap, AuthInfo authInfo) {
		this.wap = wap;
		this.authInfo = authInfo;
	}
}