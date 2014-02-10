package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs481.mobilemapper.DashboardListRow;
import com.cs481.mobilemapper.R;



public class WlanFragment extends ListFragment implements OnRefreshListener{
    private PullToRefreshLayout mPullToRefreshLayout;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wlan, container, false);
    }
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
	       // This is the View which is created by ListFragment
        ViewGroup viewGroup = (ViewGroup) view;

        // We need to create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
        
                // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                .insertLayoutInto(viewGroup)
                
                // We need to mark the ListView and it's Empty View as pullable
                // This is because they are not dirent children of the ViewGroup
                .theseChildrenArePullable(getListView(), getListView().getEmptyView())
                
                // We can now complete the setup as desired
                .listener(this)
                .setup(mPullToRefreshLayout);
	}
    
    @Override
    public void onStart(){
    	super.onStart();
        ///You will setup the action bar with pull to refresh layout
        mPullToRefreshLayout = (PullToRefreshLayout) getView().findViewById(R.id.ptr_layout);
        ActionBarPullToRefresh.from(getActivity())
          .allChildrenArePullable()
          .listener(this)
          .setup(mPullToRefreshLayout);
    	
    	//ListView list = (ListView) getView().findViewById(R.id.overview_list);
    	ArrayList<DashboardListRow> rows = new ArrayList<DashboardListRow>();
    	
    	//rows.add(new ListRow("Wireless", "2 CLIENTS"));
    	//rows.add(new ListRow("LAN", "DHCP - 2 Clients"));
    	//rows.add(new ListRow("WAN", "3 Forwarded ports"));
    	//rows.add(new ListRow("GPIO", "Dimmed Mode"));
    	//setListAdapter(new DashboardAdapter(getActivity(), rows));
    }

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		
	} 
    
}

