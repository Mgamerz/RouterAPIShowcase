package com.cs481.commandcenter.fragments.preferences;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.cs481.commandcenter.R;

/**
 * Preference Fragment for the ui preferences page. THIS MUST RUN IN ITS OWN
 * ACTIVITY AS IT DOES NOT WORK WITH THE SUPPORT FRAGMENT MANAGER...
 * 
 * @author Mgamerz
 * 
 */
public class AboutPrefsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.about_prefs);
		String versionName = "Error getting version name";
		try {
			versionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Preference build = findPreference(getResources().getString(R.string.prefs_app_version));
		build.setSummary(versionName);
	}
}