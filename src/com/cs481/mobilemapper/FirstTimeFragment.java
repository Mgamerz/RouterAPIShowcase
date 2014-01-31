package com.cs481.mobilemapper;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cs481.mobilemapper.debug.DebugActivity;

public class FirstTimeFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstancedState){
		super.onCreate(savedInstancedState);
		setHasOptionsMenu (true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_firstrun, container,
				false);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		setupUI();
	}

	public void setupUI() {
		EditText ipAddress = (EditText) getView().findViewById(R.id.router_ip);
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				if (end > start) {
					String destTxt = dest.toString();
					String resultingTxt = destTxt.substring(0, dstart)
							+ source.subSequence(start, end)
							+ destTxt.substring(dend);
					if (!resultingTxt
							.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
						return "";
					} else {
						String[] splits = resultingTxt.split("\\.");
						for (int i = 0; i < splits.length; i++) {
							if (Integer.valueOf(splits[i]) > 255) {
								return "";
							}
						}
					}
				}
				return null;
			}
		};
		ipAddress.setFilters(filters);
	}

	@Override
	public void onCreateOptionsMenu(
	      Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.firstrun_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // handle item selection
	   switch (item.getItemId()) {
	      case R.id.fr_debug:
	    	  	Intent intent = new Intent(getActivity(), DebugActivity.class);
	  			CheckBox gateway = (CheckBox) getView().findViewById(R.id.use_default_gateway);
	  			String routerip = "";
	  			if (gateway.isChecked()) {
					final WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
					final DhcpInfo dhcp = manager.getDhcpInfo();
					// This method is deprecated because we use IPv4 and the new one is IPv6. IPv6 is way more complicated and used in more
					// telecom related things (public facing things). It still works so ignore the deprecation.
					routerip = Formatter.formatIpAddress(dhcp.gateway); 
				} else {
					EditText iptext = (EditText) getView().findViewById(R.id.router_ip);
					routerip = iptext.getText().toString();
				}
	    	  
	    	  intent.putExtra("ip", routerip);
	    	  String password = ((EditText) getView().findViewById(R.id.router_password)).getText().toString();
	    	  intent.putExtra("password", password);
	    	  startActivity(intent);
	         return true;
	      default:
	         return super.onOptionsItemSelected(item);
	   }
	}

}
