package com.cs481.mobilemapper.debug;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.fragments.ECMLoginFragment;

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
		//create first UI fragment and set it up   
		Fragment fragment = new ECMLoginFragment();

		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

		//Inject our fragment
		ft.replace(R.id.debug_container,fragment);
		//set type of animation

		//Commit to the UI
		ft.commit();
	}
}
