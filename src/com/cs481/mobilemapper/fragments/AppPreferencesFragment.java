package com.cs481.mobilemapper.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.cs481.mobilemapper.R;

public class AppPreferencesFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.app_prefs);
	}
}
