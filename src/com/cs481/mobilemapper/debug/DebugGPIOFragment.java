package com.cs481.mobilemapper.debug;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import com.cs481.mobilemapper.CommandCenter;
import com.cs481.mobilemapper.R;

public class DebugGPIOFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_gpio, container,
				false);
		return rootView;
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
}
