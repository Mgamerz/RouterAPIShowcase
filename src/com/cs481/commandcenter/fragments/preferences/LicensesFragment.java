package com.cs481.commandcenter.fragments.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.cs481.commandcenter.R;

/**
 * Preference Fragment for the ui preferences page. THIS MUST RUN IN ITS OWN
 * ACTIVITY AS IT DOES NOT WORK WITH THE SUPPORT FRAGMENT MANAGER...
 * 
 * @author Mgamerz
 * 
 */
public class LicensesFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.license_prefs);

	}
}