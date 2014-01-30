package com.cs481.mobilemapper;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class CommandCenter extends SpiceActivity {

	public static final String TAG = "CommandCenter";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapping);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapping, menu);
		return true;
	}

	
	//inner class of your spiced Activity
	private class GetRequestListener implements RequestListener<POJO> {

	  @Override
	  public void onRequestFailure(SpiceException e) {
	    //update your UI
		  Log.i(TAG, "Command failure!");
	  }

	  @Override
	  public void onRequestSuccess(POJO returnvals) {
	    //update your UI
		  Log.i(TAG, "Command success!");
	  }
	}
}