package com.cs481.mobilemapper.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.Profile;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.activities.CommandCenterActivity;
import com.cs481.mobilemapper.responses.ecm.routers.Router;

public class RouterConfirmDialogFragment extends DialogFragment {
	private Router router;
	private AuthInfo authInfo;

	// Context context;

	/**
	 * This constructor must be empty or the Fragment won't be able to start.
	 */
	public RouterConfirmDialogFragment() {
		Log.i(CommandCenterActivity.TAG, "Created fragment.");
		// context = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			router = savedInstanceState.getParcelable("router");
			authInfo = savedInstanceState.getParcelable("authInfo");
		}
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        HoloDialogBuilder  alertDialogBuilder = new HoloDialogBuilder(getActivity());
        alertDialogBuilder.setTitle(router.getName());
        Resources resources = getResources();
        
        // THIS MAY NEED TO GET CHANGED (following two lines):
        alertDialogBuilder.setTitleColor(resources.getString(R.color.CradlepointRed));
        alertDialogBuilder.setDividerColor(resources.getString(R.color.CradlepointRed));
        
        LayoutInflater inflator = getActivity().getLayoutInflater();
        final View v = inflator.inflate(R.layout.dialog_routerconfirm, null);
        TextView tv = (TextView) v.findViewById(R.id.rcdialog_text);
        String text = tv.getText().toString();
        text = String.format(text, router.getName());
        tv.setText(text);
        
        alertDialogBuilder.setCustomView(v);
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Check to see if the 'save profile' is selected.
				CheckBox sp = (CheckBox) v.findViewById(R.id.ecm_save_as_profile);
				if (sp.isChecked()){
					Profile profile = new Profile();
					AuthInfo encryptedAuthInfo = Utility.encryptAuthInfo(getActivity(), authInfo);
					
					profile.setAuthInfo(authInfo);
					
					
					
					profile.setProfileName(router.getName());
					
					Utility.saveProfile(getActivity(), profile);
				}
				
				Intent intent = new Intent(getActivity(), CommandCenterActivity.class);
				/*intent.putExtra("ip", "");
				intent.putExtra("pass", authInfo.getPassword());
				intent.putExtra("ecm", true);
				intent.putExtra("id", router.getId());
				intent.putExtra("user", authInfo.getUsername());*/
				intent.putExtra("authInfo", authInfo);
				intent.putExtra("ab_subtitle", router.getName()); //changes subtitle.
				startActivity(intent);
				getActivity().finish();
			}
		});
        
        alertDialogBuilder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return alertDialogBuilder.create();
    }

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("router", router);
		outState.putParcelable("authInfo", authInfo);
	}

	/**
	 * Sets the authorization information and the router information, allowing
	 * this dialog to navigate to the CommandCenter activity with all required
	 * information.
	 * 
	 * @param router
	 *            Router object containing this router information
	 * @param authInfo
	 *            Auth information allowing the user to edit this router
	 */
	public void setData(Router router, AuthInfo authInfo) {
		this.router = router;
		this.authInfo = authInfo;
	}

	public static RouterConfirmDialogFragment newInstance(Router router,
			AuthInfo authInfo) {
		// TODO Auto-generated method stub
		RouterConfirmDialogFragment rcdf = new RouterConfirmDialogFragment();
		rcdf.router = router;
		rcdf.authInfo = authInfo;
		return rcdf;
	}
}