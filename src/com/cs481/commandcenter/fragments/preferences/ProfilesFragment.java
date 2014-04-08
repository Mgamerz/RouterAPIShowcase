package com.cs481.commandcenter.fragments.preferences;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.cs481.commandcenter.Profile;
import com.cs481.commandcenter.R;
import com.cs481.commandcenter.Utility;

/**
 * Preference Fragment for the ui preferences page. THIS MUST RUN IN ITS OWN
 * ACTIVITY AS IT DOES NOT WORK WITH THE SUPPORT FRAGMENT MANAGER...
 * @author Mike Perez
 */
public class ProfilesFragment extends Fragment {
	private ExpandableListView mExpandableList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profileeditor, container, false);

		ArrayList<Profile> profilesArray = Utility.getProfiles(getActivity());
		mExpandableList = (ExpandableListView) v.findViewById(R.id.expandable_profilelist);

		if (profilesArray.size() == 0) {
			TextView banner = (TextView) v.findViewById(R.id.profilemanager_banner);
			banner.setText(getResources().getString(R.string.no_profiles));
			banner.setGravity(Gravity.CENTER);
			mExpandableList.setVisibility(ExpandableListView.GONE);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			//params.weight = 1.0f;
			params.gravity = Gravity.CENTER;

			LinearLayout profilesLayout = (LinearLayout) v.findViewById(R.id.profilemanager_layout);
			profilesLayout.setLayoutParams(params);

			return v;
		}

		// sets the adapter that provides data to the list.
		final ProfileListAdapter cla = new ProfileListAdapter(getActivity(), profilesArray);

		mExpandableList.setAdapter(cla);
		return v;
	}

	/**
	 * Adapter template pulled from ALRaP project
	 */
	private class ProfileListAdapter extends BaseExpandableListAdapter {

		private LayoutInflater inflater;
		private ArrayList<Profile> profiles;

		public ProfileListAdapter(Context context, ArrayList<Profile> profiles) {
			this.profiles = profiles;
			inflater = LayoutInflater.from(context);
		}

		@Override
		// counts the number of group/profile items so the list knows how many
		// times calls getGroupView() method
		public int getGroupCount() {
			return profiles.size();
		}

		@Override
		// counts the number of children items so the list knows how many times
		// calls getChildView() method
		public int getChildrenCount(int i) {
			Profile profile = profiles.get(i);
			if (profile.getAuthInfo().isEcm()) {
				return 1;
			} else {
				return 3;
			}
		}

		@Override
		// gets the title of each InstructionType/group
		public Object getGroup(int i) {
			return profiles.get(i).getProfileName();
		}

		@Override
		// gets the name of each item
		public Object getChild(int i, int i1) {
			return profiles.get(i);
		}

		@Override
		public long getGroupId(int i) {
			return i;
		}

		@Override
		public long getChildId(int i, int i1) {
			return i1;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		// in this method you must set the text to see the InstructionType/group
		// on the list
		public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {

			if (view == null) {
				view = inflater.inflate(R.layout.listrow_profilemanager, viewGroup, false);
			}

			final Profile profile = (Profile) profiles.get(i);
			TextView textView = (TextView) view.findViewById(R.id.profilerow_title);
			// "i" is the position of the profile/group in the list
			textView.setText(profile.getProfileName());

			// Profile image
			ImageView profileIcon = (ImageView) view.findViewById(R.id.profilerow_image);

			if (profile.getAuthInfo().isEcm()) {
				// set ecm cloud icon
				profileIcon.setImageResource(R.drawable.ic_profile_ecm);
			} else {
				// set normal icon
				profileIcon.setImageResource(R.drawable.ic_profile_local);
			}

			ImageView deleteIcon = (ImageView) view.findViewById(R.id.profilerow_deleteicon);
			deleteIcon.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Toast.makeText(getActivity(), "Clicked delete on profile", Toast.LENGTH_LONG).show();
					Utility.deleteProfile(getActivity(), profile);
					profiles.remove(i);
					notifyDataSetChanged();
				}
			});

			// return the entire view
			return view;
		}

		@Override
		// in this method you must set the text to see the children on the list
		public View getChildView(int profileindex, int profileinfo_index, boolean b, View view, ViewGroup viewGroup) {
			if (view == null) {
				view = inflater.inflate(R.layout.expandable_profilechild, viewGroup, false);
			}

			Profile profile = profiles.get(profileindex);
			TextView descriptor = (TextView) view.findViewById(R.id.expandableprofile_text);
			TextView value = (TextView) view.findViewById(R.id.expandableprofile_value);
			Resources resources = getResources();
			switch (profileinfo_index) {
			case 0:
				if (profile.getAuthInfo().isEcm()) {
					descriptor.setText(resources.getString(R.string.profiledescriptor_routerid));
					value.setText(profile.getAuthInfo().getRouterId());
				} else {
					descriptor.setText(resources.getString(R.string.profiledescriptor_routerip));
					value.setText(profile.getAuthInfo().getRouterip());
				}
				break;
			case 1:
				if (profile.getAuthInfo().isEcm()) {
					break; // we don't care
				} else {
					descriptor.setText(resources.getString(R.string.profiledescriptor_routerport));
					value.setText(Integer.toString(profile.getAuthInfo().getRouterport()));
				}
				break;

			case 2:
				if (profile.getAuthInfo().isEcm()) {
					break; // we don't care
				} else {
					descriptor.setText(resources.getString(R.string.profiledescriptor_uses_ssl));
					String httpsvalue = resources.getString(profile.getAuthInfo().isHttps() ? R.string.yes : R.string.no);
					value.setText(httpsvalue);
				}
				break;
			}

			// return the entire view
			return view;
		}

		@Override
		public boolean isChildSelectable(int i, int i1) {
			return true;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			/* used to make the notifyDataSetChanged() method work */
			super.registerDataSetObserver(observer);
		}
	}
}