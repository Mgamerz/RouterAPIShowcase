package com.cs481.mobilemapper.activities;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import roboguice.util.temp.Ln;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
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
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.Cryptography;
import com.cs481.mobilemapper.Profile;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.debug.DebugActivity;
import com.cs481.mobilemapper.fragments.PINFragment;
import com.cs481.mobilemapper.fragments.SplashScreenFragment;
import com.cs481.mobilemapper.responses.GetRequest;
import com.cs481.mobilemapper.responses.Response;
import com.cs481.mobilemapper.responses.status.product_info.Product_info;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class LoginActivity extends SpiceActivity {
	private AuthInfo authInfo;
	private ArrayList<Profile> profiles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private ProfileAdapter adapter;
	private ActionBarDrawerToggle mDrawerToggle;
	private Profile unlockProfile;
	private ProgressDialog progressDialog;

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
		profiles = Utility.getProfiles();

		// PLACEHOLDER STUFF until melissa gets the DB up
		profiles = new ArrayList<Profile>();
		Profile profile = new Profile();
		AuthInfo authInfo = new AuthInfo();
		authInfo.setEcm(true);
		authInfo.setPassword("CradeDVS554$");
		authInfo.setUsername("mperez");
		authInfo.setRouterId("25716");
		profile.setProfileName("Cloud Router");
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
		mDrawerList.setBackgroundResource(R.color.ActionbarWhite);

		// Set the adapter for the list view
		adapter = new ProfileAdapter(this, profiles);
		mDrawerList.setAdapter(adapter);
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
			// frFragment.animate();
			transaction.commit();

			// CRASH HERE
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
		case R.id.action_settings:
			Intent prefsIntent = new Intent(this, PrefsActivity.class);
			startActivity(prefsIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

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
		Profile profile = profiles.get(position);
		startPINDecrypt(profile);
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
	public class ProfileAdapter extends ArrayAdapter<Profile> {
		private final Context context;
		private final ArrayList<Profile> rows;

		public ProfileAdapter(Context context, ArrayList<Profile> rows) {
			super(context, R.layout.listrow_profiles, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public Profile getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.listrow_profiles, parent,
					false);

			Profile profile = rows.get(position);
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

	/**
	 * Creates a new PIN that allows you to attempt to decrypt a profile.
	 * 
	 * @param profile
	 */
	public void startPINDecrypt(Profile profile) {
		mTitle = getActionBar().getTitle();
		Log.i(CommandCenterActivity.TAG, "Title is: " + mTitle);
		PINFragment PIN = PINFragment.newInstance(false);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.login_fragment, PIN, "PIN");
		transaction.addToBackStack(null);
		transaction.commit();
		unlockProfile = profile; // unlocking this profile once complete.
	}

	/**
	 * Sets up the interface for creating a NEW PIN. If the old one exists this
	 * will prevent them from being decrypted.
	 */
	public void startPINCreate() {
		mTitle = getActionBar().getTitle();
		PINFragment PIN = PINFragment.newInstance(true);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		transaction.replace(R.id.login_fragment, PIN, "PIN");
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void onPINResult(String pin) {
		getActionBar().show();
		getActionBar().setTitle(mTitle);
		
		if (pin == null) {
			// denied.
			return;
		}

		getSupportFragmentManager().popBackStack(); // get rid of the pin
													// fragment

		// login
		try {
			AuthInfo profileAuth = unlockProfile.getAuthInfo();
			String uuid = Cryptography.createLocalUUID(this);
			SecretKey secret = Cryptography.generateKey(pin,
					uuid.getBytes("UTF-8"));

			profileAuth.setUsername(Cryptography.decryptMsg(
					Base64.decode(profileAuth.getUsername(), Base64.DEFAULT),
					secret));
			profileAuth.setPassword(Cryptography.decryptMsg(
					Base64.decode(profileAuth.getPassword(), Base64.DEFAULT),
					secret));

			// connection goes here.

			// Login via ECM.
			if (profileAuth.isEcm()) {
				Intent intent = new Intent(this, CommandCenterActivity.class);
				intent.putExtra("authInfo", authInfo);
				intent.putExtra("ab_subtitle", unlockProfile.getProfileName()); // changes
																				// subtitle.
				startActivity(intent);
				finish();
				return; // makes sure nothing else happens in this method if it
						// tries to continue execution.
			} else {
				// It's a direct login
				// Perform a credential login before we load up the management interface.

				GetRequest request = new GetRequest(authInfo, "status/product_info",
						Product_info.class, "direct_login");
				String lastRequestCacheKey = request.createCacheKey();

				progressDialog = new ProgressDialog(this,
						R.style.DialogTheme);
				progressDialog.setMessage(getResources().getString(
						R.string.connecting));
				progressDialog.show();
				progressDialog.setCanceledOnTouchOutside(false);
				
				//fire off the request.
				spiceManager.execute(request, lastRequestCacheKey,
						DurationInMillis.ALWAYS_EXPIRED, new LoginGetRequestListener());
			}

		} catch (Exception e) {
			// failed to decrypt due to 1-million possible errors.
			Log.e(CommandCenterActivity.TAG,
					"The supplied PIN was unable to decrypt the username and/or password, logging in has been cancelled.");
		}
	}

	private class LoginGetRequestListener implements RequestListener<Response> {

		@Override
		public void onRequestFailure(SpiceException e) {
			// update your UI
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			Log.i(CommandCenterActivity.TAG, "Failed to log in!");
			Toast.makeText(LoginActivity.this,
					getResources().getString(R.string.direct_login_fail),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(Response response) {
			Product_info proin = (Product_info) response.getData();

			// update your UI
			if (progressDialog != null)
				progressDialog.dismiss(); // update your UI
			if (response.getResponseInfo() != null) {
				if (response.getResponseInfo().getSuccess()) {
					// login successful
					// Prepare new intent.
					Intent intent = new Intent(LoginActivity.this,
							CommandCenterActivity.class);
					intent.putExtra("authInfo", authInfo);
					intent.putExtra("ab_subtitle", proin.getProduct_name()); // changes
																				// subtitle.
					// start the new activity, and prevent this one from being
					// returned to unless logout is chosen.
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(LoginActivity.this,
							response.getResponseInfo().getReason(),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(
						LoginActivity.this,
						getResources().getString(
								R.string.gpio_get_null_response),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		final PINFragment fragment = (PINFragment) getSupportFragmentManager()
				.findFragmentByTag("PIN");
		if (fragment != null) { // and then you define a method allowBackPressed
								// with the logic to allow back pressed or not
			Log.i(CommandCenterActivity.TAG, "Back - restoring title to "
					+ mTitle);
			getActionBar().setTitle(mTitle);
			getActionBar().show();
		}
		super.onBackPressed();

	}
}
