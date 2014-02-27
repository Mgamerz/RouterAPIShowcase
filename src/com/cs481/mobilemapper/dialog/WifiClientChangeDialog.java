package com.cs481.mobilemapper.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.fragments.WifiClientFragment;
import com.cs481.mobilemapper.responses.config.wwan.WANProfile;
import com.cs481.mobilemapper.responses.status.wlan.WAP;

public class WifiClientChangeDialog extends DialogFragment {
	private WAP wap;
	private AuthInfo authInfo;
	private WifiClientFragment hostingFragment;

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public WifiClientChangeDialog() {
		Log.i(CommandCenterActivity.TAG, "Created fragment.");
		// context = getActivity();
	}
	
	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public static WifiClientChangeDialog newInstance(WifiClientFragment wawf) {
		WifiClientChangeDialog wwdf = new WifiClientChangeDialog();
		wwdf.hostingFragment = wawf;
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
		HoloDialogBuilder dialogBuilder = new HoloDialogBuilder(
				getActivity());
		
		Theme theme = getActivity().getTheme();
		TypedValue typedValue = new TypedValue();
		theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true);
		//Color color = getResources().getColor(colorid);
		
		dialogBuilder.setDividerColor(typedValue.resourceId);
		dialogBuilder.setTitleColor(typedValue.resourceId);
		dialogBuilder.setMessage(String.format("Are you sure you want to change the Wireless Client mode to %s","herp"));

		//setup buttons
		dialogBuilder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						/*Toast.makeText(getActivity(), getResources().getString(R.string.connecting_as_wan),
								Toast.LENGTH_LONG).show(); */
						Toast.makeText(getActivity(), "Changing WiFi client mode...", Toast.LENGTH_LONG).show();
						//hostingFragment.commitClientChange();
					}
				});

		dialogBuilder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//hostingFragment.revertClientChange();
						dialog.dismiss();
					}
				});

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
	
	/**
	 * This method sends a PUT request to the router, telling it to connect to a new AP as WAN.
	 * 
	 * @param wap
	 */
	public void connectAsWan(WAP wap){
		//Construct a router WANProfile object
		WANProfile wanprofile = new WANProfile();
		wanprofile.setAuthmode(wap.getAuthmode());
		wanprofile.setBssid(wap.getBssid());
		wanprofile.setSsid(wap.getSsid());
		wanprofile.setBssid(wap.getBssid());
		wanprofile.setWpacipher("aes"); //TODO figure out what this means
		wanprofile.setUid(wap.getBssid());
		wanprofile.setEnabled(true);
		
		TextView tv = (TextView) getDialog().findViewById(R.id.wapconnect_password_field);
		String pass = tv.getText().toString(); 
		wanprofile.setWpapsk(pass);
		
		hostingFragment.connectAsWAN(wanprofile);
	}
	
	
}