package com.cs481.mobilemapper.activities;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.fragments.preferences.RootPrefsFragment;

public class PrefsActivity extends PreferenceActivity {
	protected static final int THEME_RED = 0;
	protected static final int THEME_BLUE = 1;
	protected static final int THEME_GREEN = 2;
	protected static final int THEME_BLACK = 3;

	@Override
	public void onCreate(Bundle savedInstanceState){
		// set theme
		SharedPreferences mPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String theme = mPrefs.getString(
				getResources().getString(R.string.prefskey_theme), "0");
		int themeid = Integer.parseInt(theme);
		switch (themeid) {
		case PrefsActivity.THEME_BLUE:
			setTheme(R.style.BlueAppTheme);
			break;
		default:
			setTheme(R.style.RedAppTheme);
		}
	}
	
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