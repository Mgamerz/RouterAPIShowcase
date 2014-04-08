package com.cs481.commandcenter.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs481.commandcenter.R;

/**
 * Fragment that shows information on how to use
 * the PIN creator and also shows the status
 * of PIN entry
 * @author Mike Perez
 */

public class PINHeaderSubfragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		// setHasOptionsMenu(true);
	}
	
	public static PINHeaderSubfragment newInstance(String header) {
		PINHeaderSubfragment pinFragment = new PINHeaderSubfragment();

		Bundle args = new Bundle();
		//Log.i(CommandCenterActivity.TAG, "CP: "+createPIN);
		args.putString("headertext", header);
		pinFragment.setArguments(args);
		return pinFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.subfrag_pinheader, container,
				false);
		TextView enterText = (TextView) rootView
				.findViewById(R.id.enterpin_text);

		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) enterText
				.getLayoutParams();
		
		
		//set the headers margin based on how tall the current screen height is - it can change across devices and orientations.
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int height = size.y;
		
		int margin = (int) ((height/10)*0.8); //8%
		
		params.setMargins(0, margin, 0, 0);
		enterText.setLayoutParams(params);
		if (savedInstanceState != null){
			enterText.setText(savedInstanceState.getString("titletext"));
		}

		return rootView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		TextView enterText = (TextView) getView()
				.findViewById(R.id.enterpin_text);
		
		outState.putString("titletext", enterText.getText().toString());
	}
}
