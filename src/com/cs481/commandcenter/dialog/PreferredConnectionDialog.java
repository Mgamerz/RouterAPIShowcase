package com.cs481.commandcenter.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.cs481.commandcenter.R;

/**
 * Dialog fragment that asks the user if their choice
 * of ECM or Direct connect is their preferred
 * choice of connection type
 * @author Sasa Rkman
 *
 */

public class PreferredConnectionDialog extends DialogFragment {
	private String connectionType;
	
	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public PreferredConnectionDialog() {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			connectionType = savedInstanceState.getString("connectionType");
		}
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        HoloDialogBuilder alertDialogBuilder = new HoloDialogBuilder(getActivity());
        Theme theme = getActivity().getTheme();
		TypedValue typedValue = new TypedValue();
		theme.resolveAttribute(android.R.attr.windowBackground, typedValue,
				true);
		// Color color = getResources().getColor(colorid);

		alertDialogBuilder.setDividerColor(typedValue.resourceId);
		alertDialogBuilder.setTitleColor(typedValue.resourceId);
        alertDialogBuilder.setTitle(connectionType + " Connection");
        final Resources resources = getResources();
        
        // THIS MAY NEED TO GET CHANGED (following two lines):
        alertDialogBuilder.setTitleColor(resources.getString(R.color.CradlepointRed));
        alertDialogBuilder.setDividerColor(resources.getString(R.color.CradlepointRed));
        
        LayoutInflater inflator = getActivity().getLayoutInflater();
        final View v = inflator.inflate(R.layout.dialog_preferredconnection, null);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		final SharedPreferences.Editor editor = prefs.edit();
        
        alertDialogBuilder.setCustomView(v);
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				editor.putBoolean("prefs_connection_dontAskAgain", true);
				editor.putString(getResources().getString(R.string.prefskey_connection_type), connectionType);
				editor.commit();
			}
		});
        
        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	CheckBox dontAskAgain = (CheckBox) v.findViewById(R.id.checkbox_donotaskagain);
            	if(dontAskAgain.isChecked()){
            		editor.putBoolean("prefs_connection_dontAskAgain", true);
            		editor.commit();
            	}
                dialog.dismiss();
            }
        });


        return alertDialogBuilder.create();
    }

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("connectionType", connectionType);
	}

	public static PreferredConnectionDialog newInstance(String connectionType) {
		PreferredConnectionDialog pcd = new PreferredConnectionDialog();
		pcd.connectionType = connectionType;
		return pcd;
	}
}