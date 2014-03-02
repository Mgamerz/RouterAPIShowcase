package com.cs481.mobilemapper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.fragments.PINFragment;


/**
 * This activity exists because the PIN screen would be required to show up in both Preferences and LoginActivity, which use
 * different fragment managers, because they are completely isolated.
 * @author Mgamerz
 *
 */
public class PINActivity extends FragmentActivity {

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin);
		
		Intent intent = getIntent();
		boolean createpin = intent.getExtras().getBoolean("createpin");
		
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
