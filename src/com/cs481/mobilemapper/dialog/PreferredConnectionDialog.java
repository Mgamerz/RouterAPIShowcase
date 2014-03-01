package com.cs481.mobilemapper.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.cs481.mobilemapper.Profile;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.activities.CommandCenterActivity;

public class PreferredConnectionDialog extends DialogFragment {
	private String connectionType;
	
	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public PreferredConnectionDialog() {
		Log.i(CommandCenterActivity.TAG, "Created fragment.");
		// context = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			connectionType = savedInstanceState.getParcelable("connectionType");
		}
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        HoloDialogBuilder alertDialogBuilder = new HoloDialogBuilder(getActivity());
        alertDialogBuilder.setTitle(connectionType + " Connection");
        Resources resources = getResources();
        
        // THIS MAY NEED TO GET CHANGED (following two lines):
        alertDialogBuilder.setTitleColor(resources.getString(R.color.CradlepointRed));
        alertDialogBuilder.setDividerColor(resources.getString(R.color.CradlepointRed));
        
        LayoutInflater inflator = getActivity().getLayoutInflater();
        final View v = inflator.inflate(R.layout.dialog_preferredconnection, null);
        
        alertDialogBuilder.setCustomView(v);
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Check to see if the 'save profile' is selected.
				CheckBox askAgain = (CheckBox) v.findViewById(R.id.checkbox_donotaskagain);
				if (askAgain.isChecked()){
					Profile profile = new Profile();
					Utility.saveProfile(profile);
				}
				
				Intent intent = new Intent(getActivity(), CommandCenterActivity.class);
				intent.putExtra("ab_subtitle", connectionType); //changes subtitle.
				startActivity(intent);
				getActivity().finish();
			}
		});
        
        alertDialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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