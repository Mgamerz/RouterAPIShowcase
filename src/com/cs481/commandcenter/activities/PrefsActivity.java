package com.cs481.commandcenter.activities;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.fragments.preferences.SecurityPrefsFragment;
import com.cs481.commandcenter.fragments.preferences.UIPrefsFragment;

public class PrefsActivity extends PreferenceActivity {
	public static final int THEME_RED = 0;
	public static final int THEME_BLUE = 1;
	public static final int THEME_GREEN = 2;
	public static final int THEME_BLACK = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//setTheme(Utility.getTheme(this));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.prefs_headers, target);
	}

	@Override
	protected boolean isValidFragment(String fragmentName) {
		Log.i(CommandCenterActivity.TAG,
				"Validating: " + UIPrefsFragment.class.getName() + " vs "
						+ fragmentName);
		if (UIPrefsFragment.class.getName().equals(fragmentName)||
				SecurityPrefsFragment.class.getName().equals(fragmentName)) {
			return true;
		}
		return false;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return false;
		}
	}
}