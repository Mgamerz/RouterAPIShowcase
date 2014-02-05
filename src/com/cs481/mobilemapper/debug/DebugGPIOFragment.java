package com.cs481.mobilemapper.debug;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
