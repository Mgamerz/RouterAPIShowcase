package com.cs481.mobilemapper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class CommandCenter extends SpiceActivity {

	public static final String TAG = "CommandCenter";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commandcenter);
		
		Bundle args = new Bundle();        
		// add needed args

		//create fragment and set arguments    
		Fragment fragment = new DashboardFragment();
		fragment.setArguments(args);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// getSupportFragmentManager - uses for compatible library instead of getFragmentManager

		//replace frame with our fragment
		ft.replace(R.id.overview_fragment,fragment);
		//set type of animation
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		//finish transaction
		ft.commit();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapping, menu);
		return true;
	}
}