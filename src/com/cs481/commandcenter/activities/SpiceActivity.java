package com.cs481.commandcenter.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cs481.commandcenter.CCSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * The Spice Activity is a parent activity so we don't have to 'spice' every
 * activity with our REST request setup.
 * 
 * @author Mike Perez
 */
public abstract class SpiceActivity extends FragmentActivity {
	// use JacksonSpringAndroidSpiceService.class instead of
	// JsonSpiceService.class
	protected SpiceManager spiceManager = new SpiceManager(CCSpiceService.class);
	public final static long DURATION_3SECS = 3000L;


	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
	}

	/**
	 * As the parent activites of most fragments we are going to use, we should
	 * have a way to share a spice manager with the children fragments. This
	 * method allows fragments to find their parents spicemanager.
	 * 
	 * @return Activity spicemanager
	 */
	public SpiceManager getSpiceManager() {
		return spiceManager;
	}

}
