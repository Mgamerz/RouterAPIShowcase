package com.cs481.mobilemapper.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.responses.ecm.routers.Router;

public class RouterConfirmDialogFragment extends DialogFragment {
	Router router;
	AuthInfo authInfo;
	//Context context;
	
    /**
     * This constructor must be empty or the Fragment won't be able to start.
     */
    public RouterConfirmDialogFragment() {
    	Log.i(CommandCenterActivity.TAG, "Created fragment.");
    	//context = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
        	router = savedInstanceState.getParcelable("router");
        	authInfo = savedInstanceState.getParcelable("authInfo");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		ContextThemeWrapper wrapper = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light);

        AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(wrapper);

        //HoloDialogBuilder  alertDialogBuilder = new HoloDialogBuilder(getActivity());
        alertDialogBuilder.setTitle(router.getName());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        alertDialogBuilder.setView(inflater.inflate(R.layout.dialog_routerconfirm, null));
        
        //alertDialogBuilder.setTitleColor(getResources().getString(R.color.Black));
        //null should be your on click listener
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), CommandCenterActivity.class);
				intent.putExtra("ip", "");
				intent.putExtra("pass", authInfo.getPassword());
				intent.putExtra("ecm", true);
				intent.putExtra("id", router.getId());
				intent.putExtra("user", authInfo.getUsername());
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
    
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putParcelable("router", router);
    	outState.putParcelable("authInfo", authInfo);
    }

    /**
     * Sets the authorization information and the router information, allowing this dialog to navigate to the CommandCenter activity with all required information.
     * @param router Router object containing this router information
     * @param authInfo Auth information allowing the user to edit this router
     */
	public void setData(Router router, AuthInfo authInfo) {
		this.router = router;
		this.authInfo = authInfo;
	}
}