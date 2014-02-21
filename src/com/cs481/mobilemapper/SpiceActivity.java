package com.cs481.mobilemapper;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.octo.android.robospice.SpiceManager;


/**
 * The Spice Activity is a parent activity so we don't have to 'spice' every activity with our REST request setup.
 * @author Mgamerz
 */
public class SpiceActivity extends FragmentActivity {
	//use JacksonSpringAndroidSpiceService.class instead of JsonSpiceService.class
	protected SpiceManager spiceManager = new SpiceManager(CCSpiceService.class);


	@Override
	protected void onStart() {
	  super.onStart();
	  Log.i(CommandCenterActivity.TAG, "Spice manager: "+spiceManager);
	  spiceManager.start(this);
	}

	@Override
	protected void onStop() {
	  spiceManager.shouldStop();
	  super.onStop();
	}
	
	/**
	 * As the parent activites of most fragments we are going to use, we should have a way to share a spice manager with the children fragments.
	 * This method allows fragments to find their parents spicemanager.
	 * @return
	 */
	public SpiceManager getSpiceManager(){
		return spiceManager;
	}
}
