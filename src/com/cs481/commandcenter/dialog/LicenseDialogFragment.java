/**
 * Copyright 2013 Adam Speakman

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.cs481.commandcenter.dialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
// TODO If you don't support Android 2.x, you should use the non-support version!
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.cs481.commandcenter.R;


/**
 * Created by Adam Speakman on 24/09/13.
 * http://speakman.net.nz
 */
public class LicenseDialogFragment extends DialogFragment {

    private AsyncTask<Void, Void, String> mLicenseLoader;
    private int rawresource;
    
    private static final String FRAGMENT_TAG = "com.cs481.commandcenter.dialog.LicenseDialogFragment";

    public static LicenseDialogFragment newInstance(int rawresource) {
    	LicenseDialogFragment ldf = new LicenseDialogFragment();
    	ldf.setLicenseResource(rawresource);
        return ldf;
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			rawresource = savedInstanceState.getInt("rawresource");
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("rawresource", rawresource);
	}

    private void setLicenseResource(int rawresource) {
		// TODO Auto-generated method stub
		this.rawresource = rawresource;
	}

    
    /**
     * Builds and displays a licenses fragment for you.
     * @param fm Fragment manager to use to show the dialog
     * @param rawresource Resource to display. Should be under /res/raw/<html file>
     */
    public static void displayLicenseDialogFragment(FragmentManager fm, int rawresource) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag(FRAGMENT_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = LicenseDialogFragment.newInstance(rawresource);
        newFragment.show(ft, FRAGMENT_TAG);
    }

    private WebView mWebView;
    private ProgressBar mIndeterminateProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Extract this title out into your strings resource file.
        getDialog().setTitle(R.string.open_source_title);
        View view = inflater.inflate(R.layout.licenses_fragment, container, false);
        mIndeterminateProgress = (ProgressBar)view.findViewById(R.id.licensesFragmentIndeterminateProgress);
        mWebView = (WebView)view.findViewById(R.id.licensesFragmentWebView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadLicenses();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLicenseLoader != null) {
            mLicenseLoader.cancel(true);
        }
    }

    private void loadLicenses() {
        // Load asynchronously in case of a very large file.
        mLicenseLoader = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                InputStream rawResource = getActivity().getResources().openRawResource(rawresource);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(rawResource));

                String line;
                StringBuilder sb = new StringBuilder();

                try {
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    // TODO You may want to include some logging here.
                }

                return sb.toString();
            }

            @Override
            protected void onPostExecute(String licensesBody) {
                super.onPostExecute(licensesBody);
                if (getActivity() == null || isCancelled()) return;
                mIndeterminateProgress.setVisibility(View.INVISIBLE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadDataWithBaseURL(null, licensesBody, "text/html", "utf-8", null);
                mLicenseLoader = null;
            }

        }.execute();
    }
}
