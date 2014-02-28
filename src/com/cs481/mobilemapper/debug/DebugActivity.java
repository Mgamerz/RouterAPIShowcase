package com.cs481.mobilemapper.debug;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.activities.SpiceActivity;

public class DebugActivity extends SpiceActivity {
	private AuthInfo authInfo;
	public boolean create_new = false;

	public AuthInfo getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(AuthInfo authInfo) {
		this.authInfo = authInfo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		create_new = intent.getBooleanExtra("create_new", true);
		setContentView(R.layout.activity_debug);
		Log.i(CommandCenterActivity.TAG, "Create new: "+create_new);
	}

	@Override
	public void onStart() {
		super.onStart();
	}
}
