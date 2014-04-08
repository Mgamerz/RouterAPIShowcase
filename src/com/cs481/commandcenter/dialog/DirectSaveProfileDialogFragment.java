package com.cs481.commandcenter.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.LoginActivity;
import com.cs481.commandcenter.activities.PINActivity;
import com.cs481.commandcenter.fragments.DirectLoginFragment;

/**
 * This class shows the Direct profile save dialog
 * @author Mike Perez
 * 
 */
public class DirectSaveProfileDialogFragment extends DialogFragment {
	AuthInfo authInfo;
	Context context;

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public DirectSaveProfileDialogFragment() {
		Log.i(CommandCenterActivity.TAG, "Created direct save fragment.");
		// context = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			authInfo = savedInstanceState.getParcelable("authInfo");
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("authInfo", authInfo);
	}

	@Override
	public void onStart() {
		super.onStart();
		AlertDialog d = (AlertDialog) getDialog();
		final CheckBox sp = (CheckBox) d.findViewById(R.id.direct_save_as_profile);
		LoginActivity activity = (LoginActivity) getActivity();
		final DirectLoginFragment dlf = (DirectLoginFragment) activity.getSupportFragmentManager().findFragmentByTag(DirectLoginFragment.class.getName());

		// prevent the button from closing the dialog in the event no correct
		// pin can be entered (and it has a callback on it too)

		if (d != null) {
			Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (sp.isChecked()) {
						Intent intent = new Intent(getActivity(), PINActivity.class);
						intent.putExtra("createpin", false); // verify
						startActivityForResult(intent, LoginActivity.PROFILE_PIN_ENCRYPT);
					} else {
						dlf.testLogin(false, null);
						// Dismiss once everything is OK.
						dismiss();
					}
				}
			});
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		HoloDialogBuilder alertDialogBuilder = new HoloDialogBuilder(getActivity());

		Theme theme = getActivity().getTheme();
		TypedValue typedValue = new TypedValue();
		theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true);
		alertDialogBuilder.setDividerColor(typedValue.resourceId);
		alertDialogBuilder.setTitleColor(typedValue.resourceId);
		alertDialogBuilder.setTitle(getResources().getString(R.string.direct_profile_dialog_title));

		LayoutInflater inflator = getActivity().getLayoutInflater();
		final View v = inflator.inflate(R.layout.dialog_directprofile, null);
		alertDialogBuilder.setCustomView(v);
		// null should be your on click listener
		alertDialogBuilder.setPositiveButton(android.R.string.yes, null);

		return alertDialogBuilder.create();
	}

	/**
	 * Creates a new instance of this dialog with the specified authInfo already
	 * initialized.
	 * 
	 * @param authInfo
	 *            authinfo to use
	 * @return new instance of this fragment
	 */
	public static DirectSaveProfileDialogFragment newInstance(AuthInfo authInfo) {
		DirectSaveProfileDialogFragment dspd = new DirectSaveProfileDialogFragment();
		dspd.authInfo = authInfo;
		Log.i(CommandCenterActivity.TAG, "Started DSPDF with authInfo " + authInfo);
		return dspd;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(CommandCenterActivity.TAG, "onActivityResult() in DSPDF: " + requestCode + " with result " + resultCode);
		if (resultCode != Activity.RESULT_OK) {
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}
		switch (requestCode) {
		case LoginActivity.PROFILE_PIN_ENCRYPT:
			AuthInfo encryptedInfo = Utility.encryptAuthInfo(getActivity(), data.getExtras().getString("pin"), authInfo);
			LoginActivity activity = (LoginActivity) getActivity();
			DirectLoginFragment dlf = (DirectLoginFragment) activity.getSupportFragmentManager().findFragmentByTag(DirectLoginFragment.class.getName());
			dismiss();
			dlf.testLogin(true, encryptedInfo);
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}