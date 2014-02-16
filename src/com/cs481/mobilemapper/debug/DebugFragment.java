package com.cs481.mobilemapper.debug;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.fragments.PINFragment;

public class DebugFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_debug, container,
				false);
		
		DebugActivity da = (DebugActivity) getActivity();
		boolean create = da.create_new;
		Log.i(CommandCenterActivity.TAG, "DF CR: "+create);
		
		if (savedInstanceState == null) {
			Fragment fragment = PINFragment.newInstance(create); //used to pass arguments to a fragment
			FragmentTransaction ft = getChildFragmentManager()
					.beginTransaction();

			// Inject our fragment
			ft.replace(R.id.debug_container, fragment);
			// set type of animation

			// Commit to the UI
			ft.commit();
		}
		return rootView;
	}

}
