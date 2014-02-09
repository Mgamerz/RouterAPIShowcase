 package com.cs481.mobilemapper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cs481.mobilemapper.responses.ecm.routers.Routers;

public class FirstRunActivity extends SpiceActivity {
	Routers routers; //used if ECM login is called
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firstrun);
	}

	public void defaultGatewayChange(View v) {
		CheckBox cb = (CheckBox) v;
		Log.i(CommandCenter.TAG, "Default gateway checkbox");
		EditText customip = (EditText) findViewById(R.id.router_ip);
		if (!cb.isChecked()){
			customip.setVisibility(EditText.VISIBLE);
		} else {
			customip.setVisibility(EditText.GONE);
		}
	}
}
