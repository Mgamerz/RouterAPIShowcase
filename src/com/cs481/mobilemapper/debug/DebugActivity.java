package com.cs481.mobilemapper.debug;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.SpiceActivity;
import com.cs481.mobilemapper.responses.ecm.routers.Routers;

public class DebugActivity extends SpiceActivity {
	private Routers routers; //used if ECM login is called
	private String password, ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);

	}

	@Override
	public void onStart() {
		super.onStart();
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		password = extras.getString("password");
		ip = extras.getString("ip");

		TextView header = (TextView) findViewById(R.id.debug_header);
		header.setText(ip + " - " + password);
	}

	public Routers getRouters() {
		return routers;
	}

	public void setRouters(Routers routers) {
		this.routers = routers;
	}
}
