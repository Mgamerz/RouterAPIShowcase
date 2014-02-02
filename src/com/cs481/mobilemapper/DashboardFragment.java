package com.cs481.mobilemapper;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DashboardFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash, container, false);
    }
    
    @Override
    public void onStart(){
    	super.onStart();
    	ListView list = (ListView) getView().findViewById(R.id.overview_list);
    	ArrayList<ListRow> rows = new ArrayList<ListRow>();
    	
    	rows.add(new ListRow("Wireless", "2 CLIENTS"));
    	rows.add(new ListRow("LAN", "DHCP - 2 Clients"));
    	rows.add(new ListRow("WAN", "3 Forwarded ports"));
    	list.setAdapter(new DashboardAdapter(getActivity(), rows));
    }
   
    public class DashboardAdapter extends ArrayAdapter<ListRow> {
    	  private final Context context;
    	  private final ArrayList<ListRow> rows;

    	  public DashboardAdapter(Context context, ArrayList<ListRow> rows) {
    	    super(context, R.layout.listview_row, rows);
    	    this.context = context;
    	    this.rows = rows;
    	  }

    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    	    LayoutInflater inflater = (LayoutInflater) context
    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View rowView = inflater.inflate(R.layout.listview_row, parent, false);
    	    TextView title = (TextView) rowView.findViewById(R.id.listview_title);
    	    title.setText(rows.get(position).getTitle());
    	    TextView subtitle = (TextView) rowView.findViewById(R.id.listview_subtitle);
    	    subtitle.setText(rows.get(position).getSubtitle());
    	    return rowView;
    	  }
    	} 
    
}

