package com.cs481.mobilemapper.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.dialog.PreferredConnectionDialog;

/**
 * This is the first screen the user sees.
 * 
 * @author: Sasa Rkman
 */

public class SplashScreenFragment extends Fragment {

	// Has the fragment animated?
	boolean hasAnimated = false;

	ImageView ecm_button;
	ImageView ecm_image;
	ImageView local_button;
	ImageView local_image;

	TextView splash_text;
	TextView[] indicator = new TextView[6];

	Animation zoomAnim;
	Animation fadeInAnim;
	Animation fadeInAndOutAnim;

/*	TranslateAnimation moveUp;
	TranslateAnimation moveDown;*/
	Animation moveUp;
	Animation moveDown;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_splashscreen, container, false);

		ecm_button = (ImageView) view.findViewById(R.id.button_ecm);
		local_button = (ImageView) view.findViewById(R.id.button_local);

		ecm_image = (ImageView) view.findViewById(R.id.image_ecm);
		local_image = (ImageView) view.findViewById(R.id.image_local);

		// Used only in animate()
		splash_text = (TextView) view.findViewById(R.id.text_splash);

		// The ^'s and v's
		indicator[0] = (TextView) view.findViewById(R.id.up1);
		indicator[1] = (TextView) view.findViewById(R.id.up2);
		indicator[2] = (TextView) view.findViewById(R.id.up3);
		indicator[3] = (TextView) view.findViewById(R.id.down1);
		indicator[4] = (TextView) view.findViewById(R.id.down2);
		indicator[5] = (TextView) view.findViewById(R.id.down3);

		// Used for retrieving user preferences
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

		ecm_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg) {
				// Display the preferred connection dialog
				if (!prefs.getBoolean("prefs_connection_dontAskAgain", false)) {
					PreferredConnectionDialog rcFragment = PreferredConnectionDialog.newInstance("ECM");
					rcFragment.show(getActivity().getSupportFragmentManager(),"PreferredConnection");
				}

				// Load the ECM login fragment
				ECMLoginFragment ecmLoginFragment = new ECMLoginFragment();
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.login_fragment, ecmLoginFragment);
				transaction.commit();
			}
		});

		local_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg) {
				// Display the preferred connection dialog
				if (!prefs.getBoolean("prefs_connection_dontAskAgain", false)) {
					PreferredConnectionDialog rcFragment = PreferredConnectionDialog.newInstance("Local Router");
					rcFragment.show(getActivity().getSupportFragmentManager(),"PreferredConnection");
				}

				// Load the local login fragment
				LocalLoginFragment localLoginFragment = new LocalLoginFragment();
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.login_fragment, localLoginFragment);
				transaction.commit();
			}
		});

		// If an instance was saved, then fragment MUST have animated
		if (savedInstanceState != null) {
			hasAnimated = true;
		}

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		animate();
	}

	// Animates the fragment
	public void animate() {
		zoomAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
		fadeInAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
		fadeInAndOutAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in_and_out);
		moveUp = AnimationUtils.loadAnimation(getActivity(), R.anim.move_up); 
		moveDown = AnimationUtils.loadAnimation(getActivity(), R.anim.move_down); 

		// This makes the animation permanent after initial rotation
		if (hasAnimated) {
			zoomAnim.setDuration(0);
			moveUp.setDuration(0);
			moveDown.setDuration(0);
			fadeInAnim.setDuration(0);
			fadeInAndOutAnim.setDuration(0);
		}

		// Create a set of animations for the two buttons
		final AnimationSet ecm_anim = new AnimationSet(false); // Zoom in and move up
		final AnimationSet local_anim = new AnimationSet(false); // Zoom in and move
															// down

		// Makes effects of animation NOT reset
		ecm_anim.setFillEnabled(true);
		ecm_anim.setFillAfter(true);
		local_anim.setFillEnabled(true);
		local_anim.setFillAfter(true);

		ecm_anim.addAnimation(zoomAnim);
		ecm_anim.addAnimation(moveUp);
		local_anim.addAnimation(zoomAnim);
		local_anim.addAnimation(moveDown);

		// Start animating the buttons
		if(!hasAnimated) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					ecm_image.startAnimation(ecm_anim);
					local_image.startAnimation(local_anim);
				}
			}, 1000);
	
			// Start animating the text
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					splash_text.startAnimation(fadeInAnim);
				}
			}, 2000);
			
			// Start animating the indicators
			int interval = 1; // seconds
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					indicator[0].startAnimation(fadeInAndOutAnim);
				}
			}, interval * 3 * 1000);
			
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					indicator[1].startAnimation(fadeInAndOutAnim);
				}
			}, interval * 4 * 1000);
			
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					indicator[2].startAnimation(fadeInAndOutAnim);
				}
			}, interval * 5 * 1000);
			
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					indicator[3].startAnimation(fadeInAndOutAnim);
				}
			}, interval * 3 * 1000);
			
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					indicator[4].startAnimation(fadeInAndOutAnim);
				}
			}, interval * 4 * 1000);
			
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					indicator[5].startAnimation(fadeInAndOutAnim);
				}
			}, interval * 5 * 1000);
		}
		else {
			ecm_image.startAnimation(ecm_anim);
			local_image.startAnimation(local_anim);
			splash_text.startAnimation(fadeInAnim);
		}

	}
}