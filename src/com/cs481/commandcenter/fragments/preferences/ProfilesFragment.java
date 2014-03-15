package com.cs481.commandcenter.fragments.preferences;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs481.commandcenter.R;


/**
 * Preference Fragment for the ui preferences page.
 * THIS MUST RUN IN ITS OWN ACTIVITY AS IT DOES NOT WORK WITH THE SUPPORT FRAGMENT MANAGER...
 * @author Mgamerz
 *
 */
public class ProfilesFragment extends Fragment  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View v = inflater.inflate(R.layout.fragment_profileeditor, container, false);
    	return v;
    }
}