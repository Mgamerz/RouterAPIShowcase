package com.cs481.mobilemapper.activities;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.fragments.preferences.UIPrefsFragment;

public class PrefsActivity extends PreferenceActivity {
	public static final int THEME_RED = 0;
	public static final int THEME_BLUE = 1;
	public static final int THEME_GREEN = 2;
	public static final int THEME_BLACK = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(Utility.getTheme(this));
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.prefs_headers, target);
	}

	@Override
	protected boolean isValidFragment(String fragmentName) {
		if (UIPrefsFragment.class.getName().equals(fragmentName)) {
			return true;
		}
		return false;

	}
}