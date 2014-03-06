package com.cs481.commandcenter.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cs481.commandcenter.AuthInfo;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.cs481.commandcenter.fragments.DashboardFragment;
import com.cs481.commandcenter.fragments.WifiClientFragment;

public class CommandCenterActivity extends SpiceActivity implements
		OnBackStackChangedListener {

	public static final String TAG = "CommandCenter";
	private boolean isDualPane;
	private AuthInfo authInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(Utility.getTheme(this));

		setContentView(R.layout.activity_commandcenter);
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		Intent intent = getIntent();
		// Rebuild authInfo from the intent that put us here
		authInfo = intent.getParcelableExtra("authInfo");
		getActionBar().setSubtitle(intent.getStringExtra("ab_subtitle"));

		// Retain fragments on rotation
		if (null == savedInstanceState) {
			// set you initial fragment object

			// create first UI fragment and set it up
			Fragment fragment = DashboardFragment.newInstance(authInfo);

			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();

			// Inject our fragment
			ft.replace(R.id.leftside_fragment, fragment, fragment.getClass().getName());
			// set type of animation

			// Commit to the UI
			ft.commit();
		}
		// getActionBar().setSubtitle("--Cloud Router testing--");
		View dual = findViewById(R.id.rightside_fragment);
		setDualPane(dual != null);

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
			Toast.makeText(this, getResources().getString(R.string.logged_out),
					Toast.LENGTH_SHORT).show();
			startActivity(logoutIntent);
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			return true;
		case R.id.action_settings:
			Intent prefsIntent = new Intent(this, PrefsActivity.class);
			startActivity(prefsIntent);
			return true;
		case R.id.about:
			String url = getResources().getString(R.string.googlePlusURL);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
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

	@Override
	public void onBackStackChanged() {
		// This method is called when the backstack changes.
		Log.i(TAG, "Backstack has changed.");
		
		int backStackEntryCount = getSupportFragmentManager()
				.getBackStackEntryCount();
		if (backStackEntryCount > 0) {
			FragmentManager fm = getSupportFragmentManager();
			Fragment wcf = fm.findFragmentByTag(WifiClientFragment.class.getName());
			
			if (wcf == null){
				//its not showing
				getActionBar().setDisplayShowTitleEnabled(true);
			}
					
			if (!isDualPane) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		} else {
			DashboardFragment db = (DashboardFragment) getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getName());
			// this shouldn't be null... hopefully
			db.setCurrentSelection(-1);
			db.getListView().invalidateViews(); //redraw
			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}
}
