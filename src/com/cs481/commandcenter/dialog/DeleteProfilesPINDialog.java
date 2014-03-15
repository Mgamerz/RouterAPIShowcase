package com.cs481.commandcenter.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;

public class DeleteProfilesPINDialog extends DialogFragment {
	// Context context;

	private boolean fullwipe = false; // wipes pin validator too if set to true

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public DeleteProfilesPINDialog() {
		Log.i(CommandCenterActivity.TAG, "Created delete profiles  dialog fragment.");
		// context = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			fullwipe = savedInstanceState.getBoolean("fullwipe");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		HoloDialogBuilder alertDialogBuilder = new HoloDialogBuilder(getActivity());

		// Theme theme = getActivity().getTheme();
		// TypedValue typedValue = new TypedValue();
		// theme.resolveAttribute(android.R.attr.windowBackground, typedValue,
		// true);
		// Color color = getResources().getColor(colorid);

		// alertDialogBuilder.setDividerColor(typedValue.resourceId);
		// alertDialogBuilder.setTitleColor(typedValue.resourceId);
		Resources resources = getResources();

		String title = resources.getString((fullwipe) ? R.string.prefs_delete_pin : R.string.prefs_delete_profiles);
		alertDialogBuilder.setTitle(title);
		String message = resources.getString((fullwipe) ? R.string.wipe_all_warning : R.string.wipe_profiles_warning);
		alertDialogBuilder.setMessage(message);

		alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Utility.deleteAllProfiles(getActivity());

				if (fullwipe) {
					// wipe pin authenticator as well
					SharedPreferences crypto = getActivity().getSharedPreferences(getResources().getString(R.string.crypto_prefsdb), Context.MODE_PRIVATE);
					SharedPreferences.Editor cryptoremover = crypto.edit();
					cryptoremover.clear();
					cryptoremover.commit();
				}
				dismiss();
				Utility.restartApp(getActivity());
			}
		});
		alertDialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dismiss();
			}
		});

		return alertDialogBuilder.create();
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("fullwipe", fullwipe);
	}

	public static DeleteProfilesPINDialog newInstance(boolean fullwipe) {
		// TODO Auto-generated method stub
		DeleteProfilesPINDialog dppd = new DeleteProfilesPINDialog();
		dppd.fullwipe = fullwipe;
		return dppd;
	}
}