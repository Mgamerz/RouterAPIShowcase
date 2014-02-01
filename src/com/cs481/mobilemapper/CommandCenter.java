package com.cs481.mobilemapper;

import android.os.Bundle;
import android.view.Menu;

public class CommandCenter extends SpiceActivity {

	public static final String TAG = "CommandCenter";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commandcenter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mapping, menu);
		return true;
	}
}