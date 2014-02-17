package com.cs481.mobilemapper.fragments;

import com.cs481.mobilemapper.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SplashScreenFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.fragment_splashscreen, container, false);
        Button ecm_button = (Button) view.findViewById(R.id.button_ecm);
        Button local_button = (Button) view.findViewById(R.id.button_local);
    	
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
}
