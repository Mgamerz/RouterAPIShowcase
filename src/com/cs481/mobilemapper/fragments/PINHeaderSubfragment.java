package com.cs481.mobilemapper.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs481.mobilemapper.R;

public class PINHeaderSubfragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		// setHasOptionsMenu(true);
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

		return rootView;
	}
}
