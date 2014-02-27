package com.cs481.mobilemapper.fragments;

import com.cs481.mobilemapper.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreenFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.fragment_splashscreen, container, false);
        ImageView ecm_button = (ImageView) view.findViewById(R.id.button_ecm);
        ImageView local_button = (ImageView) view.findViewById(R.id.button_local);
    	
        ecm_button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg) {
				ECMLoginFragment ecmLoginFragment = new ECMLoginFragment();
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.login_fragment, ecmLoginFragment);
				transaction.commit();
			}
        });
        
        local_button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg) {
				LocalLoginFragment localLoginFragment = new LocalLoginFragment();
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.login_fragment, localLoginFragment);
				transaction.commit();
			}
        });
        
    	return view;
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	//Should retreive the 'animation finished' boolean in a call before this (such as onCreateView()) so it doens't keep playing if the screen rotates.
    	animate();
    }
    
    // Called in LoginActivity
    public void animate(){
    	FrameLayout ecm = (FrameLayout) getView().findViewById(
				R.id.button_ecm_layout);
    	Animation shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.animation_shake);
		ecm.startAnimation(shakeAnimation);
    }
}
