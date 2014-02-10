package com.cs481.mobilemapper;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cs481.mobilemapper.fragments.LocalLoginFragment;
import com.cs481.mobilemapper.responses.ecm.routers.Routers;

public class LoginActivity extends SpiceActivity {
	Routers routers; // used if ECM login is called
	private AuthInfo authInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//Save layout on rotation
		if (savedInstanceState == null) {
			LocalLoginFragment frFragment = new LocalLoginFragment();

			// In case this activity was started with special instructions from
			// an
			// Intent, pass the Intent's extras to the fragment as arguments
			// firstFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			transaction.replace(R.id.login_fragment, frFragment);
			transaction.commit();
		}

	}

	public void setRouters(Routers routers) {
		// TODO Auto-generated method stub
		this.routers = routers;
	}

	public Routers getRouters() {
		return routers;
	}

	public void setAuthInfo(AuthInfo authInfo) {
		// TODO Auto-generated method stub
		this.authInfo = authInfo;
	}

	public AuthInfo getAuthInfo() {
		return authInfo;
	}
}
