package com.cs481.mobilemapper;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import roboguice.util.temp.Ln;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import com.cs481.mobilemapper.debug.DebugActivity;
import com.cs481.mobilemapper.fragments.LocalLoginFragment;
import com.cs481.mobilemapper.responses.ecm.routers.Routers;

public class LoginActivity extends SpiceActivity {
	Routers routers; // used if ECM login is called
	private AuthInfo authInfo;

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

		// Save layout on rotation
		if (savedInstanceState == null) {
			LocalLoginFragment frFragment = new LocalLoginFragment();

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

	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

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
}
