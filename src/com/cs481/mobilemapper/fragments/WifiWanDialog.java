package com.cs481.mobilemapper.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.mobilemapper.AuthInfo;
import com.cs481.mobilemapper.CommandCenterActivity;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.Utility;
import com.cs481.mobilemapper.responses.status.wlan.WAP;

public class WifiWanDialog extends DialogFragment {
	WAP wap;
	AuthInfo authInfo;
	Context context;
	
    /**
     * This constructor must be empty or the Fragment won't be able to start.
     */
    public WifiWanDialog() {
    	Log.i(CommandCenterActivity.TAG, "Created fragment.");
    	//context = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	//Resources resources = getResources();
    	//CommandCenterActivity activity = (CommandCenterActivity) getActivity();
    	//authInfo = activity.getAuthInfo();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(wap.getSsid());
        
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_wifiwan, null);
        alertDialogBuilder.setView(dialogView);
        
        //Get dynamic content, set font to BOLD
        SpannableString securityString = new SpannableString(wap.getAuthmode());
        securityString.setSpan(new StyleSpan(Typeface.BOLD), 0, securityString.length(), 0);
        
        SpannableString signalString = new SpannableString(Utility.rssiToSignalLiteral(wap.getRssi()));
        signalString.setSpan(new StyleSpan(Typeface.BOLD), 0, signalString.length(), 0);
        
        //Set the dynamic content
        TextView securityType = (TextView) dialogView.findViewById(R.id.wapconnect_securitytype_value);
        securityType.setText(securityString);
        
        TextView signalStrength = (TextView) dialogView.findViewById(R.id.wapconnect_signalstrength_value);
        signalStrength.setText(signalString);
        
        
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Connecting as WAN...", Toast.LENGTH_LONG).show();
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

    /**
     * Sets the authorization information and the router information, allowing this dialog to navigate to the CommandCenter activity with all required information.
     * @param router Router object containing this router information
     * @param authInfo Auth information allowing the user to edit this router
     */
	public void setData(WAP wap, AuthInfo authInfo) {
		this.wap = wap;
		this.authInfo = authInfo;
	}
}