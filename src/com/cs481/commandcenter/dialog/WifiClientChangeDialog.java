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

public class WifiClientChangeDialog extends DialogFragment {
	private String newMode;

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public WifiClientChangeDialog() {
		Log.i(CommandCenterActivity.TAG, "Created wifi client change fragment.");
		// context = getActivity();
	}

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public static WifiClientChangeDialog newInstance(
			String newMode) {
		WifiClientChangeDialog wwdf = new WifiClientChangeDialog();
		wwdf.newMode = newMode;
		return wwdf;
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
		theme.resolveAttribute(android.R.attr.windowBackground, typedValue,
				true);
		// Color color = getResources().getColor(colorid);

		dialogBuilder.setDividerColor(typedValue.resourceId);
		dialogBuilder.setTitleColor(typedValue.resourceId);
		dialogBuilder.setTitle(getActivity().getResources().getString(R.string.warning_clientchange));
		dialogBuilder
				.setMessage(String
						.format("Are you sure you want to change the Wireless Client mode to %s? This may affect network connectivity.",
								newMode));

		// setup buttons
		dialogBuilder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						/*
						 * Toast.makeText(getActivity(),
						 * getResources().getString(R.string.connecting_as_wan),
						 * Toast.LENGTH_LONG).show();
						 */
						Toast.makeText(getActivity(),
								"Changing WiFi client mode...",
								Toast.LENGTH_LONG).show();
						getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
						// hostingFragment.commitClientChange();
					}
				});

		dialogBuilder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// hostingFragment.revertClientChange();
						getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());

					}
				});

		return dialogBuilder.create();
	}
}