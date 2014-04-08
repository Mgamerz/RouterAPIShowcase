package com.cs481.commandcenter.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;
import com.mgamerzproductions.logthislib.LogCollector;


/**
 * This activity is for sending a bug report. It is forced into portrait mode so the user doesn't cause rotating issues. 
 */

public class BugReportActivity extends SpiceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(Utility.getTheme(this));

		setContentView(R.layout.activity_bugreport);
		setupListeners();
	}

	private void setupListeners() {
		Button takeBugReport = (Button) findViewById(R.id.bugreport_button);
		takeBugReport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogCollector collector = new LogCollector(getPackageName(),"3e06ecc889a1a816bbcb9fed03631b84", "9c1502c29c523d0514208708016488d3");
				collector.getLogs(BugReportActivity.this);
			}
		});
		
		Button googlePlusButton = (Button) findViewById(R.id.googleplus_button);
		googlePlusButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = getResources().getString(R.string.googlePlusURL);
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
	}
}
