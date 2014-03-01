package com.cs481.mobilemapper.fragments;

import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.dialog.PreferredConnectionDialog;
import com.cs481.mobilemapper.dialog.RouterConfirmDialogFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This is the first screen the user sees.
 * @author: Sasa Rkman
 */

public class SplashScreenFragment extends Fragment {
	
	// Has the fragment animated?
	boolean hasAnimated = false;
	
	ImageView ecm_button;
    ImageView local_button;
    TextView ecm_text;
    TextView local_text;
    
	Animation zoomAnim;
	Animation fadeAnim;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.fragment_splashscreen, container, false);
    	
        ecm_button = (ImageView) view.findViewById(R.id.button_ecm);
        local_button = (ImageView) view.findViewById(R.id.button_local);
        
        // Used only in animate()
        ecm_text = (TextView) view.findViewById(R.id.text_ecm);
        local_text = (TextView) view.findViewById(R.id.text_local);
    	
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        
        ecm_button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg) {
				// Display the preferred connection dialog
				if(!prefs.getBoolean("prefs_connection_dontAskAgain", false)) {
					PreferredConnectionDialog rcFragment = PreferredConnectionDialog.newInstance("ECM");
					rcFragment.show(getActivity().getSupportFragmentManager(), "PreferredConnection");	
				}
				
				// Load the ECM login fragment
				ECMLoginFragment ecmLoginFragment = new ECMLoginFragment();
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.login_fragment, ecmLoginFragment);
				transaction.commit();
			}
        });
        
        local_button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg) {
				// Display the preferred connection dialog
				if(!prefs.getBoolean("prefs_connection_dontAskAgain", false)) {
					PreferredConnectionDialog rcFragment = PreferredConnectionDialog.newInstance("Local Router");
					rcFragment.show(getActivity().getSupportFragmentManager(), "PreferredConnection");	
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
    public void onStart(){
    	super.onStart();
    	animate();
    }
    
    // Animates the fragment
    public void animate(){
    	zoomAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_in);
    	fadeAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in);
    	
    	// This makes the fill effects of the animation permanent after initial rotation
    	if(hasAnimated) {
    		zoomAnim.setDuration(0);
    		fadeAnim.setDuration(0);
    	}
    	
    	// Start zoom in on the cloud and city images
		ecm_button.startAnimation(zoomAnim);
		local_button.startAnimation(zoomAnim);
		
		// Start fade in on the ECM and Local texts
		ecm_text.startAnimation(fadeAnim);
		local_text.startAnimation(fadeAnim);
    }
}
