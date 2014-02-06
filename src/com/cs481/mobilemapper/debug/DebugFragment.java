package com.cs481.mobilemapper.debug;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.cs481.mobilemapper.R;

public class DebugFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstancedState){
		super.onCreate(savedInstancedState);
		setHasOptionsMenu (true);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_debug, container,
				false);
		return rootView;
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
}
