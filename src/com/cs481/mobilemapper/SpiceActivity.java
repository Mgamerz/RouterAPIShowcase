package com.cs481.mobilemapper;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

//import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * The Spice Activity is a parent activity so we don't have to 'spice' every activity with our REST request setup.
 * @author Mgamerz
 */
public class SpiceActivity extends FragmentActivity {
	//------------------------------------------------------------------------
	//this block can be pushed up into a common base class for all activities
	//------------------------------------------------------------------------

	//if you use a pre-set service, 
	//use JacksonSpringAndroidSpiceService.class instead of JsonSpiceService.class
	protected SpiceManager spiceManager = new SpiceManager(SpiceService.class);


	@Override
	protected void onStart() {
	  super.onStart();
	  Log.i(CommandCenter.TAG, "Spice manager: "+spiceManager);
	  spiceManager.start(this);
	}

	@Override
	protected void onStop() {
	  spiceManager.shouldStop();
	  super.onStop();
	}

	//------------------------------------------------------------------------
	//---------end of block that can fit in a common base class for all activities
	//------------------------------------------------------------------------

	/*private void performRequest(String user) {
	  MainActivity.this.setProgressBarIndeterminateVisibility(true);

	  FollowersRequest request = new FollowersRequest(user);
	  lastRequestCacheKey = request.createCacheKey();

	  spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new ListFollowersRequestListener());
	}*/
}
