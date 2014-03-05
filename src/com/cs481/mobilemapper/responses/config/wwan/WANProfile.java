package com.cs481.mobilemapper.responses.config.wwan;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This class defines a WAN profile on the router. Putting this to the router
 * will define a new WAP that this router will attempt to connect to when it's
 * seen in the survey list.
 * 
 * @author Mgamerz
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = WANProfileSerializer.class)
public class WANProfile implements Parcelable {

	private String serializePassword; // This password is serializd properly by
										// the serializer

	public String getSerializePassword() {
		return serializePassword;
	}

	@JsonIgnore
	public void setSerializePassword(String serializePassword) {
		this.serializePassword = serializePassword;
	}

	@JsonProperty("authmode")
	private String authmode;

	@JsonProperty("bssid")
	private String bssid;

	@JsonProperty("enabled")
	private boolean enabled;

	@JsonProperty("ssid")
	private String ssid;

	@JsonProperty("uid")
	private String uid;

	@JsonProperty("wpacipher")
	private String wpacipher;

	@JsonProperty("wpapsk")
	private String wpapsk;

	public String getAuthmode() {
		return this.authmode;
	}

	public void setAuthmode(String authmode) {
		this.authmode = authmode;
	}

	public String getBssid() {
		return this.bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getSsid() {
		return this.ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getWpacipher() {
		return this.wpacipher;
	}

	public void setWpacipher(String wpacipher) {
		this.wpacipher = wpacipher;
	}

	public String getWpapsk() {
		return this.wpapsk;
	}

	public void setWpapsk(String wpapsk) {
		this.wpapsk = wpapsk;
	}

	protected WANProfile(Parcel in) {
		authmode = in.readString();
		bssid = in.readString();
		enabled = in.readByte() != 0x00;
		ssid = in.readString();
		uid = in.readString();
		wpacipher = in.readString();
		wpapsk = in.readString();
	}

	public WANProfile() {
		// Required empty constructor
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(authmode);
		dest.writeString(bssid);
		dest.writeByte((byte) (enabled ? 0x01 : 0x00));
		dest.writeString(ssid);
		dest.writeString(uid);
		dest.writeString(wpacipher);
		dest.writeString(wpapsk);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<WANProfile> CREATOR = new Parcelable.Creator<WANProfile>() {
		@Override
		public WANProfile createFromParcel(Parcel in) {
			return new WANProfile(in);
		}

		@Override
		public WANProfile[] newArray(int size) {
			return new WANProfile[size];
		}
	};

	// Auto Generated Eclipse equals and hash code.

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		WANProfile other = (WANProfile) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}

}