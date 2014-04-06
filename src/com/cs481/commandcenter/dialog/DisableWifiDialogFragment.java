package com.cs481.commandcenter.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.activities.CommandCenterActivity;

public class DisableWifiDialogFragment extends DialogFragment {

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public DisableWifiDialogFragment() {
		Log.i(CommandCenterActivity.TAG, "Created wifi enabled change fragment.");
		// context = getActivity();
	}

	/**
	 * Creates a new DisableWifiDialogFragment. This fragment currently doesn't have any parameters
	 * but using this method keeps it in line with the other dialogs and allows easier parameter adding in the future.
	 */
	public static DisableWifiDialogFragment newInstance() {
		DisableWifiDialogFragment dwdf = new DisableWifiDialogFragment();
		// dwdf.newMode = newMode;
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
		dialogBuilder.setTitle(getActivity().getResources().getString(R.string.warning_clientchange));
		dialogBuilder.setMessage(getResources().getString(R.string.confirm_wifi_disable));

		// setup buttons
		dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), "Disabling wifi radio...", Toast.LENGTH_LONG).show();
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