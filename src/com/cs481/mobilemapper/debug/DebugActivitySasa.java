package com.cs481.mobilemapper.debug;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;
import com.cs481.mobilemapper.responses.ecm.routers.Router;
import com.cs481.mobilemapper.responses.ecm.routers.Routers;

public class DebugActivitySasa extends SpiceActivity {
	private Routers routers; //used if ECM login is called
	private Router router;
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
		setContentView(R.layout.activity_debug_sasa);
		Log.i(CommandCenterActivity.TAG, "Create new: "+create_new);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public Routers getRouters() {
		return routers;
	}

	public void setRouters(Routers routers) {
		this.routers = routers;
	}

	public void setRouter(Router router) {
		// TODO Auto-generated method stub
		this.router = router;
	}

	public Router getRouter() {
		return router;
	}
}
