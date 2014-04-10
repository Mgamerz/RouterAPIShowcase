package com.cs481.commandcenter.fragments.preferences;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.dialog.LicenseDialogFragment;

/**
 * Preference Fragment for the ui preferences page. THIS MUST RUN IN ITS OWN
 * ACTIVITY AS IT DOES NOT WORK WITH THE SUPPORT FRAGMENT MANAGER...
 * @author Mike Perez
 */
public class LicensesFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.license_prefs);
		
		Preference robospice = findPreference(getResources().getString(R.string.prefs_open_robospice));
		robospice.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				LicenseDialogFragment.displayLicenseDialogFragment(getActivity().getFragmentManager(), R.raw.license_robospice);
				return true;
			}
		});
		
		Preference pulltorefresh = findPreference(getResources().getString(R.string.prefs_open_pulltorefresh));
		pulltorefresh.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				LicenseDialogFragment.displayLicenseDialogFragment(getActivity().getFragmentManager(), R.raw.license_pulltorefresh);
				return true;
			}
		});
		
		Preference smoothprogressbar = findPreference(getResources().getString(R.string.prefs_open_smoothprogressbar));
		smoothprogressbar.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				LicenseDialogFragment.displayLicenseDialogFragment(getActivity().getFragmentManager(), R.raw.license_smoothprogressbar);
				return true;
			}
		});
		
		Preference licensesdisplayer = findPreference(getResources().getString(R.string.prefs_androidlicensespage));
		licensesdisplayer.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				LicenseDialogFragment.displayLicenseDialogFragment(getActivity().getFragmentManager(), R.raw.license_androidlicensespage);
				return true;
			}
		});
	}
}