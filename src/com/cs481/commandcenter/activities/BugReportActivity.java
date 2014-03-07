package com.cs481.commandcenter.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;


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
				// TODO Auto-generated method stub
				//LogCollector collector = new LogCollector();
				//collector.getLogs(BugReportActivity.this);
			}
		});
	}
}
