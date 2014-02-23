package com.cs481.mobilemapper;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import roboguice.util.temp.Ln;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cs481.mobilemapper.debug.DebugActivity;
import com.cs481.mobilemapper.debug.DebugActivitySasa;
import com.cs481.mobilemapper.fragments.LocalLoginFragment;
import com.cs481.mobilemapper.fragments.SplashScreenFragment;
import com.cs481.mobilemapper.responses.ecm.routers.Routers;

public class LoginActivity extends SpiceActivity {
	Routers routers; // used if ECM login is called
	private AuthInfo authInfo;
	private String[] profilesArray;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Ln.getConfig().setLoggingLevel(Log.ERROR);

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

		profilesArray = getResources().getStringArray(R.array.profiles_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_profile, profilesArray));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Save layout on rotation
		if (savedInstanceState == null) {
			//LocalLoginFragment frFragment = new LocalLoginFragment();
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

		mTitle = mDrawerTitle = getTitle();
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
		case R.id.fr_debug_sasa: {
			Intent intent = new Intent(this, DebugActivitySasa.class);
			//intent.putExtra("create_new", false);
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

	public void setRouters(Routers routers) {
		// TODO Auto-generated method stub
		this.routers = routers;
	}

	public Routers getRouters() {
		return routers;
	}

	public void setAuthInfo(AuthInfo authInfo) {
		// TODO Auto-generated method stub
		this.authInfo = authInfo;
	}

	public AuthInfo getAuthInfo() {
		return authInfo;
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
		// Create a new fragment and specify the planet to show based on
		// position
		/*
		 * Fragment fragment = new PlanetFragment(); Bundle args = new Bundle();
		 * args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		 * fragment.setArguments(args);
		 * 
		 * // Insert the fragment by replacing any existing fragment
		 * FragmentManager fragmentManager = getFragmentManager();
		 * fragmentManager.beginTransaction() .replace(R.id.content_frame,
		 * fragment).commit();
		 */
		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(profilesArray[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
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
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        MenuItem menuitem = menu.findItem(R.id.menu_switchtolocal);
        if (menuitem != null){
        	menuitem.setVisible(!drawerOpen);
        }
        
        menuitem = menu.findItem(R.id.menu_switchtoecm);
        if (menuitem != null){
        	menuitem.setVisible(!drawerOpen);
        }
        
        return super.onPrepareOptionsMenu(menu);
    }
	
	
}
