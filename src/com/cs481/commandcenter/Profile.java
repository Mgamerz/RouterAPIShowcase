package com.cs481.commandcenter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Profile object is stored in the database and defines a profile
 * @author Mike Perez
 */
public class Profile implements Parcelable {

	private String profileName; // e.g. "Cloud Router"
	private AuthInfo authInfo; // Authentication information:
	
	/**
	 * Gets the profile name.
	 * @return Profile name
	 */
	public String getProfileName() {
		return profileName;
	}

	/**
	 * Sets the profile name.
	 * @param profileName Profile name to set
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	/**
	 * Gets this profiles authinfo for connecting.
	 * @return authinfo for connecting
	 */
	public AuthInfo getAuthInfo() {
		return authInfo;
	}

	/**
	 * Sets the authinfo this profile is associated with.
	 * @param authInfo Authinfo to set to this profile
	 */
	public void setAuthInfo(AuthInfo authInfo) {
		this.authInfo = authInfo;
	}

	/**
	 * Empty constructor. Required for parcel building.
	 */
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
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authInfo == null) ? 0 : authInfo.hashCode());
		result = prime * result + ((profileName == null) ? 0 : profileName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Profile other = (Profile) obj;
		if (profileName == null) {
			if (other.profileName != null)
				return false;
		} else if (!profileName.equals(other.profileName))
			return false;
		return true;
	}
}