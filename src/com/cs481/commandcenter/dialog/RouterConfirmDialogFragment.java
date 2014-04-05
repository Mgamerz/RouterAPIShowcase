package com.cs481.commandcenter.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.Profile;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.LoginActivity;
import com.cs481.commandcenter.activities.PINActivity;
import com.cs481.commandcenter.responses.ecm.routers.Router;

public class RouterConfirmDialogFragment extends DialogFragment {
	private Router router;
	private AuthInfo authInfo;

	// Context context;

	/**
	 * This constructor must be empty or the Fragment won't be able to start
	 * when android recreates this fragment.
	 */
	public RouterConfirmDialogFragment() {
		Log.i(CommandCenterActivity.TAG, "Created routerconfirmdialogfragment.");
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
		// TODO : Disable the save profile checkbox if the profile already
		// exists
		HoloDialogBuilder alertDialogBuilder = new HoloDialogBuilder(getActivity());

		Theme theme = getActivity().getTheme();
		TypedValue typedValue = new TypedValue();
		theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true);

		alertDialogBuilder.setDividerColor(typedValue.resourceId);
		alertDialogBuilder.setTitleColor(typedValue.resourceId);
		alertDialogBuilder.setTitle(router.getName());

		LayoutInflater inflator = getActivity().getLayoutInflater();
		final View v = inflator.inflate(R.layout.dialog_routerconfirm, null);
		TextView tv = (TextView) v.findViewById(R.id.rcdialog_text);
		String text = tv.getText().toString();
		text = String.format(text, router.getName());
		tv.setText(text);

		alertDialogBuilder.setCustomView(v);
		alertDialogBuilder.setPositiveButton(android.R.string.yes, null);
		alertDialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		return alertDialogBuilder.create();
	}

	@Override
	/**
	 * This method is called when the PIN activity finishes and returns a result to this.
	 * This will save a profile on a successful activity result and dismiss the dialog box.
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case LoginActivity.PROFILE_PIN_ENCRYPT:
			Log.i(CommandCenterActivity.TAG, "ACTIVITY HAS FINISHED, RESULT METHOD() IN FRAGMENT");
			if (requestCode == LoginActivity.PROFILE_PIN_ENCRYPT) {
				// Make sure the request was successful
				if (resultCode == Activity.RESULT_OK) {
					AuthInfo encryptedInfo = Utility.encryptAuthInfo(getActivity(), data.getExtras().getString("pin"), authInfo);
					if (encryptedInfo != null) {
						Profile profile = new Profile();
						profile.setAuthInfo(encryptedInfo);
						profile.setProfileName(router.getName());
						Utility.saveProfile(getActivity(), profile);
					} else {
						Toast.makeText(getActivity(), "Error saving profile.", Toast.LENGTH_LONG).show();
					}
					dismiss();
					Intent intent = new Intent(getActivity(), CommandCenterActivity.class);
					intent.putExtra("authInfo", authInfo);
					intent.putExtra("ab_subtitle", router.getName()); // changes
																		// subtitle.
					startActivity(intent);
					// getActivity().finish();
					return; // makes sure nothing else happens in this
				} // end pin
				break;
			}
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onStart() {
		super.onStart(); // super.onStart() is where dialog.show() is actually
							// called on the underlying dialog, so we have to do
							// it after this point
		AlertDialog d = (AlertDialog) getDialog();
		if (d != null) {
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Do something
					// TODO Auto-generated method stub
					// Check to see if the 'save profile' is selected.
					CheckBox sp = (CheckBox) getDialog().findViewById(R.id.ecm_save_as_profile);
					if (sp.isChecked()) {
						Intent intent = new Intent(getActivity(), PINActivity.class);
						intent.putExtra("createpin", false); // verify
						startActivityForResult(intent, LoginActivity.PROFILE_PIN_ENCRYPT);
					} else {

						Intent intent = new Intent(getActivity(), CommandCenterActivity.class);
						intent.putExtra("authInfo", authInfo);
						intent.putExtra("ab_subtitle", router.getName()); // changes
																			// subtitle.
						startActivity(intent);
						getActivity().finish();

						// Dismiss once everything is OK.
						dismiss();
					}
				}
			});
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

	/**
	 * Creates a new RouterConfirmDialogFragment, which allows the user to
	 * confirm editing this router, as well as choosing to save a profile.
	 * 
	 * @param router
	 * @param authInfo
	 * @return
	 */
	public static RouterConfirmDialogFragment newInstance(Router router, AuthInfo authInfo) {
		// TODO Auto-generated method stub
		RouterConfirmDialogFragment rcdf = new RouterConfirmDialogFragment();
		rcdf.router = router;
		rcdf.authInfo = authInfo;
		return rcdf;
	}
}