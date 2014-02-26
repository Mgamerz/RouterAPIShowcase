package com.cs481.mobilemapper.fragments.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;

import com.cs481.mobilemapper.R;

/**
 * Preference Fragment for the ui preferences page.
 * THIS MUST RUN IN ITS OWN ACTIVITY AS IT DOES NOT WORK WITH THE SUPPORT FRAGMENT MANAGER...
 * @author Mgamerz
 *
 */
public class UIPrefsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.ui_prefs);
    }
    
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
    	//v.setBackgroundColor(getResources().getColor(R.color.DarkBlue));
    }
}