package com.cs481.commandcenter.activities;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.fragments.preferences.AboutPrefsFragment;
import com.cs481.commandcenter.fragments.preferences.AdvancedPrefsFragment;
import com.cs481.commandcenter.fragments.preferences.SecurityPrefsFragment;
import com.cs481.commandcenter.fragments.preferences.UIPrefsFragment;

public class PrefsActivity extends PreferenceActivity {
	public static final int THEME_RED = 0;
	public static final int THEME_BLUE = 1;
	public static final int THEME_GREEN = 2;
	public static final int THEME_BLACK = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// setTheme(Utility.getTheme(this));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.prefs_headers, target);
	}

	@Override
	protected boolean isValidFragment(String fragmentName) {
		//This is hideous... required by Android 4.4 kitkat, pointless in my opinion
		Log.i(CommandCenterActivity.TAG,
				"Validating: " + UIPrefsFragment.class.getName() + " vs "
						+ fragmentName);
		if (UIPrefsFragment.class.getName().equals(fragmentName)
				|| SecurityPrefsFragment.class.getName().equals(fragmentName)
				|| AboutPrefsFragment.class.getName().equals(fragmentName) ||

				AdvancedPrefsFragment.class.getName().equals(fragmentName)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prefs_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_bugreport:
			Intent intent = new Intent(this, BugReportActivity.class);
			startActivity(intent);
			return true;
		default:
			return false;
		}
	}
}