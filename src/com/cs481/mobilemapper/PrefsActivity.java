package com.cs481.mobilemapper;

import java.util.List;

import android.preference.PreferenceActivity;

import com.cs481.mobilemapper.fragments.preferences.RootPrefsFragment;

public class PrefsActivity extends PreferenceActivity {
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.prefs_headers, target);
	}

	@Override
	protected boolean isValidFragment(String fragmentName) {
		if (RootPrefsFragment.class.getName().equals(fragmentName)) {
			return true;
		}
		return false;

	}
}