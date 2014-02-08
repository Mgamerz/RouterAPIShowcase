package com.cs481.mobilemapper;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.cs481.mobilemapper.fragments.DashboardFragment;

public class CommandCenter extends SpiceActivity{

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
		ft.replace(R.id.leftside_fragment,fragment);
		//set type of animation
	

		//finish transaction
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.commandcenter_menu, menu);

	    // Associate searchable configuration with the SearchView
	    SearchManager searchManager =
	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView =
	            (SearchView) menu.findItem(R.id.search).getActionView();
	    searchView.setSearchableInfo(
	            searchManager.getSearchableInfo(getComponentName()));
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.w(CommandCenter.TAG, "Item was clicked.");
	   // handle item selection
	   switch (item.getItemId()) {
	      case R.id.action_logout:
	    	  Intent intent = new Intent(this, FirstRunActivity.class);
	    	  Toast.makeText(this, "You have been logged out.", Toast.LENGTH_SHORT).show();
	    	  startActivity(intent);
	    	  finish();
	    	  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	         return true;
	      default:
	         return super.onOptionsItemSelected(item);
	   }
	}
}