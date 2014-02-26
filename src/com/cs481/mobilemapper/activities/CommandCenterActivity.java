package com.cs481.mobilemapper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.fragments.DashboardFragment;

public class CommandCenterActivity extends SpiceActivity {

	public static final String TAG = "CommandCenter";
	private boolean isDualPane;
	private AuthInfo authInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(Utility.getTheme(this));


		setContentView(R.layout.activity_commandcenter);

		Intent intent = getIntent();
		// Rebuild authInfo from the intent that put us here
		authInfo = new AuthInfo();
		authInfo.setEcm(intent.getBooleanExtra("ecm", false));
		authInfo.setRemote(intent.getBooleanExtra("remote", false));
		authInfo.setRouterport(intent.getIntExtra("port", 80));
		authInfo.setRouterId(intent.getStringExtra("id"));
		authInfo.setRouterip(intent.getStringExtra("ip"));
		authInfo.setUsername(intent.getStringExtra("user"));
		authInfo.setPassword(intent.getStringExtra("pass"));

		// Retain fragments on rotation
		if (null == savedInstanceState) {
			// set you initial fragment object

			// create first UI fragment and set it up
			Fragment fragment = DashboardFragment.newInstance(authInfo);

			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();

			// Inject our fragment
			ft.replace(R.id.leftside_fragment, fragment);
			// set type of animation

			// Commit to the UI
			ft.commit();
		}
		getActionBar().setSubtitle("--Cloud Router testing--");
		View dual = findViewById(R.id.rightside_fragment);
		setDualPane(dual != null && dual.getVisibility() == View.VISIBLE);

	}

	public AuthInfo getAuthInfo() {
		return authInfo;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.commandcenter_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.w(CommandCenterActivity.TAG, "Item was clicked.");
		// handle item selection
		switch (item.getItemId()) {
		case R.id.action_logout:
			Intent logoutIntent = new Intent(this, LoginActivity.class);
			Toast.makeText(this, "You have been logged out.",
					Toast.LENGTH_SHORT).show();
			startActivity(logoutIntent);
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			return true;
		case R.id.action_settings:
			Intent prefsIntent = new Intent(this, PrefsActivity.class);
			Toast.makeText(this, "You have been logged out.",
					Toast.LENGTH_SHORT).show();
			startActivity(prefsIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean isDualPane() {
		return isDualPane;
	}

	public void setDualPane(boolean isDualPane) {
		this.isDualPane = isDualPane;
	}
}