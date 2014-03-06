package com.cs481.commandcenter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Profile object is stored in the database and defines a profile
 * 
 * @author Mgamerz
 * 
 */
public class Profile implements Parcelable {
	private String profileName; // e.g. "Cloud Router"
	private AuthInfo authInfo; // Authentication information:
	
	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public AuthInfo getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(AuthInfo authInfo) {
		this.authInfo = authInfo;
	}

	public Profile(){
		//empty constructor
	}

    protected Profile(Parcel in) {
        profileName = in.readString();
        authInfo = (AuthInfo) in.readValue(AuthInfo.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(profileName);
        dest.writeValue(authInfo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}