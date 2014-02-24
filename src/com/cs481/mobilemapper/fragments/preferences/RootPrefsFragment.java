package com.cs481.mobilemapper.fragments.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.cs481.mobilemapper.R;

/**
 * Preference Fragment for the root of all preferences. 
 * THIS MUST RUN IN ITS OWN ACTIVITY AS IT DOES NOT WORK WITH THE SUPPORT FRAGMENT MANAGER...
 * @author Mgamerz
 *
 */
public class RootPrefsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.root_prefs);
    }
}