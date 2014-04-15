package com.cs481.commandcenter.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.fragments.PINFragment;


/**
 * This activity exists because the PIN screen would be required to show up in both Preferences and LoginActivity, which use
 * different fragment managers, because they are completely isolated.
 * @author Mike Perez
 *
 */
public class PINActivity extends SpiceActivity {

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setTheme(Utility.getTheme(this));
		setContentView(R.layout.activity_pin);
		
		SharedPreferences crypto = getSharedPreferences(
				getResources().getString(R.string.crypto_prefsdb),
				Context.MODE_PRIVATE);
		String verify = crypto.getString("validator", null); // this should turn
															// into the uuid
															// stored in the
															// prefs.
		boolean createpin = false; //default to validate mode.
		if (verify == null){
			createpin = true;
		}
		
		// Retain fragments on rotation
		if (null == savedInstanceState) {
			// set you initial fragment object

			// create first UI fragment and set it up
			Fragment fragment = PINFragment.newInstance(createpin);

			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();

			// Inject our fragment
			ft.replace(R.id.pin_fragment, fragment);
			// Commit to the UI
			ft.commit();
		}
	}
}