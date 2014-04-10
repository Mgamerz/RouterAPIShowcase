package com.cs481.commandcenter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.activities.CommandCenterActivity;

/**
 * Dialog fragment that asks the user if they're sure
 * about their router commit
 * @author Mike Perez
 */

public class ToggleWAPDialogFragment extends DialogFragment {

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public ToggleWAPDialogFragment() {
		Log.i(CommandCenterActivity.TAG, "Created reboot router fragment.");
		// context = getActivity();
	}

	/**
	 * Creates a new RebootRouterDialogFragment. This fragment currently doesn't have any parameters
	 * but using this method keeps it in line with the other dialogs and allows easier parameter adding in the future.
	 */
	public static ToggleWAPDialogFragment newInstance(String apName) {
		ToggleWAPDialogFragment dwdf = new ToggleWAPDialogFragment();
		return dwdf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save data on rotate. This bundle will be passed to onCreate() by
		// Android.
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		HoloDialogBuilder dialogBuilder = new HoloDialogBuilder(getActivity());

		Theme theme = getActivity().getTheme();
		TypedValue typedValue = new TypedValue();
		theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true);

		dialogBuilder.setDividerColor(typedValue.resourceId);
		dialogBuilder.setTitleColor(typedValue.resourceId);
		dialogBuilder.setTitle(getActivity().getResources().getString(R.string.toggle_wap_title));
		dialogBuilder.setMessage(getResources().getString(R.string.toggle_wap_confirm));

		// setup buttons
		dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Toast.makeText(getActivity(), "Disabling wifi radio...", Toast.LENGTH_LONG).show();
				getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
			}
		});

		dialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// hostingFragment.revertClientChange();
				getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
			}
		});

		return dialogBuilder.create();
	}
}