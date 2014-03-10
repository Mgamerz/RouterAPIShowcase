package com.cs481.commandcenter.fragments.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.Toast;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.activities.CommandCenterActivity;
import com.cs481.commandcenter.activities.LoginActivity;
import com.cs481.commandcenter.activities.PINActivity;

/**
 * Preference Fragment for the ui preferences page.
 * THIS MUST RUN IN ITS OWN ACTIVITY AS IT DOES NOT WORK WITH THE SUPPORT FRAGMENT MANAGER...
 * @author Mgamerz
 *
 */
public class SecurityPrefsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.security_prefs);
    }
    
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
    	super.onViewCreated(v, savedInstanceState);
    	
    	Preference deleteAllProfiles = findPreference(getActivity().getResources().getString(R.id.prefs_delete_profiles));
    	deleteAllProfiles.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// should create a new pin screen.
				// Prepare new intent.
				Utility.deleteAllProfiles(getActivity());
				Toast.makeText(getActivity(), "debug: Deleted all profiles.", Toast.LENGTH_LONG).show();
				return true;
			}
		});
    }
}