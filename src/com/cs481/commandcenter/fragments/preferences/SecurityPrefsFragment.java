package com.cs481.commandcenter.fragments.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.view.View;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.dialog.DeleteProfilesPINDialog;

/**
 * Preference Fragment for the ui preferences page.
 * @author Mike Perez
 */
public class SecurityPrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.security_prefs);
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
			initSummary(getPreferenceScreen().getPreference(i));
		}
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    
    private void initSummary(Preference p) {
        if (p instanceof PreferenceCategory) {
            PreferenceCategory pCat = (PreferenceCategory) p;
            for (int i = 0; i < pCat.getPreferenceCount(); i++) {
                initSummary(pCat.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }
    
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
    	super.onViewCreated(v, savedInstanceState);
    	
    	Preference deleteAllProfiles = findPreference(getActivity().getResources().getString(R.string.prefs_delete_profiles));
    	deleteAllProfiles.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				DeleteProfilesPINDialog dppdFragment = DeleteProfilesPINDialog.newInstance(false);
				dppdFragment.show(getFragmentManager(), dppdFragment.getClass().getName());
				return true;
			}
		});
    	
       	Preference clearPIN = findPreference(getActivity().getResources().getString(R.string.prefs_delete_pin));
       	clearPIN.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				DeleteProfilesPINDialog dppdFragment = DeleteProfilesPINDialog.newInstance(true);
				dppdFragment.show(getFragmentManager(), dppdFragment.getClass().getName());
				return true;
			}
		});
    }
    
    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            p.setSummary(editTextPref.getText());
        }
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		updatePrefSummary(findPreference(key));
	}
}