package com.cs481.mobilemapper;
/*
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;



//This is leftover code from the original implementation. It is not in use anymore but exists in the event it is needed.
public class Mapper extends FragmentActivity implements
		ActionBar.TabListener {

	public static final String TAG = "CommandCenter";

	private static int num = 0;
	
	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_mapping);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.firstrun_frag);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.tabMapping)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setText(R.string.tabSettings)
				.setTabListener(this));
		/*for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapping, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 *
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		


		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Log.i("com.cs481.mobilemapper","get item: "+position);

		    FragmentManager fm = getSupportFragmentManager();

			switch(position){
			//These are reversed for some reason. Don't understand why.
			case 1:
			    Fragment settingsFragment = fm.findFragmentByTag("settingsfrag");

			    // If the Fragment is non-null, then it is currently being
			    // retained across a configuration change.
			    if (settingsFragment == null) {
			    	
			      settingsFragment = new SettingsFragment();
			    //  fm.beginTransaction().add(settingsFragment, "settingsfrag").commit();
			    }
			    return settingsFragment;
			default:
			    Fragment mapFragment = fm.findFragmentByTag("mapfrag");

			    // If the Fragment is non-null, then it is currently being
			    // retained across a configuration change.
			    if (mapFragment == null) {
			    	
			      mapFragment = new SettingsFragment();
			    //  fm.beginTransaction().add(settingsFragment, "settingsfrag").commit();
			    }
				return new MappingFragment();
			}
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}
}*/