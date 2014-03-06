package com.cs481.mobilemapper.fragments.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.view.View;

import com.cs481.commandcenter.R;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.activities.LoginActivity;
import com.cs481.mobilemapper.activities.PINActivity;

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
    	
    	Preference createpin = findPreference(getActivity().getResources().getString(R.id.prefs_create_pin));
    	createpin.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// should create a new pin screen.
				// Prepare new intent.
				Intent intent = new Intent(getActivity(),
						PINActivity.class);
				intent.putExtra("createpin", true);
				// start the new activity, and prevent this one from being
				// returned to unless logout is chosen.
				startActivity(intent);
				return true;
			}
		});
    }
}