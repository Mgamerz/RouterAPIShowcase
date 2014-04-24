package com.cs481.commandcenter.listrows;

import android.os.Parcel;
import android.os.Parcelable;

import com.cs481.commandcenter.Profile;

/**
 * Defines the data that is used to create a list element in the ECM Profile
 * list. Also defines data that is relevant when clicked between the user and
 * the apps next tasks (navigating to the management interface).
 * 
 * @author Mike Perez
 */
public class ProfileListRow implements Parcelable {
	private String title = "";
	private boolean ecm = false;
	private Profile profile;

	public ProfileListRow(Profile profile) {
		this.profile = profile;
		this.title = profile.getProfileName();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isEcm() {
		return ecm;
	}

	public void setEcm(boolean ecm) {
		this.ecm = ecm;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

    protected ProfileListRow(Parcel in) {
        title = in.readString();
        ecm = in.readByte() != 0x00;
        profile = (Profile) in.readValue(Profile.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeByte((byte) (ecm ? 0x01 : 0x00));
        dest.writeValue(profile);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProfileListRow> CREATOR = new Parcelable.Creator<ProfileListRow>() {
        @Override
        public ProfileListRow createFromParcel(Parcel in) {
            return new ProfileListRow(in);
        }

        @Override
        public ProfileListRow[] newArray(int size) {
            return new ProfileListRow[size];
        }
    };
}