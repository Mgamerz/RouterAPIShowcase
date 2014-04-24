package com.cs481.commandcenter.fragments;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cs481.commandcenter.R;

/**
 * Fragment that shows the PIN input pad and
 * has the logic for PIN pad entry
 * @author Mike Perez
 */

public class PINPadSubfragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		// setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.subfrag_pinpad, container, false);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		int padHeight = (int) ((height/10)*4.5); // 45% of screen should be pad
		
		Button pinButton = (Button) rootView.findViewById(R.id.pin1);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin2);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		
		pinButton = (Button) rootView.findViewById(R.id.pin3);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin4);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin5);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin6);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin7);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin8);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin9);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin_extra);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin0);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		pinButton = (Button) rootView.findViewById(R.id.pin_backspace);
		pinButton.setHeight(padHeight/4);
		pinButton.setTypeface(null,Typeface.BOLD);
		
		return rootView;
	}

}
