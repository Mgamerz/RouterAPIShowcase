package com.cs481.mobilemapper.fragments;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cs481.mobilemapper.DashboardListRow;
import com.cs481.mobilemapper.R;
import com.cs481.mobilemapper.WlanListRow;
import com.cs481.mobilemapper.fragments.DashboardFragment.DashboardAdapter;



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
    	ArrayList<WlanListRow> rows = new ArrayList<WlanListRow>();
    	
    	rows.add(new WlanListRow(1, "Boise-Wireless", "AdHoc - B/G/N"));
    	rows.add(new WlanListRow(2, "Boise-Guest", "AdHoc - B/G/N"));
    	rows.add(new WlanListRow(3, "CS481", "AdHoc - B/G/N"));
    	rows.add(new WlanListRow(4, "Cloud Router", "AdHoc - B/G/N"));
    	setListAdapter(new WlanAdapter(getActivity(), rows));
    }
    
    public class WlanAdapter extends ArrayAdapter<WlanListRow> {
		private final Context context;
		private final ArrayList<WlanListRow> rows;

		public WlanAdapter(Context context,
				ArrayList<WlanListRow> rows) {
			super(context, R.layout.wlan_network_listrow, rows);
			this.context = context;
			this.rows = rows;
		}

		@Override
		public WlanListRow getItem(int position) {
			return rows.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			
			View rowView = inflater.inflate(R.layout.wlan_network_listrow, parent,
					false);
			TextView title = (TextView) rowView
					.findViewById(R.id.wlan_ssid_text);
			title.setText(rows.get(position).getTitle());
			TextView subtitle = (TextView) rowView
					.findViewById(R.id.wlan_type_mode_text);
			subtitle.setText(rows.get(position).getSubtitle());
			return rowView;
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		
	} 
    
}

