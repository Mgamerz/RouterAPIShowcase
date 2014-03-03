package com.cs481.mobilemapper.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.Profile;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.activities.LoginActivity;
import com.cs481.mobilemapper.activities.PINActivity;
import com.cs481.mobilemapper.responses.ecm.routers.Router;

public class RouterConfirmDialogFragment extends DialogFragment {
	private Router router;
	private AuthInfo authInfo;

	// Context context;

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public RouterConfirmDialogFragment() {
		Log.i(CommandCenterActivity.TAG, "Created fragment.");
		// context = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			router = savedInstanceState.getParcelable("router");
			authInfo = savedInstanceState.getParcelable("authInfo");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		HoloDialogBuilder alertDialogBuilder = new HoloDialogBuilder(
				getActivity());
		alertDialogBuilder.setTitle(router.getName());
		Resources resources = getResources();

		// THIS MAY NEED TO GET CHANGED (following two lines):
		alertDialogBuilder.setTitleColor(resources
				.getString(R.color.CradlepointRed));
		alertDialogBuilder.setDividerColor(resources
				.getString(R.color.CradlepointRed));

		LayoutInflater inflator = getActivity().getLayoutInflater();
		final View v = inflator.inflate(R.layout.dialog_routerconfirm, null);
		TextView tv = (TextView) v.findViewById(R.id.rcdialog_text);
		String text = tv.getText().toString();
		text = String.format(text, router.getName());
		tv.setText(text);

		alertDialogBuilder.setCustomView(v);
		alertDialogBuilder.setPositiveButton(android.R.string.yes, null);
		alertDialogBuilder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		final AlertDialog d = alertDialogBuilder.create();
		d.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				Log.i(CommandCenterActivity.TAG, "Showing dialog!");
				Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Do something
						// TODO Auto-generated method stub
						// Check to see if the 'save profile' is selected.
						CheckBox sp = (CheckBox) v
								.findViewById(R.id.ecm_save_as_profile);
						if (sp.isChecked()) {
							Intent intent = new Intent(getActivity(), PINActivity.class);
							intent.putExtra("createpin", false); // verify
							startActivityForResult(intent, LoginActivity.PIN_UNLOCK);
						} else {

							Intent intent = new Intent(getActivity(),
									CommandCenterActivity.class);
							intent.putExtra("authInfo", authInfo);
							intent.putExtra("ab_subtitle", router.getName()); // changes
																				// subtitle.
							startActivity(intent);
							getActivity().finish();

							// Dismiss once everything is OK.
							d.dismiss();
						}
					}
				});
			}
		});

		return alertDialogBuilder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == LoginActivity.PIN_UNLOCK) {
			// Make sure the request was successful
			if (resultCode == Activity.RESULT_OK) {
				AuthInfo encryptedInfo = Utility.encryptAuthInfo(getActivity(),
						data.getExtras().getString("pin"), authInfo);
				Profile profile = new Profile();
				profile.setAuthInfo(encryptedInfo);
				profile.setProfileName(router.getName());
				Utility.saveProfile(getActivity(), profile);
				dismiss();
				Intent intent = new Intent(getActivity(),
						CommandCenterActivity.class);
				intent.putExtra("authInfo", authInfo);
				intent.putExtra("ab_subtitle", router.getName()); // changes
																	// subtitle.
				startActivity(intent);
				getActivity().finish();
				return; // makes sure nothing else happens in this
			} // end pin
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("router", router);
		outState.putParcelable("authInfo", authInfo);
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
	public void setData(Router router, AuthInfo authInfo) {
		this.router = router;
		this.authInfo = authInfo;
	}

	public static RouterConfirmDialogFragment newInstance(Router router,
			AuthInfo authInfo) {
		// TODO Auto-generated method stub
		RouterConfirmDialogFragment rcdf = new RouterConfirmDialogFragment();
		rcdf.router = router;
		rcdf.authInfo = authInfo;
		return rcdf;
	}
}