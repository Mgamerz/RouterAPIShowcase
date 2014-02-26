package com.cs481.mobilemapper.activities;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import roboguice.util.temp.Ln;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.Cryptography;
import com.cs481.mobilemapper.Profile;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.debug.DebugActivity;
import com.cs481.mobilemapper.fragments.PINFragment;
import com.cs481.mobilemapper.fragments.SplashScreenFragment;
import com.cs481.mobilemapper.listrows.ProfileListRow;

public class LoginActivity extends SpiceActivity {
	private AuthInfo authInfo;
	private ArrayList<Profile> profiles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Ln.getConfig().setLoggingLevel(Log.ERROR);
		setTheme(Utility.getTheme(this));

		setContentView(R.layout.activity_login);
		// If this is the first time the app has run, there will be no salt key
		// in the shared prefs.
		// Look it up first. If it doesn't exist, we can make one. This is not
		// the pure salt key, as ANDROID_ID is added at runtime.
		// This prevents moving the data to another device and having the
		// encryption/decryption still work.

		// Reading
		Resources resources = getResources();
		SharedPreferences crypto = getSharedPreferences(
				resources.getString(R.string.crypto_prefsdb), MODE_PRIVATE);
		String uuid = crypto.getString("uuid", null);
		if (uuid == null) {
			// writing
			SecureRandom secureRandom = new SecureRandom();
			// Do *not* seed secureRandom! Automatically seeded from system.
			try {
				KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
				keyGenerator.init(256, secureRandom);
				SecretKey key = keyGenerator.generateKey();

				SharedPreferences.Editor editor = crypto.edit();
				editor.putString("uuid",
						Base64.encodeToString(key.getEncoded(), Base64.DEFAULT));
				// Commit the edits!
				editor.commit();
			} catch (NoSuchAlgorithmException e) {
				Log.e(CommandCenterActivity.TAG,
						"This devices does not support AES (that's weird!)");
			}
		}

		// Setup the list of items
		ArrayList<Profile> profiles = Utility.getProfiles();

		// PLACEHOLDER STUFF until melissa gets the DB up
		profiles = new ArrayList<Profile>();
		Profile profile = new Profile();
		AuthInfo authInfo = new AuthInfo();
		authInfo.setEcm(true);
		profile.setProfileName("Saved Profile 1");
		profile.setAuthInfo(authInfo);
		profiles.add(profile);

		profile = new Profile();
		authInfo = new AuthInfo();
		authInfo.setEcm(false);
		profile.setProfileName("Saved Profile 2");
		profile.setAuthInfo(authInfo);
		profiles.add(profile);
		// END PLACEHOLDER

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setBackgroundResource(R.color.White);

		ArrayList<ProfileListRow> rows = new ArrayList<ProfileListRow>();

		for (Profile prof : profiles) {
			rows.add(new ProfileListRow(prof));
		}
		// Set the adapter for the list view
		mDrawerList.setAdapter(new ProfileAdapter(this, rows));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Save layout on rotation
		if (savedInstanceState == null) {
			// LocalLoginFragment frFragment = new LocalLoginFragment();
			SplashScreenFragment frFragment = new SplashScreenFragment();

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments
			// firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			transaction.replace(R.id.login_fragment, frFragment);
			transaction.commit();
		}

		mTitle = getTitle();
		mDrawerTitle = getResources().getString(R.string.saved_profiles_title);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_closed) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerToggle.onOptionsItemSelected(item)) {
				return true;
			}
			return false;
		case R.id.fr_debug: {
			Intent intent = new Intent(this, DebugActivity.class);
			intent.putExtra("create_new", false);
			startActivity(intent);
			return true;
		}
		case R.id.fr_debug_new: {
			Intent intent = new Intent(this, DebugActivity.class);
			intent.putExtra("create_new", true);
			startActivity(intent);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * public void setAuthInfo(AuthInfo authInfo) { // TODO Auto-generated
	 * method stub this.authInfo = authInfo; }
	 * 
	 * public AuthInfo getAuthInfo() { return authInfo; }
	 */

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		// setTitle(profilesArray[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
		startPIN();
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		MenuItem menuitem = menu.findItem(R.id.menu_switchtolocal);
		if (menuitem != null) {
			menuitem.setVisible(!drawerOpen);
		}

		menuitem = menu.findItem(R.id.menu_switchtoecm);
		if (menuitem != null) {
			menuitem.setVisible(!drawerOpen);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	// List adapter.
	public class ProfileAdapter extends ArrayAdapter<ProfileListRow> {
		private final Context context;
		private final ArrayList<ProfileListRow> rows;

		public ProfileAdapter(Context context, ArrayList<ProfileListRow> rows) {
			super(context, R.layout.listrow_profiles, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public ProfileListRow getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.listrow_profiles, parent,
					false);

			Profile profile = rows.get(position).getProfile();
			// Title text
			TextView title = (TextView) rowView
					.findViewById(R.id.profilerow_title);
			title.setText(profile.getProfileName());

			// Profile image
			ImageView profileIcon = (ImageView) rowView
					.findViewById(R.id.profilerow_image);

			if (profile.getAuthInfo().isEcm()) {
				// set ecm cloud icon
				profileIcon.setImageResource(R.drawable.ic_ecm_cloud_profile);
			} else {
				// We would add the local image here
				profileIcon.setImageResource(R.drawable.ic_direct_wire_profile);
			}

			return rowView;
		}
	}

	public void startPIN() {
		PINFragment PIN = PINFragment.newInstance(false);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.login_fragment, PIN, "PIN");
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void closePIN(String pin){
		//getSupportFragmentManager().popBackStack();

		Log.i(CommandCenterActivity.TAG, "Action bar should be showing.");
		getActionBar().show();
		
		if (pin != null){
			//pin was returned
			//Create the secret generator
			try {
			SecretKey secret = Cryptography.generateKey(pin,
					Cryptography.createLocalUUID(this).getBytes("UTF-8"));
			
			String authUser = authInfo.getUsername();
			String authPass = authInfo.getPassword();
			
			authUser = Cryptography.decryptMsg(Base64.decode(authUser, Base64.DEFAULT), secret);
			authPass = Cryptography.decryptMsg(Base64.decode(authPass, Base64.DEFAULT), secret);

			} catch (Exception e){
				Log.e(CommandCenterActivity.TAG, "An exception occured trying to decrypt the user credentials.");
			}
		}
	}
	
	@Override
	public void onBackPressed() {
	    final PINFragment fragment = (PINFragment) getSupportFragmentManager().findFragmentByTag("PIN");
	    Log.i(CommandCenterActivity.TAG, "BACK PRESSED");
	    if (fragment != null) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
			getActionBar().show();
	    }
        super.onBackPressed();

	}
}
